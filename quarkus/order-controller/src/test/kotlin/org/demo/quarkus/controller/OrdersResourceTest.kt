package org.demo.quarkus.controller

import io.mockk.every
import io.quarkiverse.test.junit.mockk.InjectMock
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.kafka.InjectKafkaCompanion
import io.quarkus.test.kafka.KafkaCompanionResource
import io.restassured.builder.RequestSpecBuilder
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.smallrye.reactive.messaging.kafka.companion.KafkaCompanion
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@QuarkusTest
@QuarkusTestResource(KafkaCompanionResource::class)
class OrdersResourceTest {

    @InjectKafkaCompanion
    private lateinit var companion: KafkaCompanion

    @InjectMock
    @RestClient
    private lateinit var orderServiceRestClient: OrderServiceRestClient

    @Test
    fun `should get all orders`() {
        every { orderServiceRestClient.getAllOrders(1) } returns
            OrdersDTO(
                orders = listOf(Order(productName = "test", amount = 29)),
                pageNumber = 1,
                pageCount = 1
            )

        Given {
            spec(RequestSpecBuilder().setContentType(ContentType.JSON).build())
            queryParam("page", 1)
        } When {
            get("/orders")
        } Then {
            log().ifValidationFails()
            statusCode(200)
            contentType(ContentType.JSON)
            body("pageNumber", equalTo(1))
            body("pageCount", equalTo(1))
            body("orders.size()", equalTo(1))
            body("orders[0].productName", equalTo("test"))
            body("orders[0].amount", equalTo(29))
        }
    }

    @Test
    fun `should save a new order and send an event`() {
        Given {
            spec(RequestSpecBuilder().setContentType(ContentType.JSON).build())
            body("{\"productName\": \"My test\", \"amount\": 12}")
        } When {
            post("/orders")
        } Then {
            log().ifValidationFails()
            statusCode(201)
        }
        val consumed = companion.consumeStrings().fromTopics(NEW_ORDER_EVENT_TOPIC, 1)
        consumed.awaitCompletion()
        Assertions.assertEquals("{\"productName\":\"My test\",\"amount\":12}", consumed.first().value())
    }

    @Test
    fun `should give a bad request if the order is not valid for saving`() {
        Given {
            spec(RequestSpecBuilder().setContentType(ContentType.JSON).build())
        } When {
            post("/orders")
        } Then {
            statusCode(400)
        }
    }

}