package com.bow.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;

/**
 * @author vv
 * @since 2016/12/12.
 */
@Path("cal")
@Consumes({ MediaType.APPLICATION_JSON, MediaType.TEXT_XML })
@Produces({ ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8 })
public interface Calculator {

	@GET
	int calculate(@QueryParam("a") int a, @QueryParam("b") int b);

	@Path("add")
	@GET
	int add(@QueryParam("a") int a, @QueryParam("b") int b);

    @Path("sub")
	@GET
	int sub(@QueryParam("a") int a, @QueryParam("b") int b);
}
