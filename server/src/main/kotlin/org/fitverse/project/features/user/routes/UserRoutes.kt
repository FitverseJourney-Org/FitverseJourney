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
            userController.create(call)      // POST   /users
        }

        route("/{uid}") {
            get {
                userController.get(call)     // GET    /users/{uid}
            }
            put {
                userController.update(call)  // PUT    /users/{uid}
            }
            delete {
                userController.delete(call)  // DELETE /users/{uid}
            }
        }
    }
}