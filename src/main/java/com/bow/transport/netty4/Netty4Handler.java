package com.bow.transport.netty4;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.utils.NetUtils;
import com.alibaba.dubbo.remoting.Channel;
import com.alibaba.dubbo.remoting.ChannelHandler;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPromise;
import io.netty.channel.ChannelHandler.Sharable;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * netty pipeline 中最后一个handler<br/>
 * 1.缓存channel
 * 2. 处理消息
 *
 *
 * @author vv
 * @since 2016/3/18.
 */
@Sharable
public class Netty4Handler extends ChannelDuplexHandler {

    // <ip:port, channel>
    private final Map<String, Channel> channels = new ConcurrentHashMap();

    private final URL url;

    private final ChannelHandler handler;

    public Netty4Handler(URL url, ChannelHandler handler) {
        if (url == null) {
            throw new IllegalArgumentException("url == null");
        }
        if (handler == null) {
            throw new IllegalArgumentException("handler == null");
        }
        this.url = url;
        this.handler = handler;
    }

    /**
     * @return Map<ip:port, channel>
     */
    public Map<String, Channel> getChannels() {
        return channels;
    }

    /**
     * 连接建立
     * 
     * @param ctx ctx
     * @throws Exception e
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        Netty4Channel channel = Netty4Channel.getOrAddChannel(ctx.channel(), url, handler);
        try {
            if (channel != null) {
                channels.put(NetUtils.toAddressString((InetSocketAddress) ctx.channel().remoteAddress()), channel);
            }
            handler.connected(channel);
        } finally {
            Netty4Channel.removeChannelIfDisconnected(ctx.channel());
        }
    }

    /**
     * 连接断开
     * 
     * @param ctx ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {

        Netty4Channel channel = Netty4Channel.getOrAddChannel(ctx.channel(), url, handler);
        try {
            channels.remove(NetUtils.toAddressString((InetSocketAddress) ctx.channel().remoteAddress()));
            handler.disconnected(channel);
        } finally {
            Netty4Channel.removeChannelIfDisconnected(ctx.channel());
        }
    }

    /**
     * 收到请求
     * 
     * @param ctx ctx
     * @param msg msg
     * @throws Exception e
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        @SuppressWarnings("unchecked")
        Netty4Channel channel = Netty4Channel.getOrAddChannel(ctx.channel(), url, handler);
        try {
            handler.received(channel, msg);
        } finally {
            Netty4Channel.removeChannelIfDisconnected(ctx.channel());
        }

    }

    /**
     * flush 响应
     * 
     * @param ctx ctx
     * @param msg msg
     * @param promise promise
     * @throws Exception e
     */
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        // 写出 响应
        ctx.writeAndFlush(msg, promise);
        Netty4Channel channel = Netty4Channel.getOrAddChannel(ctx.channel(), url, handler);
        try {
            handler.sent(channel, msg);
        } finally {
            Netty4Channel.removeChannelIfDisconnected(ctx.channel());
        }
    }

    /**
     * 捕获异常，移除channel
     * 
     * @param ctx ctx
     * @param cause cause
     * @throws Exception e
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Netty4Channel channel = Netty4Channel.getOrAddChannel(ctx.channel(), url, handler);
        try {
            handler.caught(channel, cause);
        } finally {
            Netty4Channel.removeChannelIfDisconnected(ctx.channel());
        }
    }

}