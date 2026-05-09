package org.fitverse.project

import io.ktor.server.application.Application
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.routing.routing
import org.fitverse.project.features.user.routes.userRouting
import org.fitverse.project.plugins.configurePlugins

fun main() {
    embeddedServer(
        factory = Netty,
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
    configurePlugins()
    routing()
}

fun Application.routing() {
    routing {
        userRouting()
    }
}
