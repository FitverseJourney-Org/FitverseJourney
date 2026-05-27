package org.fitverse.data.remote.datasource.missions

import org.fitverse.domain.repository.remote.MissionTemplate
import org.fitverse.data.remote.dto.missions.MissionDto
import org.fitverse.data.remote.util.ApiConstants
import org.fitverse.data.remote.util.ApiResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.url

class MissionCatalogRemoteDataSourceImpl(
    private val httpClient: HttpClient,
) : MissionCatalogRemoteDataSource {

    override suspend fun fetchMissions(): List<MissionTemplate> =
        httpClient.get {
            url("${ApiConstants.BASE_URL}${ApiConstants.Endpoints.MISSIONS}")
        }
        .body<ApiResponse<List<MissionDto>>>()
        .data
        .map { dto ->
            MissionTemplate(
                id          = dto.id,
                title       = dto.title,
                description = dto.description,
                xpReward    = dto.xpReward,
                type        = dto.type,
                isSpecial   = dto.isSpecial,
            )
        }
}
