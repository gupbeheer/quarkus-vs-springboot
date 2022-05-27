package org.demo.service

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer
import io.smallrye.reactive.messaging.annotations.Blocking
import org.eclipse.microprofile.reactive.messaging.Incoming
import java.io.Serializable
import javax.enterprise.context.ApplicationScoped
import javax.transaction.Transactional

@ApplicationScoped
class NewOrderListener(private val orderRepository: OrderRepository) {

    @Incoming("new-order-event")
    @Blocking
    @Transactional
    fun handleNewOrderEvent(newOrderEvent: NewOrderEvent) {
        orderRepository.persist(
            Order(
                productName = newOrderEvent.productName,
                amount = newOrderEvent.amount
            )
        )
    }
}

class NewOrderEvent(
    val productName: String,
    val amount: Int
) : Serializable

class NewOrderEventDeserializer: ObjectMapperDeserializer<NewOrderEvent>(NewOrderEvent::class.java)