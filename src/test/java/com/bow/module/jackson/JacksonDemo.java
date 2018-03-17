package com.bow.module.jackson;

import com.bow.entity.Person;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.impl.Utf8Generator;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;

/**
 * @author wwxiang
 * @since 2018/3/13.
 */
public class JacksonDemo {


    @Test
    public void test() throws IOException {
        Person p = new Person();
        p.setName("vv");
        p.setPhoneNumber(Collections.singletonList("132465798"));

        JsonFactory factory = new JsonFactory();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        JsonGenerator generator = factory.createJsonGenerator(bos, JsonEncoding.UTF8);

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(generator, p);

        byte[] ary = bos.toByteArray();
        System.out.println(ary.length);
    }
}
