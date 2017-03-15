package com.bow.adaptive;

import com.alibaba.dubbo.cache.CacheFactory;
import com.alibaba.dubbo.common.extension.ExtensionLoader;

/**
 * @author vv
 * @since 2017/1/13.
 */
public class CacheFactory$Adpative implements CacheFactory {
    public com.alibaba.dubbo.cache.Cache getCache(com.alibaba.dubbo.common.URL arg0) {
        if (arg0 == null)
            throw new IllegalArgumentException("url == null");
        com.alibaba.dubbo.common.URL url = arg0;
        String extName = url.getParameter("cache", "lru");
        if (extName == null)
            throw new IllegalStateException("Fail to get extension(com.alibaba.dubbo.cache.CacheFactory) name from url("
                    + url.toString() + ") use keys([cache])");
        CacheFactory extension = ExtensionLoader.getExtensionLoader(CacheFactory.class).getExtension(extName);
        return extension.getCache(arg0);
    }
}
