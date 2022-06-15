package org.demo.spring.service

import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class OrdersResource(private val orderRepository: OrderRepository) {

    private companion object {
        const val PAGE_SIZE = 25
    }

    @GetMapping("/orders")
    fun getAllOrders(@RequestParam page: Int): OrdersDTO {
        val pagedOrders = orderRepository.findAll(Pageable.ofSize(PAGE_SIZE).withPage(page))
        return OrdersDTO(
            orders = pagedOrders.content,
            pageNumber = page,
            pageCount = pagedOrders.totalPages
        )
    }
}

class OrdersDTO(
    val orders: List<Order>,
    val pageNumber: Int,
    val pageCount: Int
)