package com.bow.rest;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;

public class RestProvider {

    public static void main(String[] args) throws Exception {
        String config = RestProvider.class.getPackage().getName().replace('.', '/') + "/rest-provider.xml";
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(config);
        context.start();
        System.in.read();
    }

    @Configuration
    @EnableDubbo(scanBasePackages = "com.bow.rest.impl.facade")
    static public class ProviderConfiguration {

    }

}