package org.fitverse.presentation.ui.progress

import org.fitverse.domain.models.progression.Exercise

/**
 * Mock data for the ProgressionScreen.
 *
 * ## Usage in @Preview:
 * ```kotlin
 * @Preview @Composable
 * fun ProgressionScreenPreview() {
 *     ProgressionScreen(uiState = ProgressionMockData.successState, onIntent = {})
 * }
 * ```
 */
object ProgressionMockData {

    // =========================================================================
    // Exercises
    // =========================================================================

    val exercises = listOf(
        Exercise(id = "1", name = "Supino Reto",        unit = "kg", muscleGroup = "PEITO"),
        Exercise(id = "2", name = "Supino Inclinado",   unit = "kg", muscleGroup = "PEITO"),
        Exercise(id = "3", name = "Agachamento Livre",  unit = "kg", muscleGroup = "PERNAS"),
        Exercise(id = "4", name = "Leg Press 45°",      unit = "kg", muscleGroup = "PERNAS"),
        Exercise(id = "5", name = "Levantamento Terra", unit = "kg", muscleGroup = "COSTAS"),
        Exercise(id = "6", name = "Remada Curvada",     unit = "kg", muscleGroup = "COSTAS"),
        Exercise(id = "7", name = "Desenvolvimento",    unit = "kg", muscleGroup = "OMBROS"),
        Exercise(id = "8", name = "Rosca Direta",       unit = "kg", muscleGroup = "BÍCEPS"),
    )

    // =========================================================================
    // RankInfo
    // =========================================================================

    val rankInfo = RankInfo(
        currentRank  = "PRATA",
        currentXp    = 8_548,
        nextRank     = "OURO",
        xpToNext     = 6_468,
        progressFrac = 0.57f,
    )

    // =========================================================================
    // CargaInfo
    // =========================================================================

    val cargaLevantamentoTerra = CargaInfo(
        exerciseName  = "Levantamento Terra",
        currentLoad   = 145,
        unit          = "kg",
        changePercent = 18,
        initialLoad   = 123,
        prLoad        = 145,
        muscleGroup   = "COSTAS",
        chartPoints   = listOf(123f,125f,126f,128f,130f,131f,133f,135f,138f,140f,143f,145f),
    )

    val cargaSupinoReto = CargaInfo(
        exerciseName  = "Supino Reto",
        currentLoad   = 86,
        unit          = "kg",
        changePercent = 25,
        initialLoad   = 69,
        prLoad        = 87,
        muscleGroup   = "PEITO",
        chartPoints   = listOf(69f,71f,73f,75f,78f,79f,81f,82f,84f,85f,87f,86f),
    )

    val cargaAgachamento = CargaInfo(
        exerciseName  = "Agachamento Livre",
        currentLoad   = 110,
        unit          = "kg",
        changePercent = -3,
        initialLoad   = 115,
        prLoad        = 120,
        muscleGroup   = "PERNAS",
        chartPoints   = listOf(115f,117f,120f,118f,116f,114f,115f,112f,110f,111f,110f,110f),
    )

    // =========================================================================
    // VolumeInfo  —  indigo heatmap
    // =========================================================================

    private val volumeMarco = MarcoInfo(
        sectionLabel  = "MARCOS",
        currentCount  = 3,
        totalCount    = 4,
        totalXpLabel  = "+1900 XP",
        milestones    = listOf(
            MarcoMilestone("10t",  "+200",  reached = true),
            MarcoMilestone("25t",  "+500",  reached = true),
            MarcoMilestone("50t",  "+1.2k", reached = true),
            MarcoMilestone("100t", "+3.0k", reached = false),
        ),
        progressFrac  = 0.864f,
        hintBoldText  = "13.6t",
        hintXpText    = "+3.0k XP",
    )

