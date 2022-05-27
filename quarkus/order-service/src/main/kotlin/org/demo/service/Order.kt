package org.demo.service

import io.quarkus.hibernate.orm.panache.PanacheEntity
import javax.persistence.Entity

@Entity(name = "orders")
class Order(
    val productName: String,
    val amount: Int
) : PanacheEntity()