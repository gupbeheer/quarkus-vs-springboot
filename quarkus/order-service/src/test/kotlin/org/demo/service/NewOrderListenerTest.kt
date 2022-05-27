package org.demo.service

import com.fasterxml.jackson.databind.ObjectMapper
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.kafka.InjectKafkaCompanion
import io.quarkus.test.kafka.KafkaCompanionResource
import io.smallrye.reactive.messaging.kafka.companion.KafkaCompanion
import org.apache.kafka.clients.producer.ProducerRecord
import org.awaitility.Awaitility
import org.awaitility.kotlin.atMost
import org.awaitility.kotlin.await
import org.awaitility.kotlin.until
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.transaction.Transactional


@QuarkusTest
@QuarkusTestResource(KafkaCompanionResource::class)
@Transactional
internal class NewOrderListenerTest {

    @InjectKafkaCompanion
    lateinit var companion: KafkaCompanion

    @Inject
    lateinit var objectMapper: ObjectMapper

    @Inject
    lateinit var orderRepository: OrderRepository

    @BeforeEach
    fun setupAwaitForPanacheCompatibility() {
        Awaitility.pollInSameThread()
    }

    @Test
    fun `should receive and store new order events`() {
        companion.produceStrings().usingGenerator( { _: Int? ->
            ProducerRecord(
                "new-order-event",
                objectMapper.writeValueAsString(NewOrderEvent("Test product", 12))
            )
        }, 12)
        await atMost Duration.ofSeconds(2) until {
            orderRepository.count() == 12L
        }
    }
}