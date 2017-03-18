package com.bow.transport.netty4;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.Version;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.NamedThreadFactory;
import com.alibaba.dubbo.common.utils.NetUtils;
import com.alibaba.dubbo.remoting.ChannelHandler;
import com.alibaba.dubbo.remoting.RemotingException;
import com.alibaba.dubbo.remoting.transport.AbstractClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.internal.SystemPropertyUtil;

import java.util.concurrent.TimeUnit;

/**
 * netty client<br/>
 * Netty4Client 包装了{@link ChannelHandler}<br/>
 * 能够处理sent和received message事件
 *
 * @author vv
 * @since 2016/3/18.
 */
public class Netty4Client extends AbstractClient {

    private static final Logger logger = LoggerFactory.getLogger(Netty4Client.class);

    private Bootstrap bootstrap;

    /**
     * netty channel
     */
    private volatile Channel channel; // volatile, please copy reference to use

    public Netty4Client(final URL url, final ChannelHandler handler) throws RemotingException {
        // 依此经过MultiMessageHandler,HeartbeatHandler处理最终通过Dispatcher选择一种好的策略处理请求(比如丢到线程池里)
        super(url, wrapChannelHandler(url, handler));
    }

    private static final int DEFAULT_EVENT_LOOP_THREADS;

    static {
        DEFAULT_EVENT_LOOP_THREADS = Math.max(1,
                SystemPropertyUtil.getInt("io.netty.eventLoopThreads", Constants.DEFAULT_IO_THREADS));

        if (logger.isDebugEnabled()) {
            logger.debug("-Dio.netty.eventLoopThreads: " + DEFAULT_EVENT_LOOP_THREADS);
        }
    }

    private static final EventLoopGroup WORKER_GROUP = new NioEventLoopGroup(DEFAULT_EVENT_LOOP_THREADS,
            new NamedThreadFactory("NettyClientTCPWorker", true));

    @Override
    protected void doOpen() throws Throwable {
        Netty4Helper.setNettyLoggerFactory();
        bootstrap = new Bootstrap();
        // config
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.group(WORKER_GROUP);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, getTimeout());
        final Netty4Handler nettyHandler = new Netty4Handler(getUrl(), this);
        bootstrap.handler(new ChannelInitializer() {
            public void initChannel(Channel ch) {
                Netty4CodecAdapter adapter = new Netty4CodecAdapter(getCodec(), getUrl(), Netty4Client.this);
                ChannelPipeline channelPipeline = ch.pipeline();
                channelPipeline.addLast(adapter.getDecoder());
                channelPipeline.addLast(adapter.getEncoder());
                channelPipeline.addLast(nettyHandler);
            }
        });

    }

    /**
     * doConnect
     * 
     * @throws Throwable e
     */
    protected void doConnect() throws Throwable {
        long start = System.currentTimeMillis();
        ChannelFuture future = bootstrap.connect(getConnectAddress());
        try {
            boolean ret = future.awaitUninterruptibly(getConnectTimeout(), TimeUnit.MILLISECONDS);

            if (ret && future.isSuccess()) {
                Channel newChannel = future.channel();

                try {
                    // 关闭旧的连接
                    // copy reference
                    Channel oldChannel = Netty4Client.this.channel;
                    if (oldChannel != null) {
                        try {
                            if (logger.isInfoEnabled()) {
                                logger.info("Close old netty channel " + oldChannel + " on create new netty channel "
                                        + newChannel);
                            }
                            oldChannel.close().syncUninterruptibly();
                        } finally {
                            Netty4Channel.removeChannelIfDisconnected(oldChannel);
                        }
                    }
                } finally {
                    if (Netty4Client.this.isClosed()) {
                        try {
                            if (logger.isInfoEnabled()) {
                                logger.info("Close new netty channel " + newChannel + ", because the client closed.");
                            }
                            newChannel.close().syncUninterruptibly();
                        } finally {
                            Netty4Client.this.channel = null;
                            Netty4Channel.removeChannelIfDisconnected(newChannel);
                        }
                    } else {
                        Netty4Client.this.channel = newChannel;
                    }
                }
            } else if (future.cause() != null) {
                throw new RemotingException(this, "client(url: " + getUrl() + ") failed to connect to server "
                        + getRemoteAddress() + ", error message is:" + future.cause().getMessage(), future.cause());
            } else {
                throw new RemotingException(this,
                        "client(url: " + getUrl() + ") failed to connect to server " + getRemoteAddress()
                                + " client-side timeout " + getConnectTimeout() + "ms (elapsed: "
                                + (System.currentTimeMillis() - start) + "ms) from netty client "
                                + NetUtils.getLocalHost() + " using dubbo version " + Version.getVersion());
            }
        } finally {
            if (!isConnected()) {
                future.cancel(true);
            }
        }
    }

    @Override
    protected void doDisConnect() throws Throwable {
        try {
            Netty4Channel.removeChannelIfDisconnected(channel);
        } catch (Throwable t) {
            logger.warn(t.getMessage());
        }
    }

    @Override
    protected void doClose() throws Throwable {
        // WORKER_GROUP.shutdownGracefully()
    }

    /**
     * 获取netty channel的包装类
     * 
     * @return 包装类 {@link Netty4Channel}
     */
    @Override
    protected com.alibaba.dubbo.remoting.Channel getChannel() {
        Channel c = channel;
        if (c == null || !c.isActive())
            return null;
        return Netty4Channel.getOrAddChannel(c, getUrl(), this);
    }

}