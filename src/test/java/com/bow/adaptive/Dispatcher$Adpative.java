package com.bow.adaptive;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.remoting.Dispatcher;

/**
 * @author vv
 * @since 2017/1/13.
 */
public class Dispatcher$Adpative implements Dispatcher {
    public com.alibaba.dubbo.remoting.ChannelHandler dispatch(com.alibaba.dubbo.remoting.ChannelHandler arg0,
            com.alibaba.dubbo.common.URL arg1) {
        if (arg1 == null)
            throw new IllegalArgumentException("url == null");
        com.alibaba.dubbo.common.URL url = arg1;
        String extName = url.getParameter("dispatcher",
                url.getParameter("dispather", url.getParameter("channel.handler", "all")));
        if (extName == null)
            throw new IllegalStateException(
                    "Fail to get extension(com.alibaba.dubbo.remoting.Dispatcher) name from url(" + url.toString()
                            + ") use keys([dispatcher, dispather, channel.handler])");
        Dispatcher extension = (Dispatcher) ExtensionLoader
                .getExtensionLoader(Dispatcher.class).getExtension(extName);
        return extension.dispatch(arg0, arg1);
    }
}
