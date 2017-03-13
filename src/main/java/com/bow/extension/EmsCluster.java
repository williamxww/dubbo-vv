package com.bow.extension;

import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.cluster.Cluster;
import com.alibaba.dubbo.rpc.cluster.Directory;

/**
 * @author vv
 * @since 2017/1/12.
 */
public class EmsCluster implements Cluster {

    public static final String NAME = "ems";

    /**
     * Merge the directory invokers to a virtual invoker.
     *
     * @param directory
     * @return cluster invoker
     * @throws RpcException
     */
    @Override
    public <T> Invoker<T> join(Directory<T> directory) throws RpcException {
        return new EmsClusterInvoker<T>(directory);
    }
}
