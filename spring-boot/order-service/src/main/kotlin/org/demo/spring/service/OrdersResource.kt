package org.demo.spring.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class OrdersResource(private val orderRepository: OrderRepository) {
    val log: Logger = LoggerFactory.getLogger(OrdersResource::class.java)

    private companion object {
        const val PAGE_SIZE = 25
    }

    @GetMapping("/orders")
    fun getAllOrders(@RequestParam(defaultValue = "0") page: Int): OrdersDTO {
        log.info("getAllOrders: page=$page")

        val pagedOrders = orderRepository.findAll(Pageable.ofSize(PAGE_SIZE).withPage(page))
        return OrdersDTO(
            orders = pagedOrders.content,
            pageNumber = page,
            pageCount = pagedOrders.totalPages
        )
    }
}

data class OrdersDTO(
    val orders: List<Order>,
    val pageNumber: Int,
    val pageCount: Int
)
