package com.terning.server.kotlin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ServerKotlinApplication

fun main(args: Array<String>) {
    runApplication<ServerKotlinApplication>(*args)
}
