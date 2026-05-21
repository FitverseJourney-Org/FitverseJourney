@file:OptIn(ExperimentalMaterial3Api::class)

package org.fitverse.presentation.ui.notification

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.fitverse.domain.models.notification.NotificationType
import org.fitverse.presentation.expect.DateFormatter
import org.fitverse.presentation.expect.formatRelativeTime
import org.fitverse.presentation.ui.notification.helpers.NotificationStyle
import org.fitverse.presentation.ui.notification.helpers.NotificationStyles
import org.fitverse.presentation.ui.notification.viewmodel.NotificationEvent
import org.fitverse.presentation.ui.notification.viewmodel.NotificationIntent
import org.fitverse.presentation.widgets.FitverseTopAppBar
import org.fitverse.presentation.theme.FitColors
import kotlinx.coroutines.delay
import kotlin.time.TimeSource


// ── Helpers ────────────────────────────────────────────────────────────────────

private fun NotificationType.actionLabel(): String? = when (this) {
    NotificationType.DESAFIO -> "Participar"
    NotificationType.TREINO  -> "Ver treino"
    NotificationType.RANKING -> "Ver ranking"
    else                     -> null
}

private fun List<NotificationUiModel>.groupedByPeriod(): Map<String, List<NotificationUiModel>> {
    val now        = DateFormatter.getCurrentTimeMillis()
    val oneDay     = 86_400_000L

    val hoje       = mutableListOf<NotificationUiModel>()
    val ontem      = mutableListOf<NotificationUiModel>()
    val estaSemana = mutableListOf<NotificationUiModel>()
    val antigas    = mutableListOf<NotificationUiModel>()

    forEach { notif ->
        val diff = now - notif.createdAt
        when {
            diff < oneDay        -> hoje.add(notif)
            diff < oneDay * 2    -> ontem.add(notif)
            diff < oneDay * 7    -> estaSemana.add(notif)
            else                 -> antigas.add(notif)
        }
    }

    return buildMap {
        if (hoje.isNotEmpty())       put("Hoje",         hoje)
        if (ontem.isNotEmpty())      put("Ontem",        ontem)
        if (estaSemana.isNotEmpty()) put("Esta semana",  estaSemana)
        if (antigas.isNotEmpty())    put("Mais antigas", antigas)
    }
}

// ── Mocks ──────────────────────────────────────────────────────────────────────

private val now get() = DateFormatter.getCurrentTimeMillis()

private fun mockUiModel(
    id: String,
    type: NotificationType,
    title: String,
    description: String,
    isRead: Boolean = true,
    createdAt: Long,
): NotificationUiModel {
    val style = NotificationStyles.from(type)
    return NotificationUiModel(
        id           = id,
        type         = type,
        title        = title,
        description  = description,
        isRead       = isRead,
        createdAt    = createdAt,
        icon         = style.icon,
        iconBg       = style.iconBg,
        dotColor     = if (!isRead) style.dotColor else null,
        borderAccent = if (!isRead) style.borderAccent else null,
    )
}

val sampleNotificationUiModels: List<NotificationUiModel> = listOf(
    mockUiModel("1", NotificationType.XP,         "XP Ganho!",              "Você ganhou +150 XP pela missão de cardio.",    isRead = false, createdAt = now - 2 * 60_000L),
    mockUiModel("2", NotificationType.STREAK,     "Streak Ativo!",           "Você está há 7 dias em sequência. Incrível!",  isRead = false, createdAt = now - 10 * 60_000L),
    mockUiModel("3", NotificationType.DESAFIO,    "Desafio disponível!",     "30 dias de treino — participe e ganhe 500 XP!", isRead = false, createdAt = now - 30_000L),
    mockUiModel("4", NotificationType.COMENTARIO, "Max comentou seu post",   "\"Que treino insano cara! Como você faz?\"",   isRead = false, createdAt = now - 5 * 60_000L),
    mockUiModel("5", NotificationType.RANKING,    "Subiu no ranking!",       "Você está em 5º na Liga Ouro desta semana.",   isRead = false, createdAt = now - 3 * 3_600_000L),
    mockUiModel("6", NotificationType.CURTIDA,    "Luna curtiu seu post",    "\"Treino de peito arrasado! 💪\"",             isRead = true,  createdAt = now - 1 * 3_600_000L),
    mockUiModel("7", NotificationType.TREINO,     "Hora do treino!",         "Seu treino Hypertrophy A aguarda você.",       isRead = true,  createdAt = now - 2 * 3_600_000L),
    mockUiModel("8", NotificationType.CONQUISTA,  "Nova Conquista!",         "Você desbloqueou \"Iron Will\" — 100 treinos!", isRead = true, createdAt = now - 1 * 86_400_000L),
    mockUiModel("9", NotificationType.SISTEMA,    "Atualização disponível",  "Versão 2.4 com novidades no treino e nutrição.", isRead = true, createdAt = now - 10 * 86_400_000L),
)

