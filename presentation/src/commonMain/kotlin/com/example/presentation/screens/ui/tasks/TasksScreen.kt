import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Autorenew
import androidx.compose.material.icons.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.LocalDining
import androidx.compose.material.icons.rounded.StarOutline
import androidx.compose.material.icons.rounded.SwapHoriz
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.models.dashboard.TaskIcon
import com.example.domain.models.dashboard.TaskItem
import com.example.presentation.screens.widgets.FitverseTaskItem
import com.example.presentation.screens.widgets.FitverseTopAppBar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    currentTasks: List<TaskItem>,
    swapsRemaining: Int,
    onBack: () -> Unit,
    onToggleTask: (TaskItem) -> Unit,
    onNavigateToLibrary: (TaskItem) -> Unit
) {
    val cs = MaterialTheme.colorScheme
    // Estado para saber qual tarefa está selecionada para troca
    var selectedTaskForSwap by remember { mutableStateOf<TaskItem?>(null) }

    Scaffold(
        topBar = {
            FitverseTopAppBar(
                title = "MINHAS TASKS",
                onBack = onBack,
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = selectedTaskForSwap != null && swapsRemaining > 0,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut()
            ) {
                ExtendedFloatingActionButton(
                    onClick = {
                        selectedTaskForSwap?.let { onNavigateToLibrary(it) }
                    },
                    containerColor = cs.primary,
                    contentColor = cs.onPrimary,
                    shape = RoundedCornerShape(16.dp),
                    icon = { Icon(Icons.Rounded.SwapHoriz, null) },
                    text = { Text("TROCAR MISSÃO", fontWeight = FontWeight.Bold) }
                )
            }
        },
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            item {
                SwapStatusHeader(swapsRemaining = swapsRemaining, cs = cs)
            }

            item {
                Text(
                    "Selecione uma missão abaixo para trocar",
                    style = MaterialTheme.typography.labelMedium,
                    color = cs.onSurfaceVariant,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(currentTasks, key = { it.id }) { task ->
                // Chamada do componente sem o Box externo para evitar conflitos de clique
                FitverseTaskItem(
                    task = task,
                    isSelected = selectedTaskForSwap?.id == task.id,
                    onSelect = {
                        // Só permite selecionar para troca se não estiver completada
                        if (!task.completed) {
                            selectedTaskForSwap =
                                if (selectedTaskForSwap?.id == task.id) null else task
                        }
                    },
                    onToggle = {
                        onToggleTask(task)
                        // Se completar a tarefa, desmarca ela da seleção de troca
                        if (selectedTaskForSwap?.id == task.id) selectedTaskForSwap = null
                    }
                )
            }
        }
    }
}

/* ========================================================================== */
/* COMPONENTES SECUNDÁRIOS                                                    */
/* ========================================================================== */

@Composable
fun SwapStatusHeader(swapsRemaining: Int, cs: ColorScheme) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = if (swapsRemaining > 0) cs.primaryContainer.copy(alpha = 0.4f) else cs.surfaceVariant.copy(
            alpha = 0.4f
        ),
        border = BorderStroke(
            1.dp,
            if (swapsRemaining > 0) cs.primary.copy(alpha = 0.2f) else cs.outline.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Rounded.Autorenew,
                    contentDescription = null,
                    tint = if (swapsRemaining > 0) cs.primary else cs.onSurfaceVariant
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Trocas Disponíveis",
                        fontWeight = FontWeight.Bold,
                        color = if (swapsRemaining > 0) cs.onSurface else cs.onSurfaceVariant
                    )
                    Text(
                        text = "Substitua missões que não deseja fazer",
                        fontSize = 12.sp,
                        color = cs.onSurfaceVariant
                    )
                }
            }
            Surface(
                shape = CircleShape,
                color = if (swapsRemaining > 0) cs.primary else cs.surfaceVariant
            ) {
                Text(
                    text = "$swapsRemaining / 2",
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                    fontWeight = FontWeight.Black,
                    color = if (swapsRemaining > 0) cs.onPrimary else cs.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun TaskLibraryContent(
    libraryTasks: List<TaskItem>,
    onTaskSelected: (TaskItem) -> Unit,
    cs: ColorScheme
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .padding(bottom = 40.dp, top = 8.dp)
    ) {
        Text(
            text = "Biblioteca de Missões",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Black,
            color = cs.onSurface
        )
        Text(
            text = "Escolha uma nova missão para o seu dia",
            style = MaterialTheme.typography.bodyMedium,
            color = cs.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (libraryTasks.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Nenhuma missão extra disponível.", color = cs.outline)
            }
        } else {
            libraryTasks.forEach { task ->
                LibraryTaskCard(task = task, onClick = { onTaskSelected(task) }, cs = cs)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun LibraryTaskCard(task: TaskItem, onClick: () -> Unit, cs: ColorScheme) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = cs.surface,
        border = BorderStroke(1.dp, cs.outline.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(cs.secondaryContainer.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (task.iconType) {
                        TaskIcon.WORKOUT -> Icons.Rounded.FitnessCenter
                        TaskIcon.NUTRITION -> Icons.Rounded.LocalDining
                        TaskIcon.RUN -> Icons.Rounded.DirectionsRun
                        else -> Icons.Rounded.StarOutline
                    },
                    contentDescription = null,
                    tint = cs.secondary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    task.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = cs.onSurface
                )
                if (task.description.isNotBlank()) {
                    Text(task.description, fontSize = 12.sp, color = cs.onSurfaceVariant)
                }
            }

            Spacer(Modifier.width(12.dp))

            Text(
                text = "+${task.xp} XP",
                color = cs.secondary,
                fontWeight = FontWeight.Black,
                fontSize = 13.sp
            )
        }
    }
}



