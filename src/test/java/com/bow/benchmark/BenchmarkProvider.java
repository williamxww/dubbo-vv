/*
 *  Copyright 2009-2016 Weibo, Inc.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.bow.benchmark;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BenchmarkProvider {

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("dubbo.application.logger", "slf4j");
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                new String[]{"com/bow/benchmark/provider.xml"});

        System.out.println("server running---");
        Thread.sleep(Long.MAX_VALUE);
    }

}
