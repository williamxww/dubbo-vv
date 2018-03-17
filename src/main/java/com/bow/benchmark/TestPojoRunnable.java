package com.bow.benchmark;

import com.bow.entity.FullName;
import com.bow.entity.Person;
import com.bow.service.BenchmarkService;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class TestPojoRunnable extends AbstractClientRunnable {
    Person person = new Person();

    public TestPojoRunnable(BenchmarkService service, String params, CyclicBarrier barrier, CountDownLatch latch, long startTime, long endTime) {
        super(service, barrier, latch, startTime, endTime);
        person.setName("motan");
        person.setFullName(new FullName("first", "last"));
        person.setBirthday(new Date());
        List<String> phoneNumber = new ArrayList<String>();
        phoneNumber.add("123");
        person.setPhoneNumber(phoneNumber);
        person.setEmail(phoneNumber);
        Map<String, String> address = new HashMap<String, String>();
        address.put("hat", "123");
        person.setAddress(address);
    }

    @Override
    protected Object call(BenchmarkService benchmarkService) {
        Object result = benchmarkService.echoObj(person);
        return result;
    }
}
