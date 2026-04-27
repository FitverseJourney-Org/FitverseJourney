package com.example.presentation.screens.ui.planWorkout

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.presentation.screens.ui.dashboard.components.SectionHeader
import com.example.presentation.screens.widgets.FitVerseSpacer
import com.example.presentation.screens.widgets.FitverseTopAppBar

@Composable
fun PlanMethodCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    onClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick() },
        color = cs.surfaceVariant.copy(alpha = 0.3f),
        border = BorderStroke(1.dp, cs.outline.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(iconTint.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = cs.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelMedium,
                    color = cs.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun CreatePlanMethodDialog(
    onDismiss: () -> Unit,
    onManualSelected: () -> Unit,
    onAiSelected: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(28.dp),
            color = cs.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "NOVO PLANO DE BATALHA",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = cs.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Como você deseja forjar seu próximo treino?",
                    style = MaterialTheme.typography.bodyMedium,
                    color = cs.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Opção IA
                PlanMethodCard(
                    title = "Geração com IA",
                    subtitle = "Um plano otimizado em segundos.",
                    icon = Icons.Default.AutoAwesome,
                    iconTint = cs.tertiary, // Cor de destaque para a IA
                    onClick = onAiSelected
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Aviso de Privacidade / Uso de Dados (Integrado visualmente abaixo da opção IA)
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = cs.tertiaryContainer.copy(alpha = 0.3f),
                    border = BorderStroke(1.dp, cs.tertiary.copy(alpha = 0.2f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Aviso de IA",
                            tint = cs.tertiary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "A IA utilizará seu histórico, medidas e perfil biocorporal para gerar uma rotina sob medida. Seus dados estão seguros.",
                            style = MaterialTheme.typography.labelSmall,
                            color = cs.onSurfaceVariant,
                            lineHeight = 14.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Opção Manual
                PlanMethodCard(
                    title = "Criação Manual",
                    subtitle = "Você no controle de cada série e repetição.",
                    icon = Icons.Default.Edit,
                    iconTint = cs.primary,
                    onClick = onManualSelected
                )

                Spacer(modifier = Modifier.height(24.dp))

                TextButton(onClick = onDismiss) {
                    Text("CANCELAR", fontWeight = FontWeight.Bold, color = cs.secondary)
                }
            }
        }
    }
}
@Composable
fun NoActivePlanCard(
    onCreateClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    val strokeColor = cs.outline.copy(alpha = 0.3f)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(size = 28.dp))
            .clickable(role = Role.Button){
                onCreateClick()
            },
    ) {
        // Desenha a borda tracejada (Dashed Border) para efeito Sênior
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRoundRect(
                color = strokeColor,
                style = Stroke(
                    width = 2.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                ),
                cornerRadius = CornerRadius(28.dp.toPx())
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.FitnessCenter,
                contentDescription = null,
                tint = cs.onSurfaceVariant.copy(alpha = 0.4f),
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "NENHUM PLANO ATIVO",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                color = cs.onSurfaceVariant
            )

            Text(
                text = "Seu progresso está pausado. Ative um plano abaixo ou crie um novo.",
                style = MaterialTheme.typography.bodySmall,
                color = cs.onSurfaceVariant.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
// --- MODELOS DE DADOS ---
data class WorkoutPlanItem(
    val id: String,
    val title: String,
    val frequency: String,
    val intensity: String, // ex: "Alta", "Moderada"
    val isActive: Boolean,
    val progress: Float // 0.0f a 1.0f
)

data class WorkoutScreenState(
    val objective: String = "Hipertrofia Muscular",
    val experienceLevel: String = "Intermediário",
    val workoutsPerWeek: Int = 5,
    val completedThisWeek: Int = 3,
    val activePlan: WorkoutPlanItem? = null,
    val availablePlans: List<WorkoutPlanItem> = emptyList()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutPlanListScreen(
    state: WorkoutScreenState,
    onBack: () -> Unit,
    onSelectedPlan: () -> Unit,
    // Alterado: onNewPlan agora lida com o tipo escolhido, mas a tela gerencia o dialog
    onNavigateToManualCreation: () -> Unit,
    onNavigateToAiCreation: () -> Unit,
    onActivatePlan: () -> Unit
) {
    val cs = MaterialTheme.colorScheme
    // Controle de estado do Dialog
    var showCreateDialog by remember { mutableStateOf(false) }

    if (showCreateDialog) {
        CreatePlanMethodDialog(
            onDismiss = { showCreateDialog = false },
            onManualSelected = {
                showCreateDialog = false
                onNavigateToManualCreation()
            },
            onAiSelected = {
                showCreateDialog = false
                onNavigateToAiCreation()
            }
        )
    }
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            FitverseTopAppBar(title = "PLANOS DE TREINO", onBack = onBack)
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = cs.primary,
                contentColor = cs.onPrimary,
                shape = RoundedCornerShape(16.dp),
                icon = { Icon(Icons.Default.Add, null, tint = Color.White) },
                text = { Text("NOVO PLANO", fontWeight = FontWeight.Black) }
            )
        },
        containerColor = cs.background
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ){
            item {
                SectionHeader(title = "PLANO ATIVO")
                FitVerseSpacer(vertical = true, value = 12.dp)

                if (state.activePlan != null) {
                    WorkoutOverviewCard(state = state)
                } else {
                    NoActivePlanCard(onCreateClick = { showCreateDialog = true })
                }
            }

            item {
                SectionHeader(title = "Planos Disponiveis")
                FitVerseSpacer(vertical = true, value = 12.dp)
            }

            items(state.availablePlans) { plan ->
                AdaptiveWorkoutPlanCard(plan = plan, onClick = onSelectedPlan)
            }
        }
    }
}

