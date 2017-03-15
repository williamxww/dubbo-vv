package com.bow.adaptive;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.registry.Registry;
import com.alibaba.dubbo.registry.RegistryFactory;

/**
 * @author vv
 * @since 2017/1/13.
 */
public class RegistryFactory$Adpative implements RegistryFactory {
    public Registry getRegistry(URL url) {
        if (url == null)
            throw new IllegalArgumentException("url == null");
        String extName = (url.getProtocol() == null ? "dubbo" : url.getProtocol());
        if (extName == null)
            throw new IllegalStateException(
                    "Fail to get extension(com.alibaba.dubbo.registry.RegistryFactory) name from url(" + url.toString()
                            + ") use keys([protocol])");
        RegistryFactory extension = ExtensionLoader.getExtensionLoader(RegistryFactory.class).getExtension(extName);
        return extension.getRegistry(url);
    }
}
