package org.demo.spring.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.kafka.common.serialization.Serializer
import org.slf4j.LoggerFactory
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
class OrderServiceRestClient(private val restTemplate: RestTemplate, @Value("\${order-service.url}") private val orderServiceUrl: String) {
    val log = LoggerFactory.getLogger(OrderServiceRestClient::class.java)

    fun getAllOrders(page: Int): OrdersDTO? {
        log.info("getAllOrders: page=$page, url='$orderServiceUrl'")
        return restTemplate.getForObject(orderServiceUrl, OrdersDTO::class.java, mapOf("page" to page))
    }
}

@Suppress("unused") // used in config
class OrderSerializer(): Serializer<Order> {
    override fun serialize(topic: String?, data: Order?): ByteArray {
        val writer = ObjectMapper().findAndRegisterModules().writerFor(Order::class.java)
        return writer.writeValueAsBytes(data)
    }
}
