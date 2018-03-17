package com.bow.rpc.protocol;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author wwxiang
 * @since 2018/3/9.
 */
public class IceProvider {

	public static void main(String[] args) throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "com/bow/rpc/protocol/provider.xml" });
        System.in.read();
	}
}
