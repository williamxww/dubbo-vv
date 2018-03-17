package com.bow.rpc.protocol;

import Demo.Printer;
import Demo.PrinterPrx;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author wwxiang
 * @since 2018/3/9.
 */
public class IceConsumer {

	public static void main(String[] args) throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "com/bow/rpc/protocol/consumer.xml" });
		Printer proxy = context.getBean("printer", Printer.class);
		while (true) {
			System.in.read();
			int size = proxy.printString("sss");
			System.out.println("size " + size);
		}
	}

}
