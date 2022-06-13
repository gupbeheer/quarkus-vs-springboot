package org.demo.controller

import javax.validation.constraints.NotNull

data class Order(
    @NotNull
    val productName: String,
    val amount: Int
)