package org.fitverse.project.routes

import com.google.firebase.auth.FirebaseAuth
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.testAuthRoute() {
    get("/home") {
        call.respondText("home")
    }
}
