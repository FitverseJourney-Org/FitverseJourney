package org.fitverse.project

import io.ktor.server.application.Application
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import org.fitverse.project.plugins.configurePlugins

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
    configurePlugins()
    initFirebase()
    routing()
}

fun Application.routing() {
    routing {
        get("/") {
            call.respondText("Ktor: Hello, world!}")
        }
        get("/home") {
            call.respondText("Ktor: My World")
        }
    }
}
