package org.demo.controller

import javax.ws.rs.GET
import javax.ws.rs.Path

@Path("/hello")
class HelloResource {

    @GET
    fun getAllOrders() = "Hello from Quarkus, order-controller"

}