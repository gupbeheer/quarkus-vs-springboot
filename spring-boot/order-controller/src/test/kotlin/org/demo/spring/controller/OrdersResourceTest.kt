package org.demo.spring.controller

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.hamcrest.Matchers.equalTo
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.stereotype.Component
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = [ "listeners=PLAINTEXT://localhost:9092", "port=9092" ])
@AutoConfigureMockMvc
class OrdersResourceTest {

    @Autowired
    private lateinit var kafkaConsumer: KafkaConsumer

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockkBean
    private lateinit var orderServiceRestClient: OrderServiceRestClient

    @Test
    fun `should get all orders`() {
        every { orderServiceRestClient.getAllOrders(1) } returns
            OrdersDTO(
                orders = listOf(Order(productName = "test", amount = 29)),
                pageNumber = 1,
                pageCount = 1
            )

        mockMvc.perform(get("/orders?page=1"))
            .andExpect {
                status().isOk
                content().contentType(MediaType.APPLICATION_JSON)
                jsonPath("$.pageNumber", equalTo(1))
                jsonPath("$.pageCount", equalTo(1))
                jsonPath("$.orders.size()", equalTo(1))
                jsonPath("$.orders[0].productNam", equalTo("test"))
                jsonPath("$.orders[0].amount", equalTo(29))
            }
    }

    @Test
    fun `should save a new order and send an event`() {
        mockMvc.perform(
            post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"productName\": \"My test\", \"amount\": 12}")
        ).andExpect {
            status().isCreated
        }

        val messageConsumed = kafkaConsumer.getLatch().await(5, TimeUnit.SECONDS)
        assertTrue(messageConsumed)
        assertEquals("{\"productName\":\"My test\",\"amount\":12}", kafkaConsumer.getPayload())
    }

    @Test
    fun `should give a bad request if the order is not valid for saving`() {
        mockMvc.perform(
            post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect {
            status().isBadRequest
        }
    }

}

@Component
class KafkaConsumer {
    private var latch = CountDownLatch(1)
    private var payload: String? = null

    @KafkaListener(topics = [NEW_ORDER_EVENT_TOPIC], groupId = "test-group", clientIdPrefix = "test")
    fun receive(consumerRecord: ConsumerRecord<*, *>) {
        payload = consumerRecord.value().toString()
        latch.countDown()
    }

    fun getLatch() = latch

    fun getPayload() = payload
}