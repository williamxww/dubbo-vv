package com.bow.transport.netty4;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.remoting.ChannelHandler;
import com.alibaba.dubbo.remoting.RemotingException;
import com.alibaba.dubbo.remoting.transport.AbstractChannel;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * netty channel 的包装类，实现了{@link com.alibaba.dubbo.remoting.Channel}
 * 
 * @author vv
 * @since 2016/3/18.
 */
final class Netty4Channel extends AbstractChannel {

    private static final Logger LOGGER = LoggerFactory.getLogger(Netty4Channel.class);

    /**
     * 保存netty channel和封装后的Netty4Channel的对应关系
     */
    private static final ConcurrentMap<Channel, Netty4Channel> channelMap = new ConcurrentHashMap();

    /**
     * netty channel
     */
    private final Channel channel;

    private final Map<String, Object> attributes = new ConcurrentHashMap();

    private Netty4Channel(Channel channel, URL url, ChannelHandler handler) {
        super(url, handler);
        if (channel == null) {
            throw new IllegalArgumentException("netty channel == null;");
        }
        this.channel = channel;
    }

    /**
     * 包内访问，netty channel 获取包装类
     * 
     * @param ch netty channel
     * @param url url
     * @param handler handler
     * @return 包装类
     */
    static Netty4Channel getOrAddChannel(Channel ch, URL url, ChannelHandler handler) {
        if (ch == null) {
            return null;
        }
        Netty4Channel ret = channelMap.get(ch);
        if (ret == null) {
            Netty4Channel nc = new Netty4Channel(ch, url, handler);
            if (ch.isActive()) {
                ret = channelMap.putIfAbsent(ch, nc);
            }
            if (ret == null) {
                ret = nc;
            }
        }
        return ret;
    }

    static void removeChannelIfDisconnected(Channel ch) {
        if (ch != null && !ch.isActive()) {
            channelMap.remove(ch);
        }
    }

    public InetSocketAddress getLocalAddress() {
        return (InetSocketAddress) channel.localAddress();
    }

    public InetSocketAddress getRemoteAddress() {
        return (InetSocketAddress) channel.remoteAddress();
    }

    public boolean isConnected() {
        return channel.isActive();
    }

    public void send(Object message, boolean sent) throws RemotingException {
        super.send(message, sent);

        boolean success = true;
        int timeout = 0;
        try {
            ChannelFuture future = channel.writeAndFlush(message);
            if (sent) {
                timeout = getUrl().getPositiveParameter(Constants.TIMEOUT_KEY, Constants.DEFAULT_TIMEOUT);
                success = future.syncUninterruptibly().await(timeout);
            }
            Throwable cause = future.cause();
            if (cause != null) {
                throw cause;
            }
        } catch (Throwable e) {
            throw new RemotingException(this,
                    "Failed to send message " + message + " to " + getRemoteAddress() + ", cause: " + e.getMessage(),
                    e);
        }

        if (!success) {
            throw new RemotingException(this, "Failed to send message " + message + " to " + getRemoteAddress()
                    + "in timeout(" + timeout + "ms) limit");
        }
    }

    public void close() {
        try {
            super.close();
        } catch (Exception e) {
            LOGGER.warn(e.getMessage(), e);
        }
        try {
            removeChannelIfDisconnected(channel);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage(), e);
        }
        try {
            attributes.clear();
        } catch (Exception e) {
            LOGGER.warn(e.getMessage(), e);
        }
        try {
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("Close netty channel " + channel);
            }
            channel.close().syncUninterruptibly();
        } catch (Exception e) {
            LOGGER.warn(e.getMessage(), e);
        }
    }

    public boolean hasAttribute(String key) {
        return attributes.containsKey(key);
    }

    public Object getAttribute(String key) {
        return attributes.get(key);
    }

    public void setAttribute(String key, Object value) {
        if (value == null) { // The null value unallowed in the
                             // ConcurrentHashMap.
            attributes.remove(key);
        } else {
            attributes.put(key, value);
        }
    }

    public void removeAttribute(String key) {
        attributes.remove(key);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (channel.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Netty4Channel other = (Netty4Channel) obj;
        return channel.equals(other.channel);
    }

    @Override
    public String toString() {
        return "NettyChannel [channel=" + channel + "]";
    }

}