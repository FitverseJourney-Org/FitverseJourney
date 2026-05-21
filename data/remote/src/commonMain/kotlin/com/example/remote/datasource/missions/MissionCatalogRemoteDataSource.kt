package org.fitverse.data.remote.datasource.missions

import org.fitverse.domain.repository.remote.MissionTemplate

interface MissionCatalogRemoteDataSource {
    suspend fun fetchMissions(): List<MissionTemplate>
}
