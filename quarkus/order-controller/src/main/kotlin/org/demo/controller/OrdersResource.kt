package org.demo.controller

import org.jboss.resteasy.reactive.ResponseStatus
import org.jboss.resteasy.reactive.RestQuery
import org.jboss.resteasy.reactive.RestResponse
import javax.validation.Valid
import javax.validation.constraints.NotNull
import javax.ws.rs.GET
import javax.ws.rs.POST
import javax.ws.rs.Path

@Path("/orders")
class OrdersResource(private val orderRepository: OrderRepository) {

    @GET
    fun getAllOrders(@RestQuery page: Int) = orderRepository.getAllOrders(page)

    @POST
    @ResponseStatus(RestResponse.StatusCode.CREATED)
    fun newOrder(@Valid @NotNull order: Order) {
        orderRepository.save(order)
    }

}