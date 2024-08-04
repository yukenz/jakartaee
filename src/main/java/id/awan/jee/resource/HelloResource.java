package id.awan.jee.resource;

import id.awan.jee.interceptor.annotation.SafeHttpInvoke;
import id.awan.jee.service.UserService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.HashMap;

@Path("/hello-world")
@SafeHttpInvoke
public class HelloResource {

    @Inject
    UserService userService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public HashMap<String, Object> hello() {

        HashMap<String, Object> map = new HashMap<>();
        map.put("message", "Hello World");


        throw new RuntimeException("lol exxss");

//        return map;
    }
}