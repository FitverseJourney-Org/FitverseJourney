package org.fitverse.project.features.missions.controller

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.ApplicationCall
import org.fitverse.project.features.missions.models.MissionResponse
import org.fitverse.project.features.missions.services.MissionService
import org.fitverse.project.plugins.respondError
import org.fitverse.project.plugins.respondSuccess

class MissionController(private val service: MissionService) {

    suspend fun getAll(call: ApplicationCall) {
        val result = service.getMissions()
        if (result.isSuccess) {
            call.respondSuccess<List<MissionResponse>>(data = result.getOrNull())
        } else {
            call.respondError(
                code    = "MISSIONS_ERROR",
                message = result.exceptionOrNull()?.message ?: "Erro ao buscar missões",
            )
        }
    }

    suspend fun seed(call: ApplicationCall) {
        val result = service.seedMissions()
        if (result.isSuccess) {
            call.respondSuccess<Unit>(
                message = "${result.getOrNull()} missões inseridas na coleção missions_collections.",
                status  = HttpStatusCode.Created,
            )
        } else {
            call.respondError(
                code    = "SEED_ERROR",
                message = result.exceptionOrNull()?.message ?: "Erro ao popular missões",
            )
        }
    }
}
