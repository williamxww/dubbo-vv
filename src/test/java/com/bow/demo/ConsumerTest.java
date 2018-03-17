package com.bow.demo;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.bow.service.Calculator;

/**
 * @author wwxiang
 * @since 2018/3/8.
 */
public class ConsumerTest {

	public static void main(String[] args) throws IOException {
		ApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "com/bow/demo/consumer.xml" });
		Calculator dubboCal = context.getBean("dubboCal", Calculator.class);
		while (true) {
			try{
				System.in.read();
				int a = dubboCal.add(1, 1);
				System.out.println(a);
			}catch (Exception e){
				e.printStackTrace();
			}

		}
	}
}