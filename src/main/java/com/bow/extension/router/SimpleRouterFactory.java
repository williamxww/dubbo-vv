package com.bow.extension.router;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.cluster.Router;
import com.alibaba.dubbo.rpc.cluster.RouterFactory;

/**
 * @author wwxiang
 * @since 2018/3/14.
 */
public class SimpleRouterFactory implements RouterFactory {
    /**
     *
     * @param url routers下的子节点，用于定义路由规则
     * @return
     */
    @Override
    public Router getRouter(URL url) {
        return new SimpleRouter(url);
    }
}
