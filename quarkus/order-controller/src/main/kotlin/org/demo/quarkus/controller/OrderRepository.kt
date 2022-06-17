package org.demo.quarkus.controller

import org.eclipse.microprofile.reactive.messaging.Channel
import org.eclipse.microprofile.reactive.messaging.Emitter
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.jboss.resteasy.reactive.RestQuery
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.GET
import javax.ws.rs.Path

const val NEW_ORDER_EVENT_TOPIC = "new-order-event-quarkus"

@ApplicationScoped
class OrderRepository(
    @Channel(NEW_ORDER_EVENT_TOPIC)
    private val orderEmitter: Emitter<Order>,
    @RestClient
    private val orderService: OrderServiceRestClient
) {
    fun getAllOrders(page: Int) = orderService.getAllOrders(page)
    fun save(order: Order) {
        orderEmitter.send(order).toCompletableFuture().join()
    }
}

@ApplicationScoped
@Path("/orders")
@RegisterRestClient(configKey = "order-service-api")
interface OrderServiceRestClient {
    @GET
    fun getAllOrders(@RestQuery page: Int): OrdersDTO
}


