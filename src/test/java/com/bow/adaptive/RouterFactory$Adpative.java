package com.bow.adaptive;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.rpc.cluster.RouterFactory;

/**
 * @author vv
 * @since 2017/1/13.
 */
public class RouterFactory$Adpative implements RouterFactory {
    public com.alibaba.dubbo.rpc.cluster.Router getRouter(com.alibaba.dubbo.common.URL url) {
        if (url == null)
            throw new IllegalArgumentException("url == null");
        String extName = url.getProtocol();
        if (extName == null)
            throw new IllegalStateException(
                    "Fail to get extension(com.alibaba.dubbo.rpc.cluster.RouterFactory) name from url(" + url.toString()
                            + ") use keys([protocol])");
        RouterFactory extension = ExtensionLoader.getExtensionLoader(RouterFactory.class).getExtension(extName);
        return extension.getRouter(url);
    }
}
