package org.fitverse.project.features.missions.services

import org.fitverse.project.features.missions.db.MissionRepository
import org.fitverse.project.features.missions.db.MissionSeedData
import org.fitverse.project.features.missions.models.MissionResponse

class MissionService(private val repository: MissionRepository) {

    suspend fun getMissions(): Result<List<MissionResponse>> = runCatching {
        repository.getAllMissions()
    }

    suspend fun seedMissions(): Result<Int> = runCatching {
        repository.seedMissions(MissionSeedData.missions)
    }
}
