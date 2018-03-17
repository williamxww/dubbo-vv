package com.bow.rpc.protocol;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.ServiceClassHolder;
import com.alibaba.dubbo.rpc.protocol.AbstractProxyProtocol;

import Ice.Communicator;

/**
 * @author vv
 * @since 2017/3/18.
 */
public class IceProtocol extends AbstractProxyProtocol {

	private static final Logger LOGGER = LoggerFactory.getLogger(IceProtocol.class);

	private static final int DEFAULT_PORT = 10000;

	public static final String NAME = "ice";

	private final Map<String, IceServer> servers = new ConcurrentHashMap<>();

	private Communicator communicator = null;

	public IceProtocol() {
		communicator = IceServer.buildCommunicator();
	}

	@Override
	protected <T> Runnable doExport(T impl, Class<T> type, URL url) throws RpcException {
		String addr = getAddr(url);
		Class implClass = ServiceClassHolder.getInstance().popServiceClass();
		IceServer server = servers.get(addr);
		if (server == null) {
			server = new IceServer(addr);
			server.start();
			servers.put(addr, server);
		}
		String identity = type.getSimpleName();
		// 将此实现加入到 ICE server
		server.deploy(identity, implClass);

		final IceServer s = server;
		// 注销时的钩子
		return new Runnable() {
			@Override
			public void run() {
				s.unDeploy(implClass);
			}
		};
	}

	/**
	 * 将url包装为实现了type接口的代理
	 * 
	 * @param type 需要实现的接口
	 * @param url 远端地址
	 * @param <T> T
	 * @return 代理
	 * @throws RpcException e
	 */
	@Override
	protected <T> T doRefer(Class<T> type, URL url) throws RpcException {
		// 获取type对应的ICE代理，其实现了接口xxxPrx，没有实现type
		Object obj = getIceProxy(type, url);
		// 然后将xxxPrx包装为type的实现
		return proxy(type, obj);
	}

	private Object getIceProxy(Class type, URL url) {
		// 找到此type对应的ICE proxy
		String identity = type.getSimpleName();
		Ice.ObjectPrx objectPrx = communicator
				.stringToProxy(identity + ":tcp -h " + url.getHost() + " -p " + url.getPort());
		String helperName = type.getName() + "PrxHelper";
		Method castMethod;
		try {
			Class helperClz = Thread.currentThread().getContextClassLoader().loadClass(helperName);
			castMethod = helperClz.getMethod("checkedCast", Ice.ObjectPrx.class);
		} catch (Exception e) {
			throw new RpcException("Error loading class " + helperName, e);
		}
		Object result;
		try {
			// 通过PrxHelper将ObjectPrx转换为业务Prx，这里会发起远程调用检查是否能转换
			result = castMethod.invoke(null, objectPrx);
		} catch (IllegalAccessException | InvocationTargetException e) {
			throw new RpcException("Error casting ObjectPrx.", e);
		}
		return result;
	}

	private <T> T proxy(Class<T> type, Object target) {
		Object result = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[] { type },
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						Class clz = target.getClass();
						Method targetMethod = clz.getMethod(method.getName(), method.getParameterTypes());
						return targetMethod.invoke(target, args);
					}
				});
		return (T) result;
	}

	@Override
	public int getDefaultPort() {
		return DEFAULT_PORT;
	}
}