// ── Root ───────────────────────────────────────────────────────────────────────

@Composable
fun NotificationRoot(
    viewModel: NotificationViewModel,
    onBack: () -> Unit,
    onExplore: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()
    var showSkeleton by remember { mutableStateOf(false) }

    // Só mostra o skeleton após a animação de entrada terminar
    LaunchedEffect(Unit) {
        delay(350)               // ← espera a animação slideFromTop terminar
        showSkeleton = true
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                NotificationEvent.NavigateBack -> onBack()
            }
        }
    }

    when {
        uiState.isLoading && !showSkeleton -> Box(Modifier.fillMaxSize())  // tela em branco durante animação
        uiState.isLoading -> NotificationSkeleton()
        uiState.notifications.isEmpty() -> NotificationScreen(
            onBack        = { viewModel.onIntent(NotificationIntent.NavigateBack) },
            onExplore     = onExplore,
            notifications = emptyList(),
        )
        else -> NotificationScreen(
            onBack        = { viewModel.onIntent(NotificationIntent.NavigateBack) },
            onExplore     = onExplore,
            notifications = uiState.notifications,
        )
    }
}

// Skeleton simples enquanto carrega
@Composable
private fun NotificationSkeleton() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        repeat(6) {
            SkeletonRow()
        }
    }
}
@Composable
private fun SkeletonRow() {
    val alpha by rememberInfiniteTransition(label = "skeleton")
        .animateFloat(
            initialValue = 0.3f,
            targetValue  = 0.7f,
            animationSpec = infiniteRepeatable(
                animation  = tween(800),
                repeatMode = RepeatMode.Reverse,
            ),
            label = "alpha",
        )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Ícone
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(FitColors.Surface.copy(alpha = alpha)),
        )
        Spacer(Modifier.width(14.dp))
        // Conteúdo
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Box(
                modifier = Modifier
                    .width(160.dp)
                    .height(13.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(FitColors.Surface.copy(alpha = alpha)),
            )
            Box(
                modifier = Modifier
                    .width(220.dp)
                    .height(11.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(FitColors.Surface.copy(alpha = alpha)),
            )
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(10.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(FitColors.Surface.copy(alpha = alpha)),
            )
        }
    }
}
// ── Screen ─────────────────────────────────────────────────────────────────────

@Composable
fun NotificationScreen(
    onBack: () -> Unit,
    onExplore: () -> Unit = {},
    notifications: List<NotificationUiModel> = sampleNotificationUiModels,
) {

    val grouped = remember(notifications) {
        val mark = TimeSource.Monotonic.markNow()
        val result = notifications.groupedByPeriod()
        println("NotificationScreen | groupedByPeriod ${notifications.size} items | ${mark.elapsedNow().inWholeMicroseconds}µs")
        result
    }

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            FitverseTopAppBar(title = "Notificações", onBack = onBack)
        },
    ) { innerPadding ->

        if (notifications.isEmpty()) {
            EmptyNotifications(
                onExplore = onExplore,
                modifier  = Modifier.padding(innerPadding),
            )
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 32.dp),
        ) {
            grouped.forEach { (period, items) ->
                item(key = "header_$period") {
                    PeriodHeader(label = period)
                }
                items(items, key = { it.id }) { notif ->
                    NotificationRow(notif = notif)
                    HorizontalDivider(
                        modifier  = Modifier.padding(start = 78.dp, end = 20.dp),
                        thickness = 0.5.dp,
                        color     = FitColors.Border.copy(alpha = 0.5f),
                    )
                }
            }
        }
    }
}

// ── Empty state ────────────────────────────────────────────────────────────────

