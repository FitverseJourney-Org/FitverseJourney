package org.fitverse.presentation.ui.leaderboards.viewmodel

import androidx.compose.ui.graphics.Color
import org.fitverse.presentation.theme.FitColors

data class LeaderboardEntry(
    val rank:       Int,
    val name:       String,
    val level:      Int,
    val className:  String,
    val score:      Int,
    val delta:      Int,
    val isMe:       Boolean = false,
    val isOnline:   Boolean = false,
    val classColor: Color   = FitColors.Accent,
)

val defaultLeaderboardEntries = listOf(
    LeaderboardEntry(1,  "Lucas M.", 18, "Titã",   12_580, +2,  classColor = Color(0xFFFF4D1C)),
    LeaderboardEntry(2,  "Ana S.",   14, "Sábio",   8_240, +1,  classColor = Color(0xFF7C6FFF)),
    LeaderboardEntry(3,  "Carla R.", 16, "Nômade",  7_100, -1,  classColor = Color(0xFF00C97A)),
    LeaderboardEntry(4,  "Pedro A.", 18, "Titã",    6_890, +3,  classColor = Color(0xFFFF4D1C)),
    LeaderboardEntry(5,  "Você",     12, "Titã",    6_240,  0,  isMe = true, classColor = Color(0xFFFF4D1C)),
    LeaderboardEntry(6,  "Julia F.", 11, "Nômade",  5_980, -2,  classColor = Color(0xFF00C97A)),
    LeaderboardEntry(7,  "Marco V.", 10, "Sábio",   5_610, +1,  classColor = Color(0xFF7C6FFF)),
    LeaderboardEntry(8,  "Sofia L.",  9, "Nômade",  4_990, -1,  classColor = Color(0xFF00C97A)),
    LeaderboardEntry(9,  "Bruno T.",  8, "Titã",    4_320, +4,  classColor = Color(0xFFFF4D1C)),
    LeaderboardEntry(10, "Lara C.",   7, "Sábio",   3_870, -2,  classColor = Color(0xFF7C6FFF)),
)

data class LeaderboardsUiState(
    val entries:       List<LeaderboardEntry> = defaultLeaderboardEntries,
    val seasonDaysLeft: Int                   = 6,
    val scope:         String                 = "GLOBAL",
    val metric:        String                 = "XP TOTAL",
    val period:        String                 = "SEMANA",
)

sealed interface LeaderboardsIntent {
    data object NavigateBack                  : LeaderboardsIntent
    data class FilterScope(val scope: String) : LeaderboardsIntent
    data class FilterMetric(val metric: String) : LeaderboardsIntent
    data class FilterPeriod(val period: String) : LeaderboardsIntent
}

sealed interface LeaderboardsEvent {
    data object NavigateBack : LeaderboardsEvent
}
