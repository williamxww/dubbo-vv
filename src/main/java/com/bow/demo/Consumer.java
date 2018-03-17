package com.bow.demo;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.dubbo.qos.common.Constants;
import com.bow.service.Calculator;

import Demo.Printer;

/**
 * @author vv
 * @since 2016/12/12.
 */
public class Consumer {
	private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

	public static void main(String[] args) throws Exception {
		System.setProperty(Constants.QOS_PORT, "22221");
		ApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "com/bow/demo/consumer.xml" });
		Calculator restCal = context.getBean("restCal", Calculator.class);
		Calculator dubboCal = context.getBean("dubboCal", Calculator.class);
		Printer printer = context.getBean("printer", Printer.class);
		while (true) {
			TimeUnit.SECONDS.sleep(5);
			try {
				LOGGER.info("restCal  result>>> " + restCal.add(1, 2));
				LOGGER.info("dubboCal result>>> " + dubboCal.sub(10, 3));
				LOGGER.info("printer  result>>> " + printer.printString("vv"));
			} catch (Exception e) {

			}
		}
	}
}
