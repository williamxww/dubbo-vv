package com.bow.adaptive;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.cluster.Cluster;

/**
 * @author vv
 * @since 2017/1/12.
 */
public class Cluster$Adpative implements Cluster {
    public com.alibaba.dubbo.rpc.Invoker join(com.alibaba.dubbo.rpc.cluster.Directory arg0) throws RpcException {
        if (arg0 == null)
            throw new IllegalArgumentException("com.alibaba.dubbo.rpc.cluster.Directory argument == null");
        if (arg0.getUrl() == null)
            throw new IllegalArgumentException("com.alibaba.dubbo.rpc.cluster.Directory argument getUrl() == null");
        com.alibaba.dubbo.common.URL url = arg0.getUrl();
        String extName = url.getParameter("cluster", "failover");
        if (extName == null)
            throw new IllegalStateException(
                    "Fail to get extension(com.alibaba.dubbo.rpc.cluster.Cluster) name from url(" + url.toString()
                            + ") use keys([cluster])");
        Cluster extension = (Cluster) ExtensionLoader
                .getExtensionLoader(Cluster.class).getExtension(extName);
        return extension.join(arg0);
    }
}
