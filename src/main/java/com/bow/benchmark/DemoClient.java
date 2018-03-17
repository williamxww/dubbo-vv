package com.bow.benchmark;

import com.alibaba.dubbo.qos.common.Constants;
import com.bow.service.BenchmarkService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * @author wwxiang
 * @since 2018/3/7.
 */
public class DemoClient {

    public static void main(String[] args) throws IOException {
        System.setProperty(Constants.QOS_PORT, "22222");
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                new String[] {"com/bow/benchmark/consumer.xml"});
        BenchmarkService benchmarkService = applicationContext.getBean("bench", BenchmarkService.class);

        while(true){
            Object result = benchmarkService.echoService("aaaa");
            System.out.println(result);
            System.in.read();
        }
    }
}
