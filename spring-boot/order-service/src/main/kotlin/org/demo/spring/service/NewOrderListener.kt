package org.demo.spring.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.serializer.JsonDeserializer
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import java.io.Serializable
import javax.transaction.Transactional

const val NEW_ORDER_EVENT_TOPIC = "new-order-event-spring"

@Component
class NewOrderListener(private val orderRepository: OrderRepository) {
    val log : Logger = LoggerFactory.getLogger(NewOrderListener::class.java)

    @Transactional
    @KafkaListener(topics = [NEW_ORDER_EVENT_TOPIC])
    fun handleNewOrderEvent(@Payload newOrderEvent: NewOrderEvent) {
        log.info("handleNewOrderEvent: newOrderEvent='$newOrderEvent'")

        orderRepository.save(
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

@Suppress("unused") // used in config
class NewOrderEventDeserializer: JsonDeserializer<NewOrderEvent>(NewOrderEvent::class.java, ObjectMapper().findAndRegisterModules(), false)