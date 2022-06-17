package org.demo.quarkus.service

import io.quarkus.hibernate.orm.panache.PanacheEntity
import javax.persistence.Entity

@Entity(name = "orders_quarkus")
data class Order(
    val productName: String,
    val amount: Int
) : PanacheEntity()