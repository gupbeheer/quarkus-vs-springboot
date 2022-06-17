package org.demo.spring.controller

data class OrdersDTO(
    val orders: List<Order>,
    val pageNumber: Int,
    val pageCount: Int
)