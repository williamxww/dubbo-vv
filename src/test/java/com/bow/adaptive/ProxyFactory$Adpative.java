package com.bow.adaptive;

import com.alibaba.dubbo.common.extension.ExtensionLoader;
import com.alibaba.dubbo.rpc.ProxyFactory;
import com.alibaba.dubbo.rpc.RpcException;

/**
 * @author vv
 * @since 2017/1/13.
 */
public class ProxyFactory$Adpative implements ProxyFactory {
    public Object getProxy(com.alibaba.dubbo.rpc.Invoker arg0) throws RpcException {
        if (arg0 == null)
            throw new IllegalArgumentException("com.alibaba.dubbo.rpc.Invoker argument == null");
        if (arg0.getUrl() == null)
            throw new IllegalArgumentException("com.alibaba.dubbo.rpc.Invoker argument getUrl() == null");
        com.alibaba.dubbo.common.URL url = arg0.getUrl();
        String extName = url.getParameter("proxy", "javassist");
        if (extName == null)
            throw new IllegalStateException("Fail to get extension(com.alibaba.dubbo.rpc.ProxyFactory) name from url("
                    + url.toString() + ") use keys([proxy])");
        ProxyFactory extension = ExtensionLoader
                .getExtensionLoader(ProxyFactory.class).getExtension(extName);
        return extension.getProxy(arg0);
    }

    public com.alibaba.dubbo.rpc.Invoker getInvoker(Object arg0, Class arg1,
            com.alibaba.dubbo.common.URL arg2) throws RpcException {
        if (arg2 == null)
            throw new IllegalArgumentException("url == null");
        com.alibaba.dubbo.common.URL url = arg2;
        String extName = url.getParameter("proxy", "javassist");
        if (extName == null)
            throw new IllegalStateException("Fail to get extension(com.alibaba.dubbo.rpc.ProxyFactory) name from url("
                    + url.toString() + ") use keys([proxy])");
        ProxyFactory extension = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getExtension(extName);
        return extension.getInvoker(arg0, arg1, arg2);
    }
}
