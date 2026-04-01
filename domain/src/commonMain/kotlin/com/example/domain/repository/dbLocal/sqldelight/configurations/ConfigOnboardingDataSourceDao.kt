package com.example.domain.repository.dbLocal.sqldelight.configurations

interface ConfigOnboardingDataSourceDao {
    suspend fun getOnboardingCompleted(): Boolean
    suspend fun setOnboardingCompleted(completed: Boolean)
}