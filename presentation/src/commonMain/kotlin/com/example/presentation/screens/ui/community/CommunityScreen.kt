package com.example.presentation.screens.ui.community

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ModeComment
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.presentation.screens.ui.community.viewmodel.CommunityViewModel


// --- Models simples para a demo ---
// Representação de um post diário de treino/hábito
@Immutable
data class WorkoutPost(
    val id: String,
    val userName: String,
    val imageUrl: String,
    val description: String,
    val timestamp: Long
)

// Definição estrita dos estados da tela
sealed interface CommunityUiState {
    data object Loading : CommunityUiState
    data object NotInGroup : CommunityUiState
    data class InGroup(val squadName: String, val posts: List<WorkoutPost>) : CommunityUiState
    data class Error(val message: String) : CommunityUiState
}

data class CommunityGroup(
    val id: String,
    val name: String,
    val accessCode: String,
    val description: String,
    val memberCount: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommunityScreen(
    viewModel: CommunityViewModel, // O ideal é injetar via Hilt/Koin
    toAddPost: () -> Unit
) {
    // collectAsStateWithLifecycle otimiza o consumo de recursos quando o app vai para background
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is CommunityUiState.Loading -> {
                    CircularProgressIndicator()
                }

                is CommunityUiState.NotInGroup -> {
                    JoinGroupContent(
                        onJoinClick = { code -> viewModel.joinGroup(code) },
                        onCreateClick = { name, desc ->
                            // Chame a função de criação no seu ViewModel
                            viewModel.createGroup(name, desc)
                        }
                    )
                }

                is CommunityUiState.InGroup -> {
                    FeedContent(
                        squadName = state.squadName,
                        posts = state.posts,
                        onLeaveGroup = { viewModel.leaveGroup() }, // Conexão aqui
                        toAddPost = toAddPost
                    )
                }

                is CommunityUiState.Error -> {
                    ErrorStateContent(
                        message = state.message,
                        onRetry = { viewModel.resetToJoin() } // Precisamos criar essa função no ViewModel
                    )
                }
            }
        }
    }
}
@Composable
fun SquadCreatedDialog(
    accessCode: String,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text("Squad Fundado! 🛡️", fontWeight = FontWeight.Black) },
        text = {
            Column {
                Text("Compartilhe o código abaixo com seus parceiros de treino:")
                Spacer(Modifier.height(16.dp))
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = accessCode,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) { Text("BORA TREINAR") }
        }
    )
}
@Composable
private fun JoinGroupContent(
    onJoinClick: (String) -> Unit,
    onCreateClick: (String, String) -> Unit // Nome e Descrição
) {
    var groupCode by remember { mutableStateOf("") }
    var isCreating by remember { mutableStateOf(false) } // Toggle entre entrar/criar

    var newSquadName by remember { mutableStateOf("") }
    var newSquadDesc by remember { mutableStateOf("") }

    val cs = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Ícone de Comunidade Estilizado
        Icon(
            imageVector = Icons.Default.Groups, // Adicione essa importação
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = cs.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (isCreating) "Lidere seu Squad" else "Junte-se à Elite",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Seletor de Modo (Entrar vs Criar)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(cs.surfaceVariant.copy(alpha = 0.3f))
                .padding(4.dp)
        ) {
            val modes = listOf("ENTRAR", "CRIAR")
            modes.forEachIndexed { index, title ->
                val selected = (index == 1 && isCreating) || (index == 0 && !isCreating)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (selected) cs.primary else Color.Transparent)
                        .clickable { isCreating = index == 1 }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        color = if (selected) Color.Black else cs.onSurfaceVariant,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Conteúdo Dinâmico baseado no modo
        if (!isCreating) {
            // MODO ENTRAR
            OutlinedTextField(
                value = groupCode,
                onValueChange = { groupCode = it.uppercase() },
                label = { Text("Código do Squad") },
                placeholder = { Text("Ex: FIT-2024") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { onJoinClick(groupCode) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = groupCode.length >= 4,
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("ENTRAR NO GRUPO", fontWeight = FontWeight.Black)
            }
        } else {
            // MODO CRIAR
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = newSquadName,
                    onValueChange = { newSquadName = it },
                    label = { Text("Nome do Squad") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )

                OutlinedTextField(
                    value = newSquadDesc,
                    onValueChange = { newSquadDesc = it },
                    label = { Text("Objetivo (Ex: Rumo aos 100kg)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { onCreateClick(newSquadName, newSquadDesc) },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    enabled = newSquadName.isNotBlank(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = cs.secondary)
                ) {
                    Text("FUNDAR SQUAD", fontWeight = FontWeight.Black)
                }
            }
        }
    }
}
@Composable
fun LeaveGroupDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Sair do Squad?") },
        text = { Text("Você deixará de ver as postagens diárias e o progresso dos membros deste grupo.") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Sair", color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
@Composable
fun FeedContent(
    squadName: String,
    posts: List<WorkoutPost>,
    toAddPost: () -> Unit,
    onLeaveGroup: () -> Unit // Novo parâmetro
) {
    var showExitDialog by remember { mutableStateOf(false) }

    if (showExitDialog) {
        LeaveGroupDialog(
            onConfirm = {
                showExitDialog = false
                onLeaveGroup()
            },
            onDismiss = { showExitDialog = false }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp), // Espaço para o FAB
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Cabeçalho Estilizado
            item {
                // Passamos o clique para abrir o diálogo
                SquadHeader(
                    name = squadName,
                    onExitClick = { showExitDialog = true }
                )
            }

            // Lista de Posts com Animação de Entrada
            itemsIndexed(
                items = posts,
                key = { _, post -> post.id }
            ) { index, post ->
                var visible by remember { mutableStateOf(false) }
                LaunchedEffect(Unit) { visible = true }

                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(animationSpec = tween(500, delayMillis = index * 100)) +
                            slideInVertically(
                                initialOffsetY = { it / 2 },
                                animationSpec = tween(500, delayMillis = index * 100)
                            )
                ) {
                    PostCard(post = post)
                }
            }
        }

        // Botão flutuante para postar nova foto (Gamificação)
        FloatingActionButton(
            onClick = {
            /* Abrir Câmera */
                toAddPost()
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp),
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ) {
            Icon(Icons.Default.AddAPhoto, contentDescription = "Postar Foto")
        }
    }
}

@Composable
private fun SquadHeader(
    name: String,
    onExitClick: () -> Unit // Novo parâmetro
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        Color.Transparent
                    )
                )
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Squad Ativo",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.ExtraBold
            )
        }

        // Botão discreto, mas acessível para sair
        IconButton(onClick = onExitClick) {
            Icon(
                imageVector = Icons.Filled.Logout,
                contentDescription = "Sair do grupo",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun PostCard(post: WorkoutPost) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column {
            // Header do Post: Usuário e Opções
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar Placeholder
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = post.userName.take(1),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1.0f)) {
                    Text(
                        text = post.userName,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "há 2 horas", // Aqui você usaria uma função de tempo relativo
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(onClick = { }) {
                    Icon(Icons.Default.MoreVert, contentDescription = null)
                }
            }

            // Área da Foto (Onde brilhará o progresso do usuário)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f) // Imagem quadrada estilo Instagram
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            ) {
                // Simulação de imagem - Aqui você usaria o Coil/Glide
                Text(
                    text = "💪 Foto de Progresso",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.labelLarge
                )
            }

            // Ações e Descrição
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(Icons.Outlined.FavoriteBorder, contentDescription = "Curtir")
                    Icon(Icons.Outlined.ModeComment, contentDescription = "Comentar")
                    Icon(Icons.Outlined.Share, contentDescription = "Compartilhar")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = post.description,
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
fun ErrorStateContent(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Ícone visualmente amigável (SearchOff indica que algo não foi encontrado)
        Surface(
            modifier = Modifier.size(120.dp),
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f)
        ) {
            Icon(
                imageVector = Icons.Default.SearchOff,
                contentDescription = null,
                modifier = Modifier.padding(24.dp).fillMaxSize(),
                tint = MaterialTheme.colorScheme.error
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Ops! Squad não encontrado",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Botão de ação principal com cor de erro sutil (Tonal)
        FilledTonalButton(
            onClick = onRetry,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Text("Tentar outro código")
        }

        TextButton(
            onClick = { /* Lógica para falar com suporte ou ver FAQ */ },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Precisa de ajuda?")
        }
    }
}