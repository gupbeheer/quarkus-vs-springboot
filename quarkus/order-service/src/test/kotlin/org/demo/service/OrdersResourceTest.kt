package org.demo.service

import io.quarkus.test.junit.QuarkusTest
import io.restassured.builder.RequestSpecBuilder
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.junit.jupiter.api.Test

@QuarkusTest
internal class OrdersResourceTest {

    @Test
    fun `should give all orders correctly paged`() {
        Given {
            spec(RequestSpecBuilder().setContentType(ContentType.JSON).build())
        } When {
            get("/orders?page=1")
        } Then {
            statusCode(200)
        }
    }
}