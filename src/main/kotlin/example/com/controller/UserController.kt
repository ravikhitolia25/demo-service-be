package example.com.controller

import example.com.service.UserService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.demoController(userService: UserService) {
    get("/users") {
        val id = call.request.queryParameters["id"]?.toIntOrNull()
        val combinedData = userService.getUserData(id)
        call.respond(HttpStatusCode.OK, combinedData)
    }
}