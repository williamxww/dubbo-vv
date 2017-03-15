package com.bow.adaptive;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.monitor.Monitor;
import com.alibaba.dubbo.monitor.MonitorFactory;

/**
 * @author vv
 * @since 2017/1/13.
 */
public class MonitorFactory$Adpative implements MonitorFactory {
    public Monitor getMonitor(URL url) {
        if (url == null)
            throw new IllegalArgumentException("url == null");
        String extName = (url.getProtocol() == null ? "dubbo" : url.getProtocol());
        if (extName == null)
            throw new IllegalStateException(
                    "Fail to get extension(com.alibaba.dubbo.monitor.MonitorFactory) name from url(" + url.toString()
                            + ") use keys([protocol])");
        MonitorFactory extension = ExtensionLoader.getExtensionLoader(MonitorFactory.class).getExtension(extName);
        return extension.getMonitor(url);
    }
}
