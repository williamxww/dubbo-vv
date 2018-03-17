package com.bow.registry;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.dubbo.qos.common.Constants;

/**
 * @author vv
 * @since 2016/12/12.
 */
public class RegistryProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(RegistryProvider.class);

    public static void main(String[] args) throws Exception {
        System.setProperty(Constants.QOS_PORT, "22220");
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"com/bow/registry/provider.xml"});
        LOGGER.info("Provider started.");
        TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }
}
