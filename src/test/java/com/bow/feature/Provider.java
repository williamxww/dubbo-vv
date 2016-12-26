package com.bow.feature;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author vv
 * @since 2016/12/12.
 */
public class Provider {

    @Test
    public void async() throws IOException {
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"com/bow/feature/provider.xml"});
        System.in.read();
    }
}
