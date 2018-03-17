package com.bow.http.demo;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * @author wwxiang
 * @since 2018/3/6.
 */
@Path("printer")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
public class Printer {

    @GET
    @Path("{id : \\d+}")
    public int print(@PathParam("id") int id){
        return id;
    }
}
