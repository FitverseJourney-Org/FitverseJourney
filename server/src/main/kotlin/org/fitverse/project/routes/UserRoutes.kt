package org.fitverse.project.routes

import com.google.firebase.auth.FirebaseAuth
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.testAuthRoute() {

    get("/test-auth") {

        val authHeader = call.request.headers["Authorization"]
            ?: return@get call.respondText("Missing token", status = HttpStatusCode.Unauthorized)

        val token = authHeader.removePrefix("Bearer ").trim()

        try {
            val decodedToken = FirebaseAuth.getInstance().verifyIdToken(token)
            val uid = decodedToken.uid

            call.respondText("Token válido. UID: $uid")

        } catch (e: Exception) {
            call.respondText("Token inválido", status = HttpStatusCode.Unauthorized)
        }
    }
}
