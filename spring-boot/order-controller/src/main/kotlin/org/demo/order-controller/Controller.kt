package org.demo.`order-controller`

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller {
    @GetMapping
    fun hello(): String {
        return "Hello from Spring-boot, order-controller"
    }
}
