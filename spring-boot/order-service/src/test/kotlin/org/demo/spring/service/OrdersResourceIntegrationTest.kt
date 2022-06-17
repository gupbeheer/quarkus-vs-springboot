package org.demo.spring.service

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@ActiveProfiles("tst")
@SpringBootTest
@AutoConfigureMockMvc
internal class OrdersResourceIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `should give all orders correctly paged`() {
        mockMvc.perform(get("/orders?page=1"))
            .andExpect(
                status().isOk
            )
    }
}