package org.fitverse.project.features.missions.routes

import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import org.fitverse.project.features.missions.controller.MissionController
import org.koin.ktor.ext.inject

fun Route.missionRouting() {
    val controller by inject<MissionController>()

    route("/missions") {
        get  { controller.getAll(call) }
        post("/seed") { controller.seed(call) }
    }
}
