package com.example.remote.datasource.missions

import com.example.domain.repository.remote.MissionTemplate

interface MissionCatalogRemoteDataSource {
    suspend fun fetchMissions(): List<MissionTemplate>
}
