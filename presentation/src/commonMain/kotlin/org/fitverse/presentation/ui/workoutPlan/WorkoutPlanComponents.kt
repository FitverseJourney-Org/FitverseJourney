package org.fitverse.presentation.ui.workoutPlan

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.fitverse.presentation.theme.FitColors
import org.fitverse.domain.models.workoutPlan.PlanCreationType
import org.fitverse.domain.models.workoutPlan.ScheduleDay
import org.fitverse.domain.models.workoutPlan.WorkoutPlan

// ─────────────────────────────────────────────────────────
//  ACTIVE PLAN CARD  (featured at top with neon border)
// ─────────────────────────────────────────────────────────

@Composable
fun ActivePlanCard(
    plan: WorkoutPlan,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(WorkoutDimensions.CardCornerRadius))
            .border(
                width = WorkoutDimensions.ActiveBorderWidth,
                color = FitColors.AccentDim,
                shape = RoundedCornerShape(WorkoutDimensions.CardCornerRadius)
            )
            .background(FitColors.Surface)
            .padding(WorkoutDimensions.CardPadding)
    ) {
        Column {
            // ── Badge row ──────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ActiveBadge()
                CreationTypeBadge(plan.creationType)
            }

            Spacer(Modifier.height(12.dp))

            // ── Plan name ──────────────────────────────
            Text(
                text = plan.name,
                color = FitColors.TextPrimary,
                fontSize = 26.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp
            )

            Spacer(Modifier.height(16.dp))

            // ── Stats row ──────────────────────────────
            Row(horizontalArrangement = Arrangement.spacedBy(28.dp)) {
                PlanStat(value = "${plan.daysPerWeek}", label = "DIAS/SEM")
                PlanStat(value = "${plan.exercisesCount}", label = "EXERCÍCIOS")
                PlanStat(
                    value = if (plan.weeksCount == null) "∞" else "${plan.weeksCount}",
                    label = "SEMANAS"
                )
            }

            Spacer(Modifier.height(14.dp))
            HorizontalDivider(color = FitColors.Outline)
            Spacer(Modifier.height(14.dp))

            // ── Schedule ───────────────────────────────
            Text(
                text = "CRONOGRAMA SEMANAL",
                color = FitColors.TextMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.8.sp
            )
            Spacer(Modifier.height(10.dp))
            ScheduleRow(days = plan.scheduleDays)
        }
    }
}

// ─────────────────────────────────────────────────────────
//  INACTIVE PLAN CARD  (in "Meus Planos" list)
// ─────────────────────────────────────────────────────────

@Composable
fun InactivePlanCard(
    plan: WorkoutPlan,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onActivate: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) FitColors.Xp else FitColors.Outline,
        animationSpec = tween(200),
        label = "borderColor"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(WorkoutDimensions.CardCornerRadius))
            .border(
                width = 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(WorkoutDimensions.CardCornerRadius)
            )
            .background(FitColors.Surface)
            .clickable { onSelect() }
            .padding(WorkoutDimensions.CardPadding)
    ) {
        Column {
            // ── Header ─────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = plan.name,
                            color = FitColors.TextPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (plan.creationType == PlanCreationType.AI) AiBadge()
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "${plan.daysPerWeek} dias · ${plan.exercisesCount} exercícios",
                        color = FitColors.TextMuted,
                        fontSize = 13.sp
                    )
                }

                if (isSelected) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(FitColors.Xp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Selecionado",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            ScheduleRow(days = plan.scheduleDays)

            // ── Activate button (visible when selected) ─
            if (isSelected) {
                Spacer(Modifier.height(14.dp))
                Button(
                    onClick = onActivate,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = FitColors.Xp),
                    shape = RoundedCornerShape(WorkoutDimensions.ButtonCornerRadius)
                ) {
                    Text(
                        text = "ATIVAR PLANO",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────
//  NO WORKOUT PLAN CARD  (empty state)
// ─────────────────────────────────────────────────────────

@Composable
fun NoWorkoutPlanCard(
    onCreatePlan: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(WorkoutDimensions.CardCornerRadius))
            .border(1.dp, FitColors.Outline, RoundedCornerShape(WorkoutDimensions.CardCornerRadius))
            .background(FitColors.Surface)
            .padding(vertical = 44.dp, horizontal = 24.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(FitColors.AccentDim)
                .border(1.dp, FitColors.Accent.copy(alpha = 0.3f), CircleShape)
        ) {
            Text(text = "💪", fontSize = 34.sp, textAlign = TextAlign.Center)
        }

        Spacer(Modifier.height(20.dp))

        Text(
            text = "NENHUM PLANO CRIADO",
            color = FitColors.TextPrimary,
            fontSize = 18.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 0.5.sp,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Crie seu primeiro plano de treino\ne comece sua jornada fitness",
            color = FitColors.TextMuted,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(Modifier.height(28.dp))

        Button(
            onClick = onCreatePlan,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = FitColors.Accent),
            shape = RoundedCornerShape(WorkoutDimensions.ButtonCornerRadius)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = FitColors.Bg,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "CRIAR PLANO",
                color = FitColors.Bg,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp,
                fontSize = 15.sp
            )
        }
    }
}

// ─────────────────────────────────────────────────────────
//  CREATE PLAN PLACEHOLDER  (dashed card)
// ─────────────────────────────────────────────────────────

@Composable
fun CreatePlanPlaceholder(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(WorkoutDimensions.CardCornerRadius))
            .border(
                width = 1.dp,
                color = FitColors.Outline,
                shape = RoundedCornerShape(WorkoutDimensions.CardCornerRadius)
            )
            .background(FitColors.Surface)
            .clickable { onClick() }
            .padding(vertical = 28.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(FitColors.AccentDim)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Criar plano",
                    tint = FitColors.Accent,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = "Criar segundo plano",
                color = FitColors.TextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Tenha variações prontas para usar",
                color = FitColors.TextMuted,
                fontSize = 13.sp
            )
        }
    }
}

// ─────────────────────────────────────────────────────────
//  LIMIT REACHED BANNER
// ─────────────────────────────────────────────────────────

@Composable
fun LimitReachedBanner(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF2A1A00))
            .border(1.dp, Color(0xFF4A3000), RoundedCornerShape(10.dp))
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            tint = FitColors.Amber,
            modifier = Modifier.size(16.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = "Limite atingido · exclua um plano",
            color = FitColors.Amber,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

// ─────────────────────────────────────────────────────────
//  BOTTOM ACTION MENU  (floating edit / delete buttons)
// ─────────────────────────────────────────────────────────

@Composable
fun PlanActionBottomMenu(
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.padding(end = 16.dp, bottom = 16.dp)
    ) {
        ActionMenuRow(
            label      = "EDITAR",
            labelColor = FitColors.TextPrimary,
            icon       = Icons.Default.Edit,
            iconBg     = FitColors.Xp,
            onClick    = onEdit
        )
        ActionMenuRow(
            label      = "EXCLUIR",
            labelColor = FitColors.Red,
            icon       = Icons.Default.Delete,
            iconBg     = FitColors.Surface2,
            iconTint   = FitColors.Red,
            onClick    = onDelete
        )
        IconButton(
            onClick = onDismiss,
            modifier = Modifier
                .size(WorkoutDimensions.IconButtonSize)
                .clip(CircleShape)
                .background(FitColors.Surface)
                .border(1.dp, FitColors.Outline, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Fechar menu",
                tint = FitColors.TextPrimary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun ActionMenuRow(
    label: String,
    labelColor: Color,
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color = Color.White,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = label,
            color = labelColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 1.sp
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(WorkoutDimensions.IconButtonSize)
                .clip(CircleShape)
                .background(iconBg)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconTint,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

// ─────────────────────────────────────────────────────────
//  DELETE CONFIRMATION DIALOG
// ─────────────────────────────────────────────────────────

@Composable
fun DeletePlanDialog(
    planName: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor   = FitColors.SurfaceModal,
        shape            = RoundedCornerShape(20.dp),
        title = {
            Text(
                text = "EXCLUIR PLANO?",
                color = FitColors.TextPrimary,
                fontWeight = FontWeight.Black,
                fontSize = 18.sp,
                letterSpacing = 0.5.sp
            )
        },
        text = {
            Text(
                text = "Esta ação não pode ser desfeita.\n\"$planName\" será removido.",
                color = FitColors.TextMuted,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors  = ButtonDefaults.buttonColors(containerColor = FitColors.Surface2),
                shape   = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "CANCELAR",
                    color = FitColors.TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors  = ButtonDefaults.buttonColors(containerColor = FitColors.Red),
                shape   = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "EXCLUIR",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    )
}

// ─────────────────────────────────────────────────────────
//  CREATE PLAN BOTTOM SHEET  (AI vs Manual choice)
// ─────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePlanBottomSheet(
    onCreateWithAI: () -> Unit,
    onCreateManually: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    ModalBottomSheet(
        sheetState    = sheetState,
        onDismissRequest = onDismiss,
        containerColor   = FitColors.Surface,
        scrimColor       = FitColors.Scrim,
        dragHandle = { Box(modifier = Modifier.background(Color.Transparent)) },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 36.dp, top = 45.dp)
            ) {
                Text(
                    text = "COMO CRIAR\nSEU PLANO?",
                    color = FitColors.TextPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    lineHeight = 34.sp
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = "Escolha o método que prefere",
                    color = FitColors.TextMuted,
                    fontSize = 14.sp
                )
                Spacer(Modifier.height(20.dp))

                // ── Card IA (Recomendado) ─────────────────────────────────
                CreateOptionCard(
                    isRecommended = true,
                    iconBg        = FitColors.bgIconIa,
                    icon          = Icons.Default.Star,
                    title         = "CRIAR COM IA",
                    titleColor    = FitColors.Blue,
                    description   = "A IA analisa seu perfil, objetivos e histórico para montar o plano ideal automaticamente.",
                    tags          = listOf("Objetivo", "Nível", "Disponibilidade", "Histórico"),
                    borderColor   = FitColors.PurpleDark,
                    bgColor       = FitColors.XpSurface,
                    onClick       = onCreateWithAI
                )

                Spacer(Modifier.height(12.dp))

                // ── Card Manual ───────────────────────────────────────────
                CreateOptionCard(
                    isRecommended = false,
                    iconBg        = FitColors.GreenSemiDark,
                    icon          = Icons.Default.Edit,
                    title         = "CRIAR MANUALMENTE",
                    titleColor    = FitColors.Accent,
                    description   = "Monte seu plano do zero, escolha os exercícios e personalize cada dia da semana.",
                    tags          = emptyList(),
                    borderColor   = FitColors.GreenDim,
                    bgColor       = FitColors.GreenDark,
                    onClick       = onCreateManually
                )
            }
        }
    )
}

@Composable
private fun CreateOptionCard(
    isRecommended: Boolean,
    iconBg: Color,
    icon: ImageVector,
    title: String,
    titleColor: Color,
    description: String,
    tags: List<String>,
    borderColor: Color,
    bgColor: Color,
    onClick: () -> Unit
) {
    // Pulsação da borda apenas no card IA — sem animação de gradiente
    val infiniteTransition = rememberInfiniteTransition(label = "borderPulse")
    val animBorderColor by infiniteTransition.animateColor(
        initialValue  = FitColors.XpBorder,
        targetValue   = FitColors.Xp,
        animationSpec = infiniteRepeatable(
            animation  = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "borderColor"
    )

    val effectiveBorder = if (isRecommended) animBorderColor else borderColor

    val stroke = BorderStroke(
        width = if(isRecommended) 2.dp else 1.dp,
        color = effectiveBorder,
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(border = stroke, RoundedCornerShape(16.dp))
            .background(bgColor)
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column {
            // ── Cabeçalho: ícone + badge ──────────────────────────
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.Top
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier         = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(iconBg.copy(alpha = 0.8f))
                ) {
                    Icon(
                        imageVector        = icon,
                        contentDescription = null,
                        tint               = Color.White,
                        modifier           = Modifier.size(22.dp)
                    )
                }

                if (isRecommended) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(FitColors.Xp)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text          = "RECOMENDADO",
                            color         = Color.White,
                            fontSize      = 10.sp,
                            fontWeight    = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Título ────────────────────────────────────────────
            Text(
                text          = title,
                color         = titleColor,
                fontSize      = 17.sp,
                fontWeight    = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp
            )

            Spacer(Modifier.height(8.dp))

            // ── Descrição ─────────────────────────────────────────
            Text(
                text       = description,
                color      = FitColors.TextMuted,
                fontSize   = 14.sp,
                lineHeight = 20.sp
            )

            // ── Tags (apenas card IA) ─────────────────────────────
            if (tags.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    tags.forEach { tag ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(FitColors.PurpleDark)
                                .border(1.dp, FitColors.XpBorder, RoundedCornerShape(20.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text       = tag,
                                color      = FitColors.Purple,
                                fontSize   = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────
//  SHARED SMALL COMPONENTS
// ─────────────────────────────────────────────────────────

@Composable
fun ScheduleRow(days: List<ScheduleDay>) {
    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        days.forEach { day -> DayCircle(day = day) }
    }
}

@Composable
fun DayCircle(day: ScheduleDay) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(WorkoutDimensions.DayCircleSize)
            .clip(CircleShape)
            .background(if (day.isTrainingDay) FitColors.Accent else FitColors.Surface3)
    ) {
        if (day.isTrainingDay) {
            Text(
                text = day.label,
                color = FitColors.Bg,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
        } else {
            Text(text = "🔥", fontSize = 14.sp, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun ActiveBadge() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.Transparent)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(text = "⚡", fontSize = 11.sp)
        Text(
            text = "ATIVO",
            color = FitColors.TextPrimary,
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun CreationTypeBadge(type: PlanCreationType) {
    val label = when (type) {
        PlanCreationType.MANUAL -> "MANUAL"
        PlanCreationType.AI     -> "IA"
    }
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .border(
                width = 1.dp,
                color = if (type == PlanCreationType.AI) FitColors.Xp else FitColors.Accent.copy(alpha = 0.4f),
                shape = RoundedCornerShape(20.dp)
            )
            .background(color = if (type == PlanCreationType.AI) FitColors.Xp.copy(alpha = 0.15f) else Color.Transparent)
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = label,
            color = if (type == PlanCreationType.AI) FitColors.Purple else FitColors.Accent,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun AiBadge() {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(FitColors.Xp)
            .padding(horizontal = 7.dp, vertical = 2.dp)
    ) {
        Text(text = "IA", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun PlanStat(value: String, label: String) {
    Column {
        Text(
            text = value,
            color = FitColors.TextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = label,
            color = FitColors.TextMuted,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.5.sp
        )
    }
}
