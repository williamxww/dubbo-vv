package com.bow.adaptive;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.common.threadpool.ThreadPool;

/**
 * @author vv
 * @since 2017/1/13.
 */
public class ThreadPool$Adpative implements ThreadPool {
    public java.util.concurrent.Executor getExecutor(URL url) {
        if (url == null)
            throw new IllegalArgumentException("url == null");
        String extName = url.getParameter("threadpool", "fixed");
        if (extName == null)
            throw new IllegalStateException(
                    "Fail to get extension(com.alibaba.dubbo.common.threadpool.ThreadPool) name from url("
                            + url.toString() + ") use keys([threadpool])");
        ThreadPool extension = ExtensionLoader.getExtensionLoader(ThreadPool.class).getExtension(extName);
        return extension.getExecutor(url);
    }
}
