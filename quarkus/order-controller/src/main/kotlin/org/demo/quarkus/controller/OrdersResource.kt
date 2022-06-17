package org.demo.quarkus.controller

import org.jboss.logging.Logger
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
    private val log = Logger.getLogger(OrdersResource::class.java)

    @GET
    fun getAllOrders(@RestQuery page: Int): OrdersDTO {
        log.info("getAllOrders: page=$page")
        return orderRepository.getAllOrders(page)
    }

    @POST
    @ResponseStatus(RestResponse.StatusCode.CREATED)
    fun newOrder(@Valid @NotNull order: Order) {
        log.info("newOrder: order='$order'")
        orderRepository.save(order)
    }

}