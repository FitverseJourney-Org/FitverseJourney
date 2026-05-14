package com.example.presentation.ui.achievements.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.repository.authentication.AuthRepository
import com.example.domain.repository.dbLocal.sqldelight.achievements.AchievementDao
import com.example.domain.repository.dbLocal.sqldelight.achievements.AchievementRecord
import com.example.presentation.ui.achievements.Achievement
import com.example.presentation.ui.achievements.AchievementsData
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AchievementsViewModel(
    private val achievementDao: AchievementDao,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AchievementsUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<AchievementsEvent>()
    val events = _events.receiveAsFlow()

    init {
        observeAchievements()
    }

    fun onIntent(intent: AchievementsIntent) {
        when (intent) {
            AchievementsIntent.NavigateBack -> viewModelScope.launch {
                _events.send(AchievementsEvent.NavigateBack)
            }
            is AchievementsIntent.FilterByCategory -> _uiState.update {
                it.copy(selectedCategory = intent.category)
            }
            is AchievementsIntent.FilterByStatus -> _uiState.update {
                it.copy(selectedStatus = intent.status)
            }
            is AchievementsIntent.SelectAchievement -> _uiState.update {
                it.copy(selectedAchievement = intent.achievement)
            }
        }
    }

    private fun observeAchievements() {
        val userId = authRepository.getCurrentUserId() ?: return
        viewModelScope.launch {
            achievementDao.observeAchievementsByUser(userId)
                .catch { _uiState.update { it.copy(isLoading = false) } }
                .collect { records ->
                    if (records.isEmpty()) {
                        seedAchievements(userId)
                    } else {
                        _uiState.update {
                            it.copy(
                                achievements = records.map { r -> r.toAchievementUi() },
                                isLoading = false,
                            )
                        }
                    }
                }
        }
    }

    private suspend fun seedAchievements(userId: String) {
        achievementDao.insertAchievements(AchievementsData.all.map { it.toSeedRecord(userId) })
    }
}

private fun Achievement.toSeedRecord(userId: String) = AchievementRecord(
    id          = id,
    userId      = userId,
    icon        = icon,
    title       = name,
    description = desc,
    xpReward    = xp,
    rarity      = rarity.name,
    status      = status.name,
    category    = cat.name,
    progress    = progress.toDouble(),
    maxProgress = 1.0,
    condition   = condition,
    unlockedAt  = date.ifEmpty { null },
)
