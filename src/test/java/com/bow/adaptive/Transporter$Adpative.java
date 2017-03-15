package com.bow.adaptive;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.remoting.Client;
import com.alibaba.dubbo.remoting.RemotingException;
import com.alibaba.dubbo.remoting.Server;
import com.alibaba.dubbo.remoting.Transporter;

/**
 * @author vv
 * @since 2017/1/13.
 */
public class Transporter$Adpative implements Transporter {
    public Client connect(URL url, com.alibaba.dubbo.remoting.ChannelHandler arg1) throws RemotingException {
        if (url == null)
            throw new IllegalArgumentException("url == null");
        String extName = url.getParameter("client", url.getParameter("transporter", "netty"));
        if (extName == null)
            throw new IllegalStateException(
                    "Fail to get extension(com.alibaba.dubbo.remoting.Transporter) name from url(" + url.toString()
                            + ") use keys([client, transporter])");
        Transporter extension = ExtensionLoader.getExtensionLoader(Transporter.class).getExtension(extName);
        return extension.connect(url, arg1);
    }

    public Server bind(URL url, com.alibaba.dubbo.remoting.ChannelHandler arg1)
            throws RemotingException {
        if (url == null)
            throw new IllegalArgumentException("url == null");
        String extName = url.getParameter("server", url.getParameter("transporter", "netty"));
        if (extName == null)
            throw new IllegalStateException(
                    "Fail to get extension(com.alibaba.dubbo.remoting.Transporter) name from url(" + url.toString()
                            + ") use keys([server, transporter])");
        Transporter extension = ExtensionLoader.getExtensionLoader(Transporter.class).getExtension(extName);
        return extension.bind(url, arg1);
    }
}
