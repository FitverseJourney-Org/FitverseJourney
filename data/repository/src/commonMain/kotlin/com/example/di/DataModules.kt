package com.example.di

import com.example.domain.models.local.User
import com.example.domain.repository.ActivatePlanRepository
import com.example.domain.repository.ExerciseRepository
import com.example.domain.repository.FakeFriendsRepository
import com.example.domain.repository.FriendsRepository
import com.example.domain.repository.ProgressionRepository
import com.example.domain.repository.dbLocal.datastore.AppAuthenticateRepository
import com.example.domain.repository.dbLocal.datastore.AppLanguageRepository
import com.example.domain.repository.dbLocal.datastore.AppOnboardingRepository
import com.example.domain.repository.dbLocal.sqldelight.configurations.ConfigTokenDataSourceDao
import com.example.domain.repository.dbLocal.sqldelight.user.UserRepository
import com.example.domain.repository.wiki.WikiRepository
import com.example.local.database.datastore.repository.AppAuthenticateRepositoryImpl
import com.example.local.database.datastore.repository.AppLanguageRepositoryImpl
import com.example.local.database.datastore.repository.AppOnboardingRepositoryImpl
import com.example.local.database.sqldelight.repository.configuration.ConfigLanguageDataSourceDao
import com.example.local.database.sqldelight.repository.configuration.ConfigOnboardingDataSourceDao
import com.example.local.database.sqldelight.repository.configuration.ConfigTokenDaoDataSourceImpl
import com.example.local.datasource.exercises.ExerciseLocalDataSource
import com.example.local.datasource.exercises.ExerciseLocalDataSourceImpl
import com.example.local.datasource.progression.ProgressionLocalDataSource
import com.example.local.datasource.progression.ProgressionLocalDataSourceImpl
import com.example.local.datasource.user.UserLocalDataSource
import com.example.local.datasource.user.UserLocalDataSourceImpl
import com.example.local.di.databaseModule
import com.example.local.mapper.EntityMapper
import com.example.remote.util.networkModule
import com.example.local.mapper.user.UserEntityMapper
import com.example.remote.datasource.activePlan.ActivatePlanRemoteDataSource
import com.example.remote.datasource.activePlan.ActivatePlanRemoteDataSourceImpl
import com.example.remote.datasource.friends.FriendsRemoteDataSourceImpl
import com.example.remote.datasource.progression.ProgressionRemoteDataSource
import com.example.remote.datasource.progression.ProgressionRemoteDataSourceImpl
import com.example.remote.datasource.user.UserRemoteDataSource
import com.example.remote.datasource.user.UserRemoteDataSourceImpl
import com.example.remote.dto.user.UserRequestDto
import com.example.remote.mapper.DtoMapper
import com.example.remote.mapper.user.UserDtoMapper
import com.example.repository.UserRepositoryImpl
import com.example.repository.WikiRepositoryImpl
import com.example.repository.activePlan.ActivatePlanRepositoryImpl
import com.example.repository.exercise.ExerciseRepositoryImpl
import com.example.repository.progression.ProgressionRepositoryImpl
import com.journey.database.migrations.UserEntity
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

// ──────────────────────────────────────────────────────────────────────────────
// ── DataStore ─────────────────────────────────────────────────────────────────
// ──────────────────────────────────────────────────────────────────────────────
val dataStoreModule = module {
    single { ConfigLanguageDataSourceDao(databaseSqlDeLightHelper = get()) }
    single { ConfigOnboardingDataSourceDao(databaseSqlDeLightHelper = get()) }
    single<ConfigTokenDataSourceDao> { ConfigTokenDaoDataSourceImpl(databaseSqlDeLightHelper = get()) }
}
// ──────────────────────────────────────────────────────────────────────────────
// ── Data Sources ──────────────────────────────────────────────────────────────
// ──────────────────────────────────────────────────────────────────────────────
val dataSourceModule = module {
    // Local
    singleOf(::ExerciseLocalDataSourceImpl) { bind<ExerciseLocalDataSource>() }
    singleOf(::ProgressionLocalDataSourceImpl) { bind<ProgressionLocalDataSource>() }

    single<UserLocalDataSource> { UserLocalDataSourceImpl(database = get()) }
    single { FakeFriendsRepository() } // Removido bind genérico se não for interface

    // Remote
    single<UserRemoteDataSource> { UserRemoteDataSourceImpl(get()) }
    single<ProgressionRemoteDataSource> { ProgressionRemoteDataSourceImpl(networkDelayMs = 600L) }
    single<ActivatePlanRemoteDataSource> { ActivatePlanRemoteDataSourceImpl(api = get()) }
}
// ──────────────────────────────────────────────────────────────────────────────
// ── Mappers ───────────────────────────────────────────────────────────────────
// ──────────────────────────────────────────────────────────────────────────────
val mapperModule = module {
    single<EntityMapper<UserEntity, User>> { UserEntityMapper() }
    single<DtoMapper<UserRequestDto, User>> { UserDtoMapper() }
}
// ──────────────────────────────────────────────────────────────────────────────
// ── Repositories ──────────────────────────────────────────────────────────────
// ──────────────────────────────────────────────────────────────────────────────
val repositoryModule = module {
    //── App Config ────────────────────────────────────────────────────────────|
    singleOf(::AppOnboardingRepositoryImpl) { bind<AppOnboardingRepository>() }
    singleOf(::AppAuthenticateRepositoryImpl) { bind<AppAuthenticateRepository>() }
    singleOf(::AppLanguageRepositoryImpl) { bind<AppLanguageRepository>() }


    // ── ActivePlan ───────────────────────────────────────────────────────────|
    singleOf(::ActivatePlanRepositoryImpl) { bind<ActivatePlanRepository>() }


    // ── User ─────────────────────────────────────────────────────────────────|
    singleOf(::UserRepositoryImpl) { bind<UserRepository>() }


    // ── Exercise ─────────────────────────────────────────────────────────────|
    singleOf(::ExerciseRepositoryImpl) { bind<ExerciseRepository>() }


    // ── Progression ──────────────────────────────────────────────────────────|
    singleOf(::ProgressionRepositoryImpl) { bind<ProgressionRepository>() }


    // ── Leaderboards ─────────────────────────────────────────────────────────|


    // ── Friends ──────────────────────────────────────────────────────────────|
    singleOf(::FriendsRemoteDataSourceImpl) { bind<FriendsRepository>() }


    // ── Tasks ────────────────────────────────────────────────────────────────|


    // ── Shopping ─────────────────────────────────────────────────────────────|


    // ── Wiki ─────────────────────────────────────────────────────────────────|
    singleOf(::WikiRepositoryImpl) { bind<WikiRepository>() }
}

// ── Agregador ────────────────────────────────────────────────────────────────|
val dataModule = listOf(
    databaseModule,
    networkModule,
    dataStoreModule,
    dataSourceModule,
    mapperModule,
    repositoryModule,
)
