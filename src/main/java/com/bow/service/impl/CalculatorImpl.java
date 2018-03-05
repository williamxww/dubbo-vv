package com.bow.service.impl;


import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.bow.service.Calculator;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author vv
 * @since 2016/12/12.
 */
@Path("cal")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class CalculatorImpl implements Calculator {


    public int calculate(int a, int b) {
        return (a+b)*2;
    }

    @GET
    @Path("{id : \\d+}")
    @Override
    public int print(@PathParam("id")int id) {
        return id;
    }


}
