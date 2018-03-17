package com.bow.benchmark;

import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.bow.entity.FullName;
import com.bow.entity.Person;
import com.bow.service.BenchmarkService;

/**
 * @author wwxiang
 * @since 2018/3/12.
 */
public class BenchmarkConsumerTest {

	public static void main(String[] args) throws IOException {
		Person person = new Person();
		person.setName("vv");
		person.setFullName(new FullName("vv", "xiang"));
		ApplicationContext context = new ClassPathXmlApplicationContext(
				new String[] { "com/bow/benchmark/consumer.xml" });
		BenchmarkService restCal = context.getBean("bench", BenchmarkService.class);
		while (true) {
			System.in.read();
			Object a = restCal.echoObj(person);
			System.out.println(a);
		}
	}
}