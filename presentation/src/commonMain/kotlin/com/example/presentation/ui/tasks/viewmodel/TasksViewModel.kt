package com.example.presentation.ui.tasks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.models.dashboard.tasks.TaskIcon
import com.example.domain.models.dashboard.tasks.TaskItem
import com.example.domain.repository.dbLocal.sqldelight.missions.DailyMissionRecord
import com.example.domain.usecase.missions.ObserveDailyMissionsUseCase
import com.example.expect.PlatformDateFormatter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TasksUiState(
    val missions: List<TaskItem> = emptyList(),
    val isLoading: Boolean = true,
    val swapsRemaining: Int = 2,
)

class TasksViewModel(
    private val observeDailyMissions: ObserveDailyMissionsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TasksUiState())
    val uiState = _uiState.asStateFlow()

    private val today: String get() = PlatformDateFormatter.getTodayIsoDate()

    init {
        observeMissions()
    }

    private fun observeMissions() {
        viewModelScope.launch {
            observeDailyMissions(today)
                .catch { _uiState.update { it.copy(isLoading = false) } }
                .collect { records ->
                    _uiState.update {
                        it.copy(
                            missions  = records.map { r -> r.toTaskItem() },
                            isLoading = false,
                        )
                    }
                }
        }
    }

    // ── Mapper ────────────────────────────────────────────────────────────────

    private fun DailyMissionRecord.toTaskItem() = TaskItem(
        id          = id,
        title       = title,
        description = description,
        xp          = xpReward,
        completed   = isCompleted,
        iconType    = when (type) {
            "CARDIO", "STEPS"     -> TaskIcon.RUN
            "NUTRITION","HYDRATION" -> TaskIcon.NUTRITION
            "STRENGTH","CHALLENGE"  -> TaskIcon.WORKOUT
            else                    -> TaskIcon.GENERIC
        },
    )
}
