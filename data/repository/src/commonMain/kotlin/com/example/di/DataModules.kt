package com.example.di

import com.example.domain.models.user.User
import com.example.domain.repository.ActivatePlanRepository
import com.example.domain.repository.ExerciseRepository
import com.example.domain.repository.FakeFriendsRepository
import com.example.domain.repository.FriendsRepository
import com.example.domain.repository.ProgressionRepository
import com.example.domain.repository.dbLocal.datastore.AppAuthenticateRepository
import com.example.domain.repository.dbLocal.datastore.AppLanguageRepository
import com.example.domain.repository.dbLocal.datastore.AppOnboardingRepository
import com.example.domain.repository.dbLocal.datastore.AppTrialRepository
import com.example.domain.repository.dbLocal.sqldelight.configurations.ConfigTokenDataSourceDao
import com.example.domain.repository.dbLocal.sqldelight.user.UserRepository
import com.example.domain.repository.wiki.WikiRepository
import com.example.local.database.datastore.repository.AppAuthenticateRepositoryImpl
import com.example.local.database.datastore.repository.AppLanguageRepositoryImpl
import com.example.local.database.datastore.repository.AppOnboardingRepositoryImpl
import com.example.local.database.datastore.repository.AppTrialRepositoryImpl
import com.example.local.database.sqldelight.repository.configuration.ConfigLanguageDataSourceDao
import com.example.local.database.sqldelight.repository.configuration.ConfigOnboardingDataSourceDao
import com.example.local.database.sqldelight.repository.configuration.ConfigTokenDaoDataSourceImpl
import com.example.domain.repository.dbLocal.sqldelight.achievements.AchievementDao
import com.example.domain.repository.dbLocal.sqldelight.missions.DailyMissionDao
import com.example.domain.repository.dbLocal.sqldelight.notifications.NotificationDao
import com.example.domain.repository.dbLocal.sqldelight.nutrition.FoodItemDao
import com.example.domain.repository.dbLocal.sqldelight.nutrition.MealEntryDao
import com.example.domain.repository.dbLocal.sqldelight.stats.UserStatsDao
import com.example.domain.repository.dbLocal.sqldelight.streak.StreakDao
import com.example.domain.repository.dbLocal.sqldelight.workout.WorkoutSessionDao
import com.example.domain.repository.dbLocal.sqldelight.workout.WorkoutSetDao
import com.example.local.datasource.achievements.AchievementDaoImpl
import com.example.local.datasource.exercises.ExerciseLocalDataSource
import com.example.local.datasource.exercises.ExerciseLocalDataSourceImpl
import com.example.local.datasource.missions.DailyMissionDaoImpl
import com.example.local.datasource.notifications.NotificationDaoImpl
import com.example.local.datasource.nutrition.FoodItemDaoImpl
import com.example.local.datasource.nutrition.MealEntryDaoImpl
import com.example.local.datasource.progression.ProgressionLocalDataSource
import com.example.local.datasource.progression.ProgressionLocalDataSourceImpl
import com.example.local.datasource.stats.UserStatsDaoImpl
import com.example.local.datasource.streak.StreakDaoImpl
import com.example.local.datasource.user.UserLocalDataSource
import com.example.local.datasource.user.UserLocalDataSourceImpl
import com.example.local.datasource.workout.WorkoutSessionDaoImpl
import com.example.local.datasource.workout.WorkoutSetDaoImpl
import com.example.local.di.databaseModule
import com.example.local.mapper.EntityMapper
import com.example.util.networkModule
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

    // ── DAOs das tabelas de negócio ───────────────────────────────────────────|
    single<DailyMissionDao>   { DailyMissionDaoImpl(database = get()) }
    single<WorkoutSessionDao> { WorkoutSessionDaoImpl(database = get()) }
    single<WorkoutSetDao>     { WorkoutSetDaoImpl(database = get()) }
    single<MealEntryDao>      { MealEntryDaoImpl(database = get()) }
    single<FoodItemDao>       { FoodItemDaoImpl(database = get()) }
    single<AchievementDao>    { AchievementDaoImpl(database = get()) }
    single<NotificationDao>   { NotificationDaoImpl(database = get()) }
    single<StreakDao>         { StreakDaoImpl(database = get()) }
    single<UserStatsDao>      { UserStatsDaoImpl(database = get()) }

    // Remote
    single<UserRemoteDataSource> { UserRemoteDataSourceImpl(get()) }
    single<ProgressionRemoteDataSource> { ProgressionRemoteDataSourceImpl(networkDelayMs = 600L) }
    single<ActivatePlanRemoteDataSource> { ActivatePlanRemoteDataSourceImpl() }
}
// ──────────────────────────────────────────────────────────────────────────────
// ── Mappers ───────────────────────────────────────────────────────────────────
// ──────────────────────────────────────────────────────────────────────────────
val mapperModule = module {
    single { UserEntityMapper() }
    single { UserDtoMapper() }
    single<EntityMapper<UserEntity, User>> { UserEntityMapper() }
    single<DtoMapper<UserRequestDto, User>> { UserDtoMapper() }
}
// ──────────────────────────────────────────────────────────────────────────────
// ── Repositories ──────────────────────────────────────────────────────────────
// ──────────────────────────────────────────────────────────────────────────────
val repositoryModule = module {
    // ── AppOnboarding ─────────────────────────────────────────────────────────────|
    singleOf(::AppOnboardingRepositoryImpl) { bind<AppOnboardingRepository>() }
    // ── AppAuthenticate ─────────────────────────────────────────────────────────────|
    singleOf(::AppAuthenticateRepositoryImpl) { bind<AppAuthenticateRepository>() }
    // ── AppLanguage ─────────────────────────────────────────────────────────────|
    singleOf(::AppLanguageRepositoryImpl) { bind<AppLanguageRepository>() }
    // ── AppTrial ─────────────────────────────────────────────────────────────|
    singleOf(::AppTrialRepositoryImpl) { bind<AppTrialRepository>() }
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
