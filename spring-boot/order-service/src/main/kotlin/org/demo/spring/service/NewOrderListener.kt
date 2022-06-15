package org.demo.spring.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.common.serialization.Deserializer
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import java.io.Serializable
import javax.transaction.Transactional

const val NEW_ORDER_EVENT_TOPIC = "new-order-event-spring"

@Component
class NewOrderListener(private val orderRepository: OrderRepository) {

    @Transactional
    @KafkaListener(topics = [NEW_ORDER_EVENT_TOPIC])
    fun handleNewOrderEvent(@Payload newOrderEvent: NewOrderEvent) {
        orderRepository.save(
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

class NewOrderEventDeserializer: Deserializer<NewOrderEvent?> {
    override fun deserialize(topic: String?, data: ByteArray?): NewOrderEvent? {
        if (data == null) {
            return null
        }
        return ObjectMapper()
            .findAndRegisterModules()
            .readValue(data, NewOrderEvent::class.java)
    }
}