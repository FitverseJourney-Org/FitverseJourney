package org.fitverse.project.features.user.routes

import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import org.fitverse.project.features.user.controller.UserController
import org.koin.ktor.ext.inject

fun Route.userRouting() {
    val userController by inject<UserController>()

    route("/users") {
        post {
            userController.create(call)
        }

        route("/{uid}") {
            get {
                userController.get(call)
            }
            put {
                userController.update(call)
            }
            delete {
                userController.delete(call)
            }
        }
    }
}