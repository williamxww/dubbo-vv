package com.bow.feature;

import com.bow.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * @author vv
 * @since 2016/12/12.
 */
public class Consumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

    private ApplicationContext context;

    @Before
    public void setup() {
        System.setProperty("dubbo.application.logger", "slf4j");
        context = new ClassPathXmlApplicationContext(new String[] { "com/bow/rest/consumer.xml" });
    }

    @Test
    public void async() throws Exception {
        UserService service = context.getBean( UserService.class);
        service.getUser(1L);
    }

}
