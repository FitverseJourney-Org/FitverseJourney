package org.fitverse.domain.repository.dbLocal.sqldelight.configurations

interface ConfigOnboardingDataSourceDao {
    suspend fun getOnboardingCompleted(): Boolean
    suspend fun setOnboardingCompleted(completed: Boolean)
}