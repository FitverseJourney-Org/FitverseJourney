package com.example.domain.repository.dbLocal.sqldelight.catalog

interface CatalogMissionDao {
    suspend fun getAll(): List<CatalogMissionRecord>
    suspend fun count(): Long
    suspend fun insertAll(missions: List<CatalogMissionRecord>)
}

data class CatalogMissionRecord(
    val id: String,
    val title: String,
    val description: String,
    val xpReward: Int,
    val type: String,
    val isSpecial: Boolean,
)