    val volumeInfo = VolumeInfo(
        totalTons = 86.4f,
        sessions = 30,
        xpEarned = 5_200,
        rowLabels = listOf("S","T","Q","Q","S","S","D"),
        grid = listOf(
            listOf(0.0f,0.3f,0.5f,0.7f,0.9f),
            listOf(0.2f,0.4f,0.6f,0.8f,1.0f),
            listOf(0.5f,0.7f,0.9f,0.4f,0.6f),
            listOf(0.3f,0.5f,0.7f,0.9f,0.8f),
            listOf(0.6f,0.8f,1.0f,0.5f,0.3f),
            listOf(0.4f,0.2f,0.5f,0.7f,0.9f),
            listOf(0.0f,0.1f,0.3f,0.5f,0.4f),
        ),
        marco = volumeMarco,
    )

    // =========================================================================
    // CaloriasInfo  —  orange bar chart
    // =========================================================================

    private val diasNaMetaMarco = MarcoInfo(
        sectionLabel  = "DIAS NA META",
        currentCount  = 0,
        totalCount    = 4,
        totalXpLabel  = "+8 XP",
        milestones    = listOf(
            MarcoMilestone("7d",   "+300",  reached = false),
            MarcoMilestone("30d",  "+1.2k", reached = false),
            MarcoMilestone("60d",  "+2.5k", reached = false),
            MarcoMilestone("100d", "+5.0k", reached = false),
        ),
        progressFrac  = 0.02f,
        hintBoldText  = "7d",
        hintXpText    = "+300 XP",
    )

    val caloriasInfo = CaloriasInfo(
        avgDaily      = 2_182,
        changePercent = 1,
        today         = 2_473,
        goal          = 2_200,
        streak        = 0,
        xpEarned      = 720,
        barValues     = listOf(2050f,2350f,1980f,2420f,2180f,2090f,2300f,2473f,2150f,2400f,2250f,2182f),
        diasNaMeta    = diasNaMetaMarco,
    )

    // =========================================================================
    // CardioInfo  —  cyan line chart
    // =========================================================================

    private val distanciaAcumuladaMarco = MarcoInfo(
        sectionLabel  = "DISTÂNCIA ACUMULADA",
        currentCount  = 1,
        totalCount    = 4,
        totalXpLabel  = "+400 XP",
        milestones    = listOf(
            MarcoMilestone("25km",  "+400",  reached = true),
            MarcoMilestone("50km",  "+900",  reached = false),
            MarcoMilestone("100km", "+2.0k", reached = false),
            MarcoMilestone("250km", "+5.5k", reached = false),
        ),
        progressFrac  = 0.42f / 0.50f * 0.33f,
        hintBoldText  = "8km",
        hintXpText    = "+900 XP",
    )

    val cardioInfo = CardioInfo(
        paceMin            = 5,
        paceSec            = 22,
        changePercent      = 9,
        vo2Max             = 48,
        distKm             = 42.0f,
        aerobicZoneFrac    = 0.70f,
        xpEarned           = 1_500,
        chartPoints        = listOf(6.10f,6.00f,5.90f,5.82f,5.75f,5.68f,5.60f,5.52f,5.40f,5.30f,5.25f,5.22f),
        distanciaAcumulada = distanciaAcumuladaMarco,
    )

    // =========================================================================
    // ConsistenciaInfo  —  amber grid
    // =========================================================================

    private val marcosStreakMarco = MarcoInfo(
        sectionLabel  = "MARCOS DE STREAK",
        currentCount  = 1,
        totalCount    = 4,
        totalXpLabel  = "+200 XP",
        milestones    = listOf(
            MarcoMilestone("7d",   "+200",  reached = true),
            MarcoMilestone("30d",  "+800",  reached = false),
            MarcoMilestone("60d",  "+2.0k", reached = false),
            MarcoMilestone("100d", "+5.0k", reached = false),
        ),
        progressFrac  = 0.10f + (23f - 7f) / (30f - 7f) * 0.33f,
        hintBoldText  = "7d",
        hintXpText    = "+800 XP",
    )

