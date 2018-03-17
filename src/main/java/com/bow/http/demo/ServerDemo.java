package com.bow.http.demo;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.log.Log;
import org.mortbay.log.StdErrLog;
import org.mortbay.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wwxiang
 * @since 2018/3/5.
 */
public class ServerDemo {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServerDemo.class);

	public void start() {

		// jetty server
		Log.setLog(new StdErrLog());
		Log.getLog().setDebugEnabled(false);

		QueuedThreadPool threadPool = new QueuedThreadPool();
		threadPool.setDaemon(true);
		threadPool.setMaxThreads(5);
		threadPool.setMinThreads(5);

		SelectChannelConnector connector = new SelectChannelConnector();
		connector.setHost("127.0.0.1");
		connector.setPort(8080);

		Server server = new Server();
		server.setThreadPool(threadPool);
		server.addConnector(connector);

		// 设置context
		Context context = new Context(Context.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        // 设置Servlet
        ServletHolder servletHolder = new ServletHolder(new RestServletDispatcher());
        servletHolder.setInitOrder(2);
        context.addServlet(servletHolder, "/*");
		try {
			server.start();
			System.out.println("Server started.");
			System.in.read();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ServerDemo demo = new ServerDemo();
		demo.start();
	}
}
