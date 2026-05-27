package org.fitverse.data.repository.di

import org.fitverse.domain.models.user.User
import org.fitverse.domain.repository.ActivatePlanRepository
import org.fitverse.domain.repository.ExerciseRepository
import org.fitverse.domain.repository.FakeFriendsRepository
import org.fitverse.domain.repository.FriendsRepository
import org.fitverse.domain.repository.ProgressionRepository
import org.fitverse.domain.repository.dbLocal.datastore.AppAuthenticateRepository
import org.fitverse.domain.repository.dbLocal.datastore.AppLanguageRepository
import org.fitverse.domain.repository.dbLocal.datastore.AppOnboardingRepository
import org.fitverse.domain.repository.dbLocal.datastore.AppTrialRepository
import org.fitverse.domain.repository.dbLocal.sqldelight.user.UserRepository
import org.fitverse.domain.repository.wiki.WikiRepository
import org.fitverse.data.local.database.datastore.repository.AppAuthenticateRepositoryImpl
import org.fitverse.data.local.database.datastore.repository.AppLanguageRepositoryImpl
import org.fitverse.data.local.database.datastore.repository.AppOnboardingRepositoryImpl
import org.fitverse.data.local.database.datastore.repository.AppTrialRepositoryImpl
import org.fitverse.domain.repository.dbLocal.sqldelight.achievements.AchievementDao
import org.fitverse.domain.repository.dbLocal.sqldelight.catalog.CatalogMissionDao
import org.fitverse.domain.repository.dbLocal.sqldelight.missions.DailyMissionDao
import org.fitverse.domain.repository.dbLocal.sqldelight.notifications.NotificationDao
import org.fitverse.domain.repository.dbLocal.sqldelight.nutrition.FoodItemDao
import org.fitverse.domain.repository.dbLocal.sqldelight.nutrition.MealEntryDao
import org.fitverse.domain.repository.dbLocal.sqldelight.stats.UserStatsDao
import org.fitverse.domain.repository.dbLocal.sqldelight.streak.StreakDao
import org.fitverse.domain.repository.dbLocal.sqldelight.workout.WorkoutSessionDao
import org.fitverse.domain.repository.dbLocal.sqldelight.workout.WorkoutSetDao
import org.fitverse.data.local.datasource.achievements.AchievementDaoImpl
import org.fitverse.local.datasource.catalog.CatalogMissionDaoImpl
import org.fitverse.data.local.datasource.exercises.ExerciseLocalDataSource
import org.fitverse.data.local.datasource.exercises.ExerciseLocalDataSourceImpl
import org.fitverse.data.local.datasource.missions.DailyMissionDaoImpl
import org.fitverse.data.local.datasource.notifications.NotificationDaoImpl
import org.fitverse.data.local.datasource.nutrition.FoodItemDaoImpl
import org.fitverse.data.local.datasource.nutrition.MealEntryDaoImpl
import org.fitverse.data.local.datasource.progression.ProgressionLocalDataSource
import org.fitverse.data.local.datasource.progression.ProgressionLocalDataSourceImpl
import org.fitverse.local.datasource.stats.UserStatsDaoImpl
import org.fitverse.data.local.datasource.streak.StreakDaoImpl
import org.fitverse.data.local.datasource.user.UserLocalDataSource
import org.fitverse.local.datasource.user.UserLocalDataSourceImpl
import org.fitverse.data.local.datasource.workout.WorkoutSessionDaoImpl
import org.fitverse.data.local.datasource.workout.WorkoutSetDaoImpl
import org.fitverse.local.di.databaseModule
import org.fitverse.data.local.mapper.EntityMapper
import org.fitverse.data.repository.util.networkModule
import org.fitverse.data.local.mapper.user.UserEntityMapper
import org.fitverse.data.remote.datasource.activePlan.ActivatePlanRemoteDataSource
import org.fitverse.data.remote.datasource.activePlan.ActivatePlanRemoteDataSourceImpl
import org.fitverse.data.remote.datasource.friends.FriendsRemoteDataSourceImpl
import org.fitverse.data.remote.datasource.progression.ProgressionRemoteDataSource
import org.fitverse.data.remote.datasource.progression.ProgressionRemoteDataSourceImpl
import org.fitverse.data.remote.datasource.user.UserRemoteDataSource
import org.fitverse.data.remote.datasource.user.UserRemoteDataSourceImpl
import org.fitverse.remote.dto.user.UserRequestDto
import org.fitverse.data.remote.mapper.DtoMapper
import org.fitverse.data.remote.mapper.user.UserDtoMapper
import org.fitverse.domain.repository.workoutPlan.WorkoutPlanRepository
import org.fitverse.data.repository.UserRepositoryImpl
import org.fitverse.data.repository.WikiRepositoryImpl
import org.fitverse.data.repository.WorkoutPlanRepositoryImpl
import org.fitverse.data.repository.activePlan.ActivatePlanRepositoryImpl
import org.fitverse.data.repository.exercise.ExerciseRepositoryImpl
import org.fitverse.data.repository.progression.ProgressionRepositoryImpl
import org.fitverse.data.repository.rank.RankRepositoryImpl
import org.fitverse.data.repository.volume.VolumeRepositoryImpl
import org.fitverse.data.repository.calorias.CaloriasRepositoryImpl
import org.fitverse.data.repository.cardio.CardioRepositoryImpl
import org.fitverse.data.repository.consistencia.ConsistenciaRepositoryImpl
import org.fitverse.domain.repository.RankRepository
import org.fitverse.domain.repository.VolumeRepository
import org.fitverse.domain.repository.CaloriasRepository
import org.fitverse.domain.repository.CardioRepository
import org.fitverse.domain.repository.ConsistenciaRepository
import com.journey.user.UserEntity
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


// ──────────────────────────────────────────────────────────────────────────────
// ── DataStore ─────────────────────────────────────────────────────────────────
// ──────────────────────────────────────────────────────────────────────────────
val dataStoreModule = module {

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
    single<CatalogMissionDao>  { CatalogMissionDaoImpl(database = get()) }
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
    // ── WorkoutPlan ───────────────────────────────────────────────────────────|
    single<WorkoutPlanRepository> { WorkoutPlanRepositoryImpl() }
    // ── Rank ─────────────────────────────────────────────────────────────────|
    single<RankRepository>        { RankRepositoryImpl() }
    // ── Volume ───────────────────────────────────────────────────────────────|
    single<VolumeRepository>      { VolumeRepositoryImpl() }
    // ── Calorias ─────────────────────────────────────────────────────────────|
    single<CaloriasRepository>    { CaloriasRepositoryImpl() }
    // ── Cardio ───────────────────────────────────────────────────────────────|
    single<CardioRepository>      { CardioRepositoryImpl() }
    // ── Consistência ─────────────────────────────────────────────────────────|
    single<ConsistenciaRepository> { ConsistenciaRepositoryImpl() }
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
