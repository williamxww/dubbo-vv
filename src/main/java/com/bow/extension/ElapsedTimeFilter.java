package com.bow.extension;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;

/**
 * @author vv
 * @since 2017/1/8.
 */
//注释放开就自动激活
//@Activate(group = Constants.CONSUMER)
public class ElapsedTimeFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElapsedTimeFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        long start = System.currentTimeMillis();
        Result result = invoker.invoke(invocation);
        long elapsed = System.currentTimeMillis() - start;
        if (invoker.getUrl() != null) {
            LOGGER.info("{}.{} elapse {} ms", invoker.getInterface(), invocation.getMethodName(), elapsed);
        }
        return result;
    }
}
