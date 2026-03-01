package com.gareebi.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GareebiServerApplication

fun main(args: Array<String>) {
    runApplication<GareebiServerApplication>(*args)
}
