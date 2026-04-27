package com.example.presentation.screens.ui.leaderboards

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.Composable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.screens.widgets.FitverseTopAppBar

// --- Modelos ---
data class LeaderboardUser(
    val id: String,
    val name: String,
    val xp: Int,
    val rank: Int,
    val isCurrentUser: Boolean = false
)

// Dados falsos
val mockFriendsLeaderboard = listOf(
    LeaderboardUser("1", "Carlos S.", 12400, 1),
    LeaderboardUser("2", "Você", 11200, 2, isCurrentUser = true),
    LeaderboardUser("3", "Ana P.", 10800, 3),
    LeaderboardUser("4", "Marcos V.", 9500, 4),
    LeaderboardUser("5", "Julia C.", 8200, 5),
    LeaderboardUser("6", "Pedro L.", 7100, 6)
)

val mockGlobalLeaderboard = listOf(
    LeaderboardUser("99", "NinjaFit", 45000, 1),
    LeaderboardUser("98", "IronMan", 42000, 2),
    LeaderboardUser("97", "Sarah_W", 41500, 3),
    LeaderboardUser("96", "BeastMode", 39000, 4),
    LeaderboardUser("2", "Você", 11200, 584, isCurrentUser = true)
)



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardsScreen(
    onBack: () -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val cs = MaterialTheme.colorScheme

    val currentList = if (selectedTabIndex == 0) mockGlobalLeaderboard else mockFriendsLeaderboard

    Scaffold(
        topBar = {
            FitverseTopAppBar(
                title = "LEADERBOARDS",
                onBack = onBack,
            )
        },
        containerColor = cs.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // O novo menu de abas aqui
            LeaderboardToggle(
                selectedTabIndex = selectedTabIndex,
                onTabSelected = { selectedTabIndex = it }
            )

            LeaderboardContent(users = currentList)
        }
    }
}

@Composable
fun LeaderboardToggle(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    val cs = MaterialTheme.colorScheme

    // Container da "Pílula"
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .height(50.dp),
        shape = RoundedCornerShape(25.dp),
        color = cs.surface,
        border = BorderStroke(1.dp, cs.outline.copy(alpha = 0.15f))
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val tabs = listOf("GLOBAL", "AMIGOS")
            tabs.forEachIndexed { index, title ->
                val isSelected = selectedTabIndex == index

                // Animação suave de cor de fundo
                val backgroundColor by animateColorAsState(
                    targetValue = if (isSelected) cs.primary else Color.Transparent,
                    label = "tabBackground"
                )
                val contentColor by animateColorAsState(
                    targetValue = if (isSelected) Color.Black else cs.onSurfaceVariant,
                    label = "tabContent"
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(4.dp) // Respiro interno para a pílula selecionada
                        .clip(RoundedCornerShape(21.dp))
                        .background(backgroundColor)
                        .clickable { onTabSelected(index) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Black,
                        color = contentColor,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}
@Composable
fun LeaderboardContent(users: List<LeaderboardUser>) {
    val top3 = users.take(3)
    val remaining = users.drop(3)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Seção do Pódio (Top 3)
        item {
            if (top3.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp, horizontal = 16.dp)
                        .height(220.dp), // Altura fixa para o pódio alinhar pela base
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.Bottom
                ) {
                    // Ordem visual: 2º lugar, 1º lugar, 3º lugar
                    if (top3.size >= 2) PodiumItem(user = top3[1], modifier = Modifier.weight(1f))
                    if (top3.isNotEmpty()) PodiumItem(user = top3[0], modifier = Modifier.weight(1f))
                    if (top3.size >= 3) PodiumItem(user = top3[2], modifier = Modifier.weight(1f))
                }
            }
        }

        // Lista do 4º lugar em diante
        items(remaining) { user ->
            LeaderboardListItem(user = user)
        }
    }
}

@Composable
fun PodiumItem(user: LeaderboardUser, modifier: Modifier = Modifier) {
    // Configuração de cores baseadas no rank (Ouro, Prata, Bronze)
    val (podiumColor, heightFraction, avatarSize) = when (user.rank) {
        1 -> Triple(Color(0xFFFFD700), 1f, 80.dp) // Ouro, mais alto, avatar maior
        2 -> Triple(Color(0xFFC0C0C0), 0.7f, 64.dp) // Prata
        else -> Triple(Color(0xFFCD7F32), 0.5f, 64.dp) // Bronze
    }

    val primaryColor = MaterialTheme.colorScheme.primary

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        // Avatar com Anel Customizado via Canvas
        Box(contentAlignment = Alignment.Center) {
            // Desenha o anel ao redor
            Canvas(modifier = Modifier.size(avatarSize + 8.dp)) {
                drawCircle(
                    brush = Brush.sweepGradient(listOf(podiumColor, podiumColor.copy(alpha = 0.4f), podiumColor)),
                    style = Stroke(width = 4.dp.toPx())
                )
            }

            // Avatar de Iniciais
            Box(
                modifier = Modifier
                    .size(avatarSize)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user.name.take(1).uppercase(),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Badge de Rank
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 8.dp)
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(podiumColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${user.rank}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = user.name,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "${user.xp} XP",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        // O Pedestal Desenhado com Canvas
        Canvas(modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(100.dp * heightFraction)
        ) {
            val cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx())
            drawRoundRect(
                brush = Brush.verticalGradient(
                    colors = listOf(primaryColor.copy(alpha = 0.2f), Color.Transparent)
                ),
                topLeft = Offset(0f, 0f),
                size = size,
                cornerRadius = cornerRadius
            )
            // Linha superior brilhante do pedestal
            drawRoundRect(
                color = podiumColor,
                topLeft = Offset(0f, 0f),
                size = size.copy(height = 4.dp.toPx()),
                cornerRadius = cornerRadius
            )
        }
    }
}

@Composable
fun LeaderboardListItem(user: LeaderboardUser) {
    val cs = MaterialTheme.colorScheme

    val itemBackground = if (user.isCurrentUser) {
        Brush.horizontalGradient(
            colors = listOf(cs.primary.copy(alpha = 0.15f), Color.Transparent)
        )
    } else {
        Brush.linearGradient(colors = listOf(Color.Transparent, Color.Transparent))
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(itemBackground)
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Rank com cor especial se for o usuário
        Text(
            text = "${user.rank}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Black,
            color = if (user.isCurrentUser) cs.primary else cs.onSurfaceVariant,
            modifier = Modifier.width(40.dp)
        )

        // Avatar Circular Minimalista
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(if (user.isCurrentUser) cs.primary else cs.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = user.name.take(1).uppercase(),
                color = if (user.isCurrentUser) Color.Black else cs.onSurfaceVariant,
                fontWeight = FontWeight.Black
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = if (user.isCurrentUser) "VOCÊ" else user.name.uppercase(),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.ExtraBold,
            color = cs.onBackground,
            modifier = Modifier.weight(1f),
            letterSpacing = 0.5.sp
        )

        // XP com a cor Dourada/XP (tertiary)
        Text(
            text = "${user.xp}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Black,
            color = cs.tertiary
        )
        Text(
            text = " XP",
            style = MaterialTheme.typography.labelSmall,
            color = cs.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}