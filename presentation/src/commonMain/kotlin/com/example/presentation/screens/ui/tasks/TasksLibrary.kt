package com.example.presentation.screens.ui.tasks


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.LocalDining
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material.icons.rounded.StarOutline
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.domain.models.dashboard.TaskIcon
import com.example.domain.models.dashboard.TaskItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskLibraryScreen(
    taskToReplace: TaskItem,
    libraryTasks: List<TaskItem>,
    onTaskSelected: (TaskItem) -> Unit,
    onBackClick: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    // ESTADO: Armazena qual tarefa da biblioteca foi clicada
    var selectedTask by remember { mutableStateOf<TaskItem?>(null) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Biblioteca de Missões",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            "Substituindo: ${taskToReplace.title}",
                            style = MaterialTheme.typography.labelSmall,
                            color = cs.primary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    // Botão Salvar: habilitado apenas se selectedTask não for nulo
                    IconButton(
                        onClick = { selectedTask?.let { onTaskSelected(it) } },
                        enabled = selectedTask != null
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Save,
                            contentDescription = "Salvar",
                            // Visual dinâmico para o ícone
                            tint = if (selectedTask != null) cs.primary else cs.onSurface.copy(alpha = 0.3f)
                        )
                    }
                },
                windowInsets = WindowInsets(0, 0, 0, 0),
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = cs.surface)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(cs.surface)
        ) {
            _root_ide_package_.com.example.presentation.screens.ui.tasks.ExchangeInfoBanner(cs = cs)

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "Opções Disponíveis",
                        style = MaterialTheme.typography.labelLarge,
                        color = cs.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }

                items(libraryTasks, key = { it.id }) { task ->
                    val isCurrentSelection = selectedTask?.id == task.id

                    _root_ide_package_.com.example.presentation.screens.ui.tasks.LibraryTaskCardPro(
                        task = task,
                        cs = cs,
                        isSelected = isCurrentSelection,
                        onClick = {
                            selectedTask = if (isCurrentSelection) null else task
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ExchangeInfoBanner(cs: ColorScheme) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        color = cs.primaryContainer.copy(alpha = 0.2f),
        border = BorderStroke(1.dp, cs.primary.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(cs.primary, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Rounded.Lightbulb,
                    contentDescription = null,
                    tint = cs.onPrimary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(Modifier.width(16.dp))
            Text(
                text = "Ao selecionar uma nova missão, a anterior será descartada e uma das suas trocas diárias será consumida.",
                style = MaterialTheme.typography.bodySmall,
                color = cs.onSurfaceVariant
            )
        }
    }
}

@Composable
fun LibraryTaskCardPro(
    task: TaskItem,
    cs: ColorScheme,
    isSelected: Boolean, // Novo parâmetro
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        color = cs.surface,
        // Borda destaca o card selecionado
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) cs.primary else cs.outlineVariant.copy(alpha = 0.4f)
        ),
        tonalElevation = if (isSelected) 8.dp else 2.dp
    ) {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(cs.surfaceVariant.copy(alpha = 0.5f)),
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
                    tint = cs.primary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(Modifier.width(16.dp))


            // Textos
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = cs.onSurface
                )

                Text(
                    text = task.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = cs.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(8.dp))

                // Badge de Recompensa dentro do Card
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Rounded.Bolt,
                        contentDescription = null,
                        tint = Color(0xFFFFB300),
                        modifier = Modifier.size(16.dp)
                    )

                    Text(
                        text = "${task.xp} XP",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFFB300)
                    )
                }

            }
            Spacer(Modifier.height(8.dp))
            Icon(
                imageVector = if (isSelected) Icons.Rounded.CheckCircle else Icons.Rounded.AddCircle,
                contentDescription = "Selecionar",
                tint = if (isSelected) cs.primary else cs.primary.copy(alpha = 0.8f),
                modifier = Modifier.size(28.dp)
            )
        }
    }
}


@Composable

fun LibraryTaskCardPro(

    task: TaskItem,

    cs: ColorScheme,

    onClick: () -> Unit

) {

    Surface(

        modifier = Modifier.fillMaxWidth(),

        onClick = onClick,

        shape = RoundedCornerShape(24.dp),

        color = cs.surface,

        border = BorderStroke(1.dp, cs.outlineVariant.copy(alpha = 0.4f)),

        tonalElevation = 2.dp

    ) {

        Row(

            modifier = Modifier.padding(16.dp),

            verticalAlignment = Alignment.CenterVertically

        ) {

// Círculo do Ícone




// Indicador de Seleção

            Icon(

                imageVector = Icons.Rounded.AddCircle,

                contentDescription = "Selecionar",

                tint = cs.primary.copy(alpha = 0.8f),

                modifier = Modifier.size(28.dp)

            )

        }

    }

}