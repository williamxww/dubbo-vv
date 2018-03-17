package com.bow.rpc.protocol;

import com.bow.service.impl.PrinterImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import Ice.Communicator;
import Ice.Util;

/**
 * @author wwxiang
 * @since 2018/3/8.
 */
public class SimpleServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleServer.class);

    private Communicator buildCommunicator() {
        Ice.InitializationData initData = new Ice.InitializationData();
        initData.logger = new Sl4jLoggerI("Ice");
        Ice.Properties properties = Util.createProperties();
        // 日志相关级别定义在类 IceInternal.TraceLevels , 程序中会对比此处设置的级别，大的就输出日志，因此此处都设置为5
        properties.setProperty("Ice.Trace.Network", "5");
        properties.setProperty("Ice.Trace.Protocol", "5");
        // 追踪 连接重试
        properties.setProperty("Ice.Trace.Retry", "5");
        properties.setProperty("Ice.Trace.Locator", "5");
        properties.setProperty("Ice.Trace.Slicing", "5");
        properties.setProperty("Ice.Trace.ThreadPool", "5");
        // 建立连接的超时时间ms
        properties.setProperty("Ice.Override.ConnectTimeout", "1000");
        // 连接失败重试，5s后重连一次，10s后重连一次 (0代表立即重试)
        properties.setProperty("Ice.RetryIntervals", "5000 10000");
        initData.properties = properties;
        Communicator communicator = Util.initialize(initData);
        return communicator;
    }

    public void start() {
        Communicator communicator = null;
        try {
            communicator = buildCommunicator();
            // assemble adapter
            Ice.ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("PrinterAdapter",
                    "tcp -h 127.0.0.1 -p 10000");
            // create identity & servant
            Ice.Object servant = new PrinterImpl();
            adapter.add(servant, communicator.stringToIdentity("Printer"));
            // activate
            adapter.activate();
            LOGGER.info("ICEServer started.");
            communicator.waitForShutdown();
        } catch (Exception e) {
            LOGGER.error("Failed to run ICEServer.", e);
        } finally {
            if (communicator != null) {
                communicator.destroy();
            }
        }
    }

	public static void main(String[] args) {
		SimpleServer server = new SimpleServer();
		server.start();
	}
}