@Composable
fun WorkoutOverviewCard(state: WorkoutScreenState) {
    val cs = MaterialTheme.colorScheme

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = cs.surface.copy(alpha = 0.7f),
        border = BorderStroke(1.dp, cs.outline.copy(alpha = 0.1f))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                // Sênior: Usando weight para garantir que o texto não quebre o layout
                Column(modifier = Modifier.weight(1.2f)) {
                    Text("OBJETIVO ATUAL", style = MaterialTheme.typography.labelSmall, color = cs.secondary)
                    Text(state.objective, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                }

                // Card de Nível Adaptativo
                Surface(
                    modifier = Modifier.weight(0.8f).aspectRatio(2.5f),
                    color = cs.secondary.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(state.experienceLevel.uppercase(), color = cs.secondary, fontSize = 10.sp, fontWeight = FontWeight.Black)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Barra de Frequência Semanal Adaptativa
            Text("FREQUÊNCIA SEMANAL", style = MaterialTheme.typography.labelSmall, color = cs.onSurfaceVariant)
            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth().height(8.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                repeat(state.workoutsPerWeek) { index ->
                    val isDone = index < state.completedThisWeek
                    Box(
                        modifier = Modifier
                            .weight(1f) // Divide o espaço igualmente independente de quantos dias são
                            .fillMaxHeight()
                            .clip(CircleShape)
                            .background(if (isDone) cs.tertiary else cs.outline.copy(alpha = 0.2f))
                    )
                }
            }
        }
    }
}

@Composable
fun AdaptiveWorkoutPlanCard(
    plan: WorkoutPlanItem,
    level: String = "Intermediário", // Adicionado para o exemplo
    objective: String = "Hipertrofia", // Adicionado para o exemplo
    onClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    // Define a opacidade e cores com base no estado ativo
    val contentAlpha = 1f

    Surface(
        modifier = Modifier.fillMaxWidth().heightIn(min = 110.dp), // Altura mínima para acomodar as tags sem quebrar
        shape = RoundedCornerShape(24.dp),
        color = cs.surface.copy(alpha = 0.7f),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
        onClick = onClick,
        tonalElevation = if (plan.isActive) 2.dp else 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Spacer(Modifier.width(16.dp))

            // --- CONTEÚDO CENTRAL ---
            Column(
                modifier = Modifier
                    .weight(1f)
                    .alpha(contentAlpha),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = plan.title.uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 0.5.sp,
                    maxLines = 1
                )

                Spacer(Modifier.height(8.dp))

                // Row de Badges (Nível e Objetivo)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    WorkoutBadge(
                        text = objective,
                        containerColor = cs.tertiaryContainer.copy(alpha = 0.4f),
                        contentColor = cs.onTertiaryContainer
                    )
                    WorkoutBadge(
                        text = level,
                        containerColor = cs.secondaryContainer.copy(alpha = 0.4f),
                        contentColor = cs.onSecondaryContainer
                    )
                }

                Spacer(Modifier.height(4.dp))

                Text(
                    text = plan.frequency,
                    style = MaterialTheme.typography.labelMedium,
                    color = cs.onSurfaceVariant
                )
            }

            // --- ÍCONE DE NAVEGAÇÃO ---
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                modifier = Modifier
                    .size(24.dp)
                    .alpha(if (plan.isActive) 1f else 0.3f),
                tint = cs.primary
            )
        }
    }
}

@Composable
private fun WorkoutBadge(
    text: String,
    containerColor: Color,
    contentColor: Color
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = containerColor
    ) {
        Text(
            text = text.uppercase(),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 9.sp,
                fontWeight = FontWeight.ExtraBold
            ),
            color = contentColor
        )
    }
}