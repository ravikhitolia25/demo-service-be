package example.com

import example.com.config.MongoConfig
import example.com.controller.demoController
import example.com.repository.UserRepository
import example.com.service.UserService
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.litote.kmongo.json

fun main() {
    embeddedServer(Netty, port = 8080, host = "localhost") {
        module()
    }.start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }

    install(Routing) {
        demoController(UserService(UserRepository(MongoConfig.db)))
    }
}
