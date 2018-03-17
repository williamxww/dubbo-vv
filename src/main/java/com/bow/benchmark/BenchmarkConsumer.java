package com.bow.benchmark;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

import com.alibaba.dubbo.qos.common.Constants;
import com.bow.service.BenchmarkService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BenchmarkConsumer extends AbstractBenchmarkClient {

    static Properties properties = new Properties();

    /**
     * 并发的Runable线程，是否使用相同的client进行调用。 true：并发线程只使用一个client（bean实例）调用服务。 false:
     * 每个并发线程使用不同的Client调用服务
     */
    private static BenchmarkService benchmarkService;

    private static boolean isMultiClient;

    public static void main(String[] args) throws IOException {
        System.setProperty(Constants.QOS_PORT, "22222");
        loadProperties();
        int concurrents = Integer.parseInt(properties.getProperty("concurrents"));
        int runtime = Integer.parseInt(properties.getProperty("runtime"));
        String classname = properties.getProperty("classname");
        String params = properties.getProperty("params");
        isMultiClient = Boolean.parseBoolean(properties.getProperty("isMultiClient"));
        if (args.length == 5) {
            concurrents = Integer.parseInt(args[0]);
            runtime = Integer.parseInt(args[1]);
            classname = args[2];
            params = args[3];
            isMultiClient = Boolean.parseBoolean(args[4]);
        }

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                new String[] {"com/bow/benchmark/consumer.xml"});
        benchmarkService = applicationContext.getBean("bench", BenchmarkService.class);
        new BenchmarkConsumer().start(concurrents, runtime, classname, params);
    }

    private static void loadProperties() {
        try {
            properties.load(ClassLoader.getSystemResourceAsStream("com/bow/benchmark/benchmark.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ClientRunnable getClientRunnable(String classname, String params, CyclicBarrier barrier,
            CountDownLatch latch, long startTime, long endTime) {
        BenchmarkService service;
        if (isMultiClient) {
            ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                    new String[] {"com/bow/benchmark/consumer.xml"});
            service = applicationContext.getBean("bench", BenchmarkService.class);
        } else {
            service = benchmarkService;
        }

        Class[] parameterTypes = new Class[] { BenchmarkService.class, String.class, CyclicBarrier.class,
                CountDownLatch.class, long.class, long.class };
        Object[] parameters = new Object[] { service, params, barrier, latch, startTime, endTime };

        ClientRunnable clientRunnable = null;
        try {
            clientRunnable = (ClientRunnable) Class.forName(classname).getConstructor(parameterTypes)
                    .newInstance(parameters);
        } catch (InstantiationException | NoSuchMethodException | ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.getTargetException();
        }

        return clientRunnable;
    }
}
