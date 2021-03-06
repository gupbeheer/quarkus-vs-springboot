package org.demo.spring.service

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity(name = "orders_spring_boot")
data class Order(
    val productName: String,
    val amount: Int
) {
    @Suppress("unused") // just for database internal use
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}