package org.fitverse.presentation.ui.tasks.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.fitverse.domain.models.dashboard.tasks.TaskIcon
import org.fitverse.domain.models.dashboard.tasks.TaskItem
import org.fitverse.domain.repository.dbLocal.sqldelight.catalog.CatalogMissionDao
import org.fitverse.domain.repository.dbLocal.sqldelight.catalog.CatalogMissionRecord
import org.fitverse.domain.repository.dbLocal.sqldelight.missions.DailyMissionRecord
import org.fitverse.domain.usecase.missions.ObserveDailyMissionsUseCase
import org.fitverse.domain.usecase.missions.SwapMissionUseCase
import org.fitverse.presentation.expect.DateFormatter
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TasksUiState(
    val missions: List<TaskItem> = emptyList(),
    val catalogMissions: List<TaskItem> = emptyList(),
    val isLoading: Boolean = true,
    val swapsRemaining: Int = 2,
)

sealed interface TasksIntent {
    data class SwapMission(val taskToReplaceId: String, val catalogMissionId: String) : TasksIntent
}

sealed interface TasksEvent {
    data object SwapSuccess : TasksEvent
    data class ShowSnackbar(val message: String) : TasksEvent
}

class TasksViewModel(
    private val observeDailyMissions: ObserveDailyMissionsUseCase,
    private val swapMission: SwapMissionUseCase,
    private val catalogDao: CatalogMissionDao,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TasksUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<TasksEvent>()
    val events = _events.receiveAsFlow()

    private val today: String get() = DateFormatter.getTodayIsoDate()

    init {
        observeMissions()
        loadCatalog()
    }

    fun onIntent(intent: TasksIntent) {
        when (intent) {
            is TasksIntent.SwapMission -> swap(intent.taskToReplaceId, intent.catalogMissionId)
        }
    }

    private fun observeMissions() {
        viewModelScope.launch {
            observeDailyMissions(today)
                .catch { _uiState.update { it.copy(isLoading = false) } }
                .collect { records ->
                    _uiState.update { state ->
                        state.copy(
                            missions       = records.map { r -> r.toTaskItem() },
                            catalogMissions = filterCatalog(state.catalogMissions, records.map { it.title }),
                            isLoading      = false,
                        )
                    }
                }
        }
    }

    private fun loadCatalog() {
        viewModelScope.launch {
            val all = catalogDao.getAll()
            _uiState.update { state ->
                state.copy(
                    catalogMissions = filterCatalog(all.map { it.toTaskItem() }, state.missions.map { it.title })
                )
            }
        }
    }

    private fun swap(taskToReplaceId: String, catalogMissionId: String) {
        viewModelScope.launch {
            val now = DateFormatter.getCurrentTimeMillis()
            swapMission(taskToReplaceId, catalogMissionId, today, now)
                .onSuccess {
                    _uiState.update { it.copy(swapsRemaining = (it.swapsRemaining - 1).coerceAtLeast(0)) }
                    _events.send(TasksEvent.SwapSuccess)
                }
                .onFailure {
                    _events.send(TasksEvent.ShowSnackbar("Erro ao trocar missão"))
                }
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun filterCatalog(catalog: List<TaskItem>, assignedTitles: List<String>): List<TaskItem> {
        val assigned = assignedTitles.toSet()
        return catalog.filter { it.title !in assigned }
    }

    // ── Mappers ───────────────────────────────────────────────────────────────

    private fun DailyMissionRecord.toTaskItem() = TaskItem(
        id          = id,
        title       = title,
        description = description,
        xp          = xpReward,
        completed   = isCompleted,
        iconType    = when (type) {
            "CARDIO", "STEPS"           -> TaskIcon.RUN
            "NUTRITION", "HYDRATION"    -> TaskIcon.NUTRITION
            "STRENGTH", "CHALLENGE"     -> TaskIcon.WORKOUT
            else                        -> TaskIcon.GENERIC
        },
    )

    private fun CatalogMissionRecord.toTaskItem() = TaskItem(
        id          = id,
        title       = title,
        description = description,
        xp          = xpReward,
        completed   = false,
        iconType    = when (type) {
            "CARDIO", "STEPS"           -> TaskIcon.RUN
            "NUTRITION", "HYDRATION"    -> TaskIcon.NUTRITION
            "STRENGTH", "CHALLENGE"     -> TaskIcon.WORKOUT
            else                        -> TaskIcon.GENERIC
        },
    )
}
