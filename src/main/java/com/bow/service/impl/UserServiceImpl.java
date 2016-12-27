package com.bow.service.impl;

import com.bow.entity.User;
import com.bow.service.UserService;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * @author vv
 * @since 2016/12/27.
 */
@Path("users")
public class UserServiceImpl implements UserService {


    @POST
    @Path("register")
    @Consumes({MediaType.APPLICATION_JSON})
    public void registerUser(User user) {
        System.out.println("save user");
    }

    @GET
    @Produces("application/json; charset=UTF-8")
    @Override
    public User getUser(@QueryParam("id") long id) {
        if(id == 1){
            User user = new User();
            user.setName("vv");
            return user;
        }
        return null;
    }
}
