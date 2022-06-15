package org.demo.spring.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/orders")
class OrdersResource(private val orderRepository: OrderRepository) {

    @GetMapping
    fun getAllOrders(@RequestParam page: Int) = orderRepository.getAllOrders(page)

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun newOrder(@RequestBody @Valid @NotNull order: Order) {
        orderRepository.save(order)
    }

}