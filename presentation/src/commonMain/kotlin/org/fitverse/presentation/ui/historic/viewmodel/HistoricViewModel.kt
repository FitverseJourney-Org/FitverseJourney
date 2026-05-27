package org.fitverse.presentation.ui.historic.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import org.fitverse.domain.repository.dbLocal.sqldelight.workout.WorkoutSessionRecord
import org.fitverse.domain.usecase.workout.DeleteWorkoutSessionUseCase
import org.fitverse.domain.usecase.workout.GetSessionsByPeriodUseCase
import org.fitverse.domain.usecase.workout.ObserveWorkoutSessionsUseCase
import org.fitverse.presentation.expect.DateFormatter
import org.fitverse.presentation.ui.historic.HistoricPeriod
import org.fitverse.presentation.ui.historic.WorkoutHistory
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class HistoricViewModel(
    private val observeWorkoutSessions: ObserveWorkoutSessionsUseCase,
    private val getSessionsByPeriod: GetSessionsByPeriodUseCase,
    private val deleteWorkoutSession: DeleteWorkoutSessionUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoricUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<HistoricEvent>()
    val events = _events.receiveAsFlow()

    init {
        observeSessions()
    }

    fun onIntent(intent: HistoricIntent) {
        when (intent) {
            HistoricIntent.NavigateBack -> viewModelScope.launch {
                _events.send(HistoricEvent.NavigateBack)
            }
            is HistoricIntent.SelectPeriod -> applyPeriodFilter(intent.period)
            is HistoricIntent.DeleteSession -> deleteSession(intent.sessionId)
        }
    }

    private fun observeSessions() {
        viewModelScope.launch {
            observeWorkoutSessions()
                .catch { _uiState.update { it.copy(isLoading = false) } }
                .collect { records ->
                    val history = records.map { it.toUi() }
                    _uiState.update { state ->
                        state.copy(
                            allHistory = history,
                            history    = history.filterByPeriod(state.selectedPeriod),
                            isLoading  = false,
                        )
                    }
                }
        }
    }

    private fun applyPeriodFilter(period: HistoricPeriod) {
        _uiState.update { state ->
            state.copy(
                selectedPeriod = period,
                history        = state.allHistory.filterByPeriod(period),
            )
        }
    }

    private fun deleteSession(sessionId: String) {
        viewModelScope.launch {
            deleteWorkoutSession(sessionId)
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun List<WorkoutHistory>.filterByPeriod(period: HistoricPeriod): List<WorkoutHistory> {
        val cutoff = DateFormatter.getCurrentTimeMillis() - period.days * 86_400_000L
        return filter { it.timestamp >= cutoff }
    }

    // ── Mapper ────────────────────────────────────────────────────────────────

    private fun WorkoutSessionRecord.toUi() = WorkoutHistory(
        id             = id,
        title          = workoutTitle,
        dateDisplay    = startedAt.toDisplayDate(),
        timestamp      = startedAt,
        duration       = durationSeconds.toDisplayDuration(),
        totalVolume    = "${totalVolume.toInt()} kg",
        muscleGroups   = muscleGroups.split(",").map { it.trim() }.filter { it.isNotBlank() },
        exerciseCount  = totalSets,
        xpEarned       = xpEarned,
        hasPR          = hasPR,
        intensityLevel = intensityLevel,
        notes          = notes,
    )

    private fun Long.toDisplayDate(): String {
        val dt = Instant.fromEpochMilliseconds(this)
            .toLocalDateTime(TimeZone.currentSystemDefault())
        return "${dt.dayOfMonth.toString().padStart(2, '0')}/${dt.monthNumber.toString().padStart(2, '0')}/${dt.year}"
    }

    private fun Int.toDisplayDuration(): String {
        val h = this / 3600
        val m = (this % 3600) / 60
        return if (h > 0) "${h}h ${m}min" else "${m}min"
    }
}
