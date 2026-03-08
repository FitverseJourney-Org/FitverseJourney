package org.fitverse.project

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

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
}