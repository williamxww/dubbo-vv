package com.bow.adaptive;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.remoting.zookeeper.ZookeeperTransporter;

/**
 * @author vv
 * @since 2017/1/13.
 */
public class ZookeeperTransporter$Adpative implements ZookeeperTransporter {
    public com.alibaba.dubbo.remoting.zookeeper.ZookeeperClient connect(com.alibaba.dubbo.common.URL arg0) {
        if (arg0 == null)
            throw new IllegalArgumentException("url == null");
        com.alibaba.dubbo.common.URL url = arg0;
        String extName = url.getParameter("client", url.getParameter("transporter", "zkclient"));
        if (extName == null)
            throw new IllegalStateException(
                    "Fail to get extension(com.alibaba.dubbo.remoting.zookeeper.ZookeeperTransporter) name from url("
                            + url.toString() + ") use keys([client, transporter])");
        ZookeeperTransporter extension = ExtensionLoader.getExtensionLoader(ZookeeperTransporter.class)
                .getExtension(extName);
        return extension.connect(arg0);
    }
}
