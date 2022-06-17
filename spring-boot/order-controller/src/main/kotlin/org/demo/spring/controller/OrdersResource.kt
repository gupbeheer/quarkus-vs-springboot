package org.demo.spring.controller

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/orders")
class OrdersResource(private val orderRepository: OrderRepository) {
    val log : Logger = LoggerFactory.getLogger(OrdersResource::class.java)

    @GetMapping
    fun getAllOrders(@RequestParam(defaultValue = "0") page: Int): OrdersDTO? {
        log.info("getAllOrders: page=$page")
        return orderRepository.getAllOrders(page)
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun newOrder(@RequestBody @Valid @NotNull order: Order) {
        log.info("newOrder: order='$order'")
        orderRepository.save(order)
    }

}