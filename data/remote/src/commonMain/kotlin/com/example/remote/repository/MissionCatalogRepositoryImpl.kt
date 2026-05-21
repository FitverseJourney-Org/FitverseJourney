package org.fitverse.data.remote.repository

import org.fitverse.domain.repository.remote.MissionCatalogRepository
import org.fitverse.domain.repository.remote.MissionTemplate
import org.fitverse.data.remote.datasource.missions.MissionCatalogRemoteDataSource

class MissionCatalogRepositoryImpl(
    private val dataSource: MissionCatalogRemoteDataSource,
) : MissionCatalogRepository {
    override suspend fun getMissionCatalog(): Result<List<MissionTemplate>> = runCatching {
        dataSource.fetchMissions()
    }
}
