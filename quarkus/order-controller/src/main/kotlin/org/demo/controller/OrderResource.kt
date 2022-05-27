package org.demo.controller

import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path

@Path("/orders")
class OrderResource {

    @GET
    fun getAllOrders() = emptyList<Order>()

    @POST
    fun newOrder(order: Order) {

    }

}