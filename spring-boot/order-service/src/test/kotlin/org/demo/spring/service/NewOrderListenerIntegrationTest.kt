package org.demo.spring.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.awaitility.kotlin.atMost
import org.awaitility.kotlin.await
import org.awaitility.kotlin.until
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import java.time.Duration


@ActiveProfiles("tst")
@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = [ "listeners=PLAINTEXT://localhost:9092", "port=9092" ])
internal class NewOrderListenerIntegrationTest {

    @Autowired
    private lateinit var kafkaTemplate: KafkaTemplate<String?, String?>

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var orderRepository: OrderRepository

    @Test
    fun `should receive and store new order events`() {
        val newOrderEvent = NewOrderEvent("Milk", 5)
        kafkaTemplate.send(NEW_ORDER_EVENT_TOPIC, objectMapper.writeValueAsString(newOrderEvent))

        await atMost Duration.ofSeconds(10) until {
            orderRepository.findAll().find { it.productName == "Milk" } != null
        }
    }
}