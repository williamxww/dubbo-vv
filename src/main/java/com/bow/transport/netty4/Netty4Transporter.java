package com.bow.transport.netty4;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.remoting.ChannelHandler;
import com.alibaba.dubbo.remoting.Client;
import com.alibaba.dubbo.remoting.RemotingException;
import com.alibaba.dubbo.remoting.Server;
import com.alibaba.dubbo.remoting.Transporter;

/**
 * @author vv
 * @since 2016/3/18.
 */
public class Netty4Transporter implements Transporter {

    public static final String NAME = "netty4";

    /**
     *
     * @param url
     * @param listener
     * @return
     * @throws RemotingException
     */
    public Server bind(URL url, ChannelHandler listener) throws RemotingException {
        return new Netty4Server(url, listener);
    }

    /**
     *
     * @param url
     * @param listener
     * @return
     * @throws RemotingException
     */
    public Client connect(URL url, ChannelHandler listener) throws RemotingException {
        return new Netty4Client(url, listener);
    }

}