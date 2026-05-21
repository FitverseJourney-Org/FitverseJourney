package org.fitverse.domain.repository.remote

interface MissionCatalogRepository {
    suspend fun getMissionCatalog(): Result<List<MissionTemplate>>
}

data class MissionTemplate(
    val id: String,
    val title: String,
    val description: String,
    val xpReward: Int,
    val type: String,       // matches MissionType.name (e.g. "STRETCH", "CARDIO")
    val isSpecial: Boolean, // true = CHALLENGE type, eligible for the 10% roll
)
