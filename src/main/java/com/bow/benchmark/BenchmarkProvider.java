package com.bow.benchmark;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.dubbo.qos.common.Constants;

public class BenchmarkProvider {

	public static void main(String[] args) throws InterruptedException {
		System.setProperty(Constants.QOS_PORT, "22221");
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				new String[] {"com/bow/benchmark/provider.xml"});

		System.out.println("server running---");
		Thread.sleep(Long.MAX_VALUE);
	}

}
