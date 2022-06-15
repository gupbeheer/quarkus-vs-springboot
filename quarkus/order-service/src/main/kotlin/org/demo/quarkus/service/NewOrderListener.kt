package org.demo.quarkus.service

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer
import io.smallrye.reactive.messaging.annotations.Blocking
import org.apache.logging.log4j.LogManager
import org.eclipse.microprofile.reactive.messaging.Incoming
import java.io.Serializable
import javax.enterprise.context.ApplicationScoped
import javax.transaction.Transactional

const val NEW_ORDER_EVENT_TOPIC = "new-order-event-quarkus"

@ApplicationScoped
class NewOrderListener(private val orderRepository: OrderRepository) {
    private val log = LogManager.getLogger()

    @Incoming(NEW_ORDER_EVENT_TOPIC)
    @Blocking
    @Transactional
    fun handleNewOrderEvent(newOrderEvent: NewOrderEvent) {
        log.info("handleNewOrderEvent: newOrderEvent='$newOrderEvent'")

        orderRepository.persist(
            Order(
                productName = newOrderEvent.productName,
                amount = newOrderEvent.amount
            )
        )
    }
}

data class NewOrderEvent(
    val productName: String,
    val amount: Int
) : Serializable

class NewOrderEventDeserializer: ObjectMapperDeserializer<NewOrderEvent>(NewOrderEvent::class.java)