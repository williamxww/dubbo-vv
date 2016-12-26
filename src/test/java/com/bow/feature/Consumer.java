package com.bow.feature;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.alibaba.dubbo.rpc.RpcContext;
import com.bow.entity.Data;
import com.bow.service.StoreService;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.bow.service.Calculator;

/**
 * @author vv
 * @since 2016/12/12.
 */
public class Consumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

    private ApplicationContext context;

    @Before
    public void setup() {
        System.setProperty("dubbo.application.logger", "slf4j");
        context = new ClassPathXmlApplicationContext(new String[] { "com/bow/feature/consumer.xml" });
    }

    @Test
    public void async() throws Exception {
        StoreService service = context.getBean("s1", StoreService.class);

        // 将调用外部服务转化为callable runnable
        Future<Boolean> f = RpcContext.getContext().asyncCall(new Callable<Boolean>() {
            public Boolean call() throws Exception {
                Data data = new Data();
                boolean r = service.save(data);
                return r;
            }
        });
        System.out.println("already send request");
        boolean result = f.get(5,TimeUnit.SECONDS);
        System.out.println("save result: " + result);
    }

    @Test
    public void async2() throws Exception {
        StoreService service = context.getBean("s1", StoreService.class);
        Data data = new Data();
        service.save(data);
        Future<Boolean> resultFuture = RpcContext.getContext().getFuture();
        System.out.println("already send request");
        boolean result = resultFuture.get();
        System.out.println("save result: " + result);
    }

    @Test
    public void test(){
        while(true){
            try {
                async();
                TimeUnit.SECONDS.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
