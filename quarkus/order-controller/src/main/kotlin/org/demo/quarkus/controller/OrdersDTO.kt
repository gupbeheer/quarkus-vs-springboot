package org.demo.quarkus.controller

data class OrdersDTO(
    val orders: List<Order>,
    val pageNumber: Int,
    val pageCount: Int
)