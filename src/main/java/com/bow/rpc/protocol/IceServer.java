package com.bow.rpc.protocol;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import Ice.Communicator;
import Ice.Util;

/**
 * @author wwxiang
 * @since 2018/3/8.
 */
public class IceServer {

	private static final Logger LOGGER = LoggerFactory.getLogger(IceServer.class);

	private Communicator communicator = null;

	private Ice.ObjectAdapter adapter;

	public IceServer(String address) {
		String[] addAry = address.split(":");
		if (addAry.length != 2) {
			throw new IllegalArgumentException("Param format don't match host:port. " + address);
		}
		String host = addAry[0];
		String port = addAry[1];
		communicator = buildCommunicator();
		adapter = communicator.createObjectAdapterWithEndpoints(address/* adapterName */,
				"tcp -h " + host + " -p " + port);
	}

	public void start() {
		adapter.activate();
		LOGGER.info("Ice server {} started.", adapter.getName());
	}

	public void deploy(String identity, Class implClazz) {
		Ice.Object servant;
		try {
			servant = (Ice.Object) implClazz.newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException("Can't instantiate class " + implClazz.getName());
		}
		adapter.add(servant, communicator.stringToIdentity(identity));
		LOGGER.debug("Deploy {} servant.", identity);
		adapter.activate();
	}

	public void unDeploy(Class implClazz) {
		LOGGER.debug("unDeploy " + implClazz.getName());
	}

	public static Communicator buildCommunicator() {
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
		Communicator communicator = Ice.Util.initialize(initData);
		return communicator;
	}
}
