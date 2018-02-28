package com.bow.common;

import com.bow.service.Calculator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.TimeUnit;

/**
 * @author vv
 * @since 2016/12/12.
 */
public class Provider {

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"com/bow/common/provider.xml"});
        System.out.println("started");
        while(true){
            Calculator c = context.getBean("c1",Calculator.class);
            int r = c.calculate(1,2);
            System.out.println(r);
            TimeUnit.SECONDS.sleep(2);
        }
    }
}
