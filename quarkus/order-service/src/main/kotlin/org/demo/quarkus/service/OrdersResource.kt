package org.demo.quarkus.service

import org.jboss.resteasy.reactive.RestQuery
import javax.ws.rs.GET
import javax.ws.rs.Path

@Path("/orders")
class OrdersResource(private val orderRepository: OrderRepository) {

    private companion object {
        const val PAGE_SIZE = 25
    }

    @GET
    fun getAllOrders(@RestQuery page: Int): OrdersDTO {
        val pagedOrders = orderRepository.findAll().page<Order>(page, PAGE_SIZE)
        return OrdersDTO(
            orders = pagedOrders.list(),
            pageNumber = page,
            pageCount = pagedOrders.pageCount()
        )
    }
}

class OrdersDTO(
    val orders: List<Order>,
    val pageNumber: Int,
    val pageCount: Int
)