@Composable
fun EmptyNotifications(
    onExplore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Box(
            modifier = Modifier.size(120.dp),
            contentAlignment = Alignment.Center,
        ) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(FitColors.Surface),
            )
            Text(text = "🔔", fontSize = 52.sp)
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .align(Alignment.TopEnd)
                    .clip(CircleShape)
                    .background(FitColors.Red),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text       = "0",
                    fontSize   = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color      = Color.White,
                )
            }
        }
        Spacer(Modifier.height(28.dp))
        Text(
            text       = "Nenhuma notificação",
            fontSize   = 18.sp,
            fontWeight = FontWeight.Bold,
            color      = FitColors.TextPrimary,
            textAlign  = TextAlign.Center,
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text       = "Você não tem nenhuma notificação no momento. Avisaremos quando algo novo acontecer!",
            fontSize   = 13.sp,
            color      = FitColors.TextMuted,
            textAlign  = TextAlign.Center,
            lineHeight = 19.sp,
        )
        Spacer(Modifier.height(28.dp))
        Button(
            onClick        = onExplore,
            shape          = RoundedCornerShape(12.dp),
            colors         = ButtonDefaults.buttonColors(
                containerColor = FitColors.Accent,
                contentColor   = FitColors.Bg,
            ),
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 12.dp),
        ) {
            Text(text = "Explorar", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// ── Period header ──────────────────────────────────────────────────────────────

@Composable
private fun PeriodHeader(label: String) {
    Text(
        text       = label,
        fontSize   = 13.sp,
        fontWeight = FontWeight.SemiBold,
        color      = FitColors.TextMuted,
        modifier   = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
    )
}

// ── Notification row ───────────────────────────────────────────────────────────

@Composable
fun NotificationRow(
    notif: NotificationUiModel,
    modifier: Modifier = Modifier,
) {
    println("NotificationRow | recompose | id=${notif.id}")
    val style       = NotificationStyles.from(notif.type)
    val accentColor = notif.dotColor ?: FitColors.TextDisabled
    val actionLabel = notif.type.actionLabel()
    val isUnread    = !notif.isRead

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // ── Circular icon with unread badge
        Box(modifier = Modifier.size(48.dp)) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(notif.iconBg),
                contentAlignment = Alignment.Center,
            ) {
                Text(notif.icon, fontSize = 22.sp)
            }
            if (isUnread && notif.dotColor != null) {
                Box(
                    modifier = Modifier
                        .size(11.dp)
                        .align(Alignment.TopEnd)
                        .clip(CircleShape)
                        .background(FitColors.Bg)
                        .padding(1.5.dp)
                        .clip(CircleShape)
                        .background(accentColor),
                )
            }
        }

        Spacer(Modifier.width(14.dp))

        // ── Content
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = buildAnnotatedString {
                    if (isUnread && notif.dotColor != null) {
                        withStyle(SpanStyle(color = accentColor, fontWeight = FontWeight.Bold)) {
                            append(notif.title)
                        }
                    } else {
                        append(notif.title)
                    }
                },
                fontSize   = 14.sp,
                fontWeight = if (isUnread) FontWeight.SemiBold else FontWeight.Normal,
                color      = FitColors.TextPrimary,
                maxLines   = 1,
                overflow   = TextOverflow.Ellipsis,
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text       = notif.description,
                fontSize   = 12.sp,
                color      = FitColors.TextMuted,
                maxLines   = 2,
                overflow   = TextOverflow.Ellipsis,
                lineHeight = 17.sp,
            )
            Spacer(Modifier.height(6.dp))
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text     = formatRelativeTime(notif.createdAt),
                    fontSize = 11.sp,
                    color    = FitColors.TextDisabled,
                )
                NotificationTypeChip(type = notif.type, style = style)
            }
        }

        // ── Action button
        if (actionLabel != null) {
            Spacer(Modifier.width(10.dp))
            ActionButton(label = actionLabel, color = accentColor)
        }
    }
}

// ── Action button ──────────────────────────────────────────────────────────────

@Composable
private fun ActionButton(label: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.12f))
            .border(1.dp, color.copy(alpha = 0.35f), RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text       = label,
            fontSize   = 11.sp,
            fontWeight = FontWeight.Bold,
            color      = color,
        )
    }
}

// ── Type chip ──────────────────────────────────────────────────────────────────

@Composable
private fun NotificationTypeChip(type: NotificationType, style: NotificationStyle) {
    val label = when (type) {
        NotificationType.XP         -> "#xp"
        NotificationType.STREAK     -> "#streak"
        NotificationType.CURTIDA    -> "#curtida"
        NotificationType.COMENTARIO -> "#comentário"
        NotificationType.TREINO     -> "#treino"
        NotificationType.CONQUISTA  -> "#conquista"
        NotificationType.DESAFIO    -> "#desafio"
        NotificationType.RANKING    -> "#ranking"
        NotificationType.SISTEMA    -> "#sistema"
    }
    val chipColor = style.dotColor ?: FitColors.TextDisabled
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(chipColor.copy(alpha = 0.10f))
            .padding(horizontal = 7.dp, vertical = 2.dp),
    ) {
        Text(
            text       = label,
            fontSize   = 10.sp,
            color      = chipColor,
            fontWeight = FontWeight.Medium,
        )
    }
}