package com.bow.transport.netty4;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.ExecutorUtil;
import com.alibaba.dubbo.common.utils.NamedThreadFactory;
import com.alibaba.dubbo.common.utils.NetUtils;
import com.alibaba.dubbo.remoting.Channel;
import com.alibaba.dubbo.remoting.ChannelHandler;
import com.alibaba.dubbo.remoting.RemotingException;
import com.alibaba.dubbo.remoting.Server;
import com.alibaba.dubbo.remoting.transport.AbstractServer;
import com.alibaba.dubbo.remoting.transport.dispatcher.ChannelHandlers;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

/**
 * NettyServer
 * 
 * @author vv
 * @since 2016/3/18.
 */
public class Netty4Server extends AbstractServer implements Server {

    private static final Logger logger = LoggerFactory.getLogger(Netty4Server.class);

    /**
     * <ip:port, channel>
     */
    private Map<String, Channel> channels;

    private io.netty.channel.Channel channel;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    public Netty4Server(URL url, ChannelHandler handler) throws RemotingException {
        super(url, ChannelHandlers.wrap(handler, ExecutorUtil.setThreadName(url, SERVER_THREAD_POOL_NAME)));
    }

    @Override
    protected void doOpen() throws Throwable {
        Netty4Helper.setNettyLoggerFactory();
        ServerBootstrap bootstrap = new ServerBootstrap();

        final Netty4Handler nettyHandler = new Netty4Handler(getUrl(), this);
        channels = nettyHandler.getChannels();

        bossGroup = new NioEventLoopGroup(1, (new NamedThreadFactory("NettyServerBoss", true)));
        workerGroup = new NioEventLoopGroup(
                getUrl().getPositiveParameter(Constants.IO_THREADS_KEY, Constants.DEFAULT_IO_THREADS),
                new NamedThreadFactory("NettyServerWorker", true));

        bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class);
        bootstrap.childOption(ChannelOption.TCP_NODELAY, false);
        bootstrap.childHandler(new ChannelInitializer() {

            public void initChannel(io.netty.channel.Channel ch) {
                Netty4CodecAdapter adapter = new Netty4CodecAdapter(getCodec(), getUrl(), Netty4Server.this);
                ChannelPipeline channelPipeline = ch.pipeline();
                channelPipeline.addLast(adapter.getDecoder());
                channelPipeline.addLast(adapter.getEncoder());
                channelPipeline.addLast(nettyHandler);
            }
        });

        // bind
        ChannelFuture channelFuture = bootstrap.bind(getBindAddress());

        channelFuture.awaitUninterruptibly();
        channel = channelFuture.channel();

    }

    @Override
    protected void doClose() throws Throwable {
        try {
            if (channel != null) {
                // unbind.
                channel.close().syncUninterruptibly();
            }
        } catch (Throwable e) {
            logger.warn(e.getMessage(), e);
        }
        try {
            Collection<Channel> channels = getChannels();
            if (channels != null && channels.size() > 0) {
                for (Channel channel : channels) {
                    try {
                        channel.close();
                    } catch (Throwable e) {
                        logger.warn(e.getMessage(), e);
                    }
                }
            }
        } catch (Throwable e) {
            logger.warn(e.getMessage(), e);
        }

        try {
            // and then shutdown the thread pools
            if (bossGroup != null) {
                bossGroup.shutdownGracefully();
            }
            if (workerGroup != null) {
                workerGroup.shutdownGracefully();
            }
        } catch (Throwable e) {
            logger.warn(e.getMessage(), e);
        }

        try {
            if (channels != null) {
                channels.clear();
            }
        } catch (Throwable e) {
            logger.warn(e.getMessage(), e);
        }
    }

    public Collection<Channel> getChannels() {
        Collection<Channel> chs = new HashSet<Channel>();
        for (Channel channel : this.channels.values()) {
            if (channel.isConnected()) {
                chs.add(channel);
            } else {
                channels.remove(NetUtils.toAddressString(channel.getRemoteAddress()));
            }
        }
        return chs;
    }

    public Channel getChannel(InetSocketAddress remoteAddress) {
        return channels.get(NetUtils.toAddressString(remoteAddress));
    }

    public boolean isBound() {
        return channel.isRegistered();
    }

}