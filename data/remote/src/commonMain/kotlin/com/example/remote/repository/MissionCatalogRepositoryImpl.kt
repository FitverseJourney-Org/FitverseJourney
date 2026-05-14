package com.example.remote.repository

import com.example.domain.repository.remote.MissionCatalogRepository
import com.example.domain.repository.remote.MissionTemplate
import com.example.remote.datasource.missions.MissionCatalogRemoteDataSource

class MissionCatalogRepositoryImpl(
    private val dataSource: MissionCatalogRemoteDataSource,
) : MissionCatalogRepository {
    override suspend fun getMissionCatalog(): Result<List<MissionTemplate>> = runCatching {
        dataSource.fetchMissions()
    }
}
