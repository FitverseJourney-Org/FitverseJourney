package org.fitverse.project

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.fitverse.project.routes.testAuthRoute

fun main() {
    embeddedServer(
        Netty,
        configure = {
            connector {
                host = "0.0.0.0"
                port = 8080
            }
        }
    ) {
        module()
    }.start(wait = true)
}

fun Application.module() {
    initFirebase()
    routing {
        get("/") {
            call.respondText("Hello my Friend !")
        }
        post("/login") {

        }
        testAuthRoute()

    }
}