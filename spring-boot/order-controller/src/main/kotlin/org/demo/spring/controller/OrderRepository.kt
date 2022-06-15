package org.demo.spring.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.common.serialization.Serializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

const val NEW_ORDER_EVENT_TOPIC = "new-order-event-spring"

@Component
class OrderRepository(
    private val orderEmitter: KafkaTemplate<String?, Order>,
    private val orderService: OrderServiceRestClient
) {
    fun getAllOrders(page: Int) = orderService.getAllOrders(page)
    fun save(order: Order) {
        orderEmitter.send(NEW_ORDER_EVENT_TOPIC, order).get()
    }
}

@Component
class OrderServiceRestClient(private val restTemplate: RestTemplate, @Value("order-service.url") private val orderServiceUrl: String) {
    fun getAllOrders(page: Int): OrdersDTO? =
        restTemplate.getForObject(orderServiceUrl, OrdersDTO::class.java, mapOf("page" to page))
}

class OrderSerializer(): Serializer<Order> {
    override fun serialize(topic: String?, data: Order?): ByteArray {
        val writer = ObjectMapper().findAndRegisterModules().writerFor(Order::class.java)
        return writer.writeValueAsBytes(data)
    }
}
