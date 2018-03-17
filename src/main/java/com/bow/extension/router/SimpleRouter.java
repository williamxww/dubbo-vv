package com.bow.extension.router;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.cluster.Router;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author wwxiang
 * @since 2018/3/14.
 */
public class SimpleRouter implements Router, Comparable<Router>{

    private final URL url;

    public SimpleRouter(URL url){
        this.url = url;
    }
    @Override
    public URL getUrl() {
        return url;
    }

    @Override
    public <T> List<Invoker<T>> route(List<Invoker<T>> invokers, URL url, Invocation invocation) throws RpcException {
        // 根据URL中的路由规则，对invokers进行路由过滤
        System.out.println("routing");
        return invokers;
    }

    @Override
    public int compareTo(Router o) {
        return 0;
    }
}
