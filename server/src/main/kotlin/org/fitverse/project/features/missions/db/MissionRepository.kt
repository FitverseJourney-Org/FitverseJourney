package org.fitverse.project.features.missions.db

import org.fitverse.project.features.missions.models.MissionDocument
import org.fitverse.project.features.missions.models.MissionResponse
import org.fitverse.project.plugins.FirestoreService

private const val COLLECTION_MISSIONS = "missions_collections"

class MissionRepository {

    suspend fun getAllMissions(): List<MissionResponse> =
        FirestoreService.getAll(COLLECTION_MISSIONS, MissionDocument::class.java)
            .getOrThrow()
            .map { (id, doc) ->
                MissionResponse(
                    id          = id,
                    title       = doc.title,
                    description = doc.description,
                    xpReward    = doc.xpReward,
                    type        = doc.type,
                    isSpecial   = doc.isSpecial,
                )
            }

    suspend fun seedMissions(missions: Map<String, MissionDocument>): Int {
        missions.forEach { (id, doc) ->
            FirestoreService.set(
                collection = COLLECTION_MISSIONS,
                documentId = id,
                data       = doc,
            ).getOrThrow()
        }
        return missions.size
    }
}
