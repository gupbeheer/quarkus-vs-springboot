package org.demo.controller

class OrdersDTO(
    val orders: List<Order>,
    val pageNumber: Int,
    val pageCount: Int
)