package com.bow.adaptive;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.rpc.cluster.ConfiguratorFactory;

/**
 * @author vv
 * @since 2017/1/13.
 */
public class ConfiguratorFactory$Adpative implements ConfiguratorFactory {
    public com.alibaba.dubbo.rpc.cluster.Configurator getConfigurator(URL url) {
        if (url == null)
            throw new IllegalArgumentException("url == null");
        String extName = url.getProtocol();
        if (extName == null)
            throw new IllegalStateException(
                    "Fail to get extension(com.alibaba.dubbo.rpc.cluster.ConfiguratorFactory) name from url("
                            + url.toString() + ") use keys([protocol])");
        ConfiguratorFactory extension = ExtensionLoader.getExtensionLoader(ConfiguratorFactory.class)
                .getExtension(extName);
        return extension.getConfigurator(url);
    }
}
