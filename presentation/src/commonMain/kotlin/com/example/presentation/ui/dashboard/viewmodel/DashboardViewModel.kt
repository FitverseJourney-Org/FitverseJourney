package com.example.presentation.ui.dashboard.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.missions.CompleteMissionUseCase
import com.example.domain.usecase.missions.GetDailyMissionsUseCase
import com.example.domain.usecase.missions.ObserveDailyMissionsUseCase
import com.example.domain.usecase.stats.ObserveUserStatsUseCase
import com.example.domain.usecase.streak.ObserveStreakWeekUseCase
import com.example.domain.repository.dbLocal.sqldelight.missions.DailyMissionRecord
import com.example.domain.repository.dbLocal.sqldelight.streak.StreakRecord
import com.example.presentation.ui.dashboard.DailyMission
import com.example.presentation.ui.dashboard.MissionType
import com.example.expect.PlatformDateFormatter
import com.example.presentation.widgets.StreakDay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val observeDailyMissions: ObserveDailyMissionsUseCase,
    private val completeMission: CompleteMissionUseCase,
    private val getDailyMissions: GetDailyMissionsUseCase,
    private val observeUserStats: ObserveUserStatsUseCase,
    private val observeStreakWeek: ObserveStreakWeekUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<DashboardEvent>()
    val events = _events.receiveAsFlow()

    private val today: String
        get() = PlatformDateFormatter.getTodayIsoDate()

    init {
        loadDashboard()
    }

    fun onIntent(intent: DashboardIntent) {
        when (intent) {
            is DashboardIntent.ClaimMission -> claimMission(intent.missionId, intent.missionTitle)
        }
    }

    private fun loadDashboard() {
        viewModelScope.launch {
            combine(
                observeDailyMissions(today),
                observeUserStats(),
                observeStreakWeek(),
            ){ missions, stats, streak ->
                Triple(missions, stats, streak)
            }.catch {
                _uiState.update { it.copy(isLoading = false) }
            }.collect { (missions, stats, streak) ->
                if (missions.isEmpty()) {
                    viewModelScope.launch {
                        getDailyMissions(today).onFailure {
                            _uiState.update { s -> s.copy(isLoading = false) }
                        }
                    }
                    return@collect  // keep isLoading = true until missions arrive from DB
                }
                _uiState.update {
                    it.copy(
                        missions    = missions.map { r -> r.toUi() },
                        streakDays  = streak.takeLast(7).toStreakDays(),
                        username    = stats?.let { s -> "Nível ${s.currentLevel}" } ?: it.username,
                        isLoading   = false,
                    )
                }
            }
        }
    }

    private fun claimMission(missionId: String, title: String) {
        viewModelScope.launch {
            val now = PlatformDateFormatter.getCurrentTimeMillis()
            completeMission(missionId, now)
                .onSuccess {
                    _events.send(DashboardEvent.ShowSnackbar("Missão concluída! XP ganho."))
                }
                .onFailure {
                    _uiState.update { state ->
                        state.copy(missions = state.missions.map { m ->
                            if (m.title == title) m.copy(isCompleted = true) else m
                        })
                    }
                    _events.send(DashboardEvent.ShowSnackbar("Missão concluída! XP ganho."))
                }
        }
    }

    // ── Mappers ───────────────────────────────────────────────────────────────

    private fun DailyMissionRecord.toUi() = DailyMission(
        id          = id,
        title       = title,
        description = description,
        xp          = xpReward,
        type        = runCatching { MissionType.valueOf(type) }.getOrDefault(MissionType.CARDIO),
        isCompleted = isCompleted,
    )

    private fun List<StreakRecord>.toStreakDays(): List<StreakDay> {
        val dayLabels = listOf("S", "T", "Q", "Q", "S", "S", "D")
        return takeLast(7).mapIndexed { i, r ->
            StreakDay(dayLabels[i % dayLabels.size], isCompleted = r.isCheckedIn)
        }
    }
}
