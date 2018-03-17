package com.bow.registry;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.dubbo.qos.common.Constants;
import com.bow.service.Calculator;

/**
 * @author vv
 * @since 2016/12/12.
 */
public class RegistryConsumer {
	private static final Logger LOGGER = LoggerFactory.getLogger(RegistryConsumer.class);

	public static void main(String[] args) throws Exception {
		System.setProperty(Constants.QOS_PORT, "22221");
		ApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "com/bow/registry/consumer.xml" });
		while (true) {
			System.in.read();
			Calculator dubboCal = context.getBean("dubboCal", Calculator.class);
			LOGGER.info("dubboCal result>>> " + dubboCal.sub(10, 3));

		}
	}
}