    val consistenciaInfo = ConsistenciaInfo(
        currentStreak = 23,
        recordStreak  = 47,
        xpEarned      = 775,
        weekGrid      = listOf(
            listOf(false,false,true, true, true, true, true),
            listOf(true, true, true, true, true, true, true),
            listOf(true, true, true, true, true, true, true),
            listOf(true, true, true, true, true, false,false),
            listOf(true, true, true, true, false,false,false),
        ),
        marcosStreak  = marcosStreakMarco,
    )

    // =========================================================================
    // Complete UiState variants
    // =========================================================================

    /** Matches all 5 screenshots exactly. */
    val successState = ProgressUiState.Success(
        period       = PeriodFilter.THIRTY_DAYS,
        rank         = rankInfo,
        carga        = cargaLevantamentoTerra,
        exercises    = exercises,
        volume       = volumeInfo,
        calorias     = caloriasInfo,
        cardio       = cardioInfo,
        consistencia = consistenciaInfo,
        isRefreshing = false,
    )

    /** Supino variant for carga card. */
    val supinState = successState.copy(carga = cargaSupinoReto)

    /** Plateau — carga dropping, pace regressing. */
    val plateauState = successState.copy(
        carga = cargaAgachamento,
        cardio = cardioInfo.copy(
            paceMin       = 5,
            paceSec       = 45,
            changePercent = -3,
            chartPoints   = listOf(5.22f,5.30f,5.40f,5.50f,5.48f,5.52f,5.44f,5.50f,5.48f,5.45f),
        ),
    )

    /** New user — barely started. */
    val newUserState = ProgressUiState.Success(
        period = PeriodFilter.SEVEN_DAYS,
        rank   = RankInfo("BRONZE", 240, "PRATA", 9_760, 0.02f),
        carga  = CargaInfo("Supino Reto", 50, "kg", 4, 48, 50, "PEITO", listOf(48f,49f,50f,50f)),
        exercises    = exercises.take(3),
        volume       = VolumeInfo(3.2f, 4, 0, List(7) { List(5) { 0f } }, listOf("S","T","Q","Q","S","S","D"), null),
        calorias     = caloriasInfo.copy(xpEarned = 0, diasNaMeta = null),
        cardio       = cardioInfo.copy(xpEarned = 0, distanciaAcumulada = null),
        consistencia = consistenciaInfo.copy(currentStreak = 4, xpEarned = 0, marcosStreak = null),
    )

    /** Refreshing overlay. */
    val refreshingState = successState.copy(isRefreshing = true)

    /** Loading skeleton. */
    val loadingState: ProgressUiState = ProgressUiState.Loading

    /** Recoverable error. */
    val errorState: ProgressUiState = ProgressUiState.Error(
        message  = "Não foi possível carregar sua progressão.\nVerifique sua conexão e tente novamente.",
        canRetry = true,
    )

    /** Non-recoverable error. */
    val errorNoRetryState: ProgressUiState = ProgressUiState.Error(
        message  = "Sessão expirada. Faça login novamente.",
        canRetry = false,
    )
}

// =============================================================================
// Required additions to UiState.kt  (copy-paste)
// =============================================================================
//
// Add these fields to the existing data classes:
//
// data class VolumeInfo(
//     ...existing fields...,
//     val xpEarned : Int        = 0,           // NEW
//     val marco    : MarcoInfo? = null,         // NEW
// )
//
// data class CaloriasInfo(
//     ...existing fields...,
//     val xpEarned  : Int        = 0,           // NEW
//     val diasNaMeta: MarcoInfo? = null,         // NEW
// )
//
// data class CardioInfo(
//     ...existing fields...,
//     val xpEarned           : Int        = 0,  // NEW
//     val distanciaAcumulada : MarcoInfo? = null,// NEW
// )
//
// data class ConsistenciaInfo(
//     ...existing fields...,
//     val xpEarned    : Int        = 0,         // NEW
//     val marcosStreak: MarcoInfo? = null,       // NEW
// )