package com.example.presentation.screens.ui.friends

import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.presentation.screens.widgets.FitverseIconBack
import com.example.presentation.screens.widgets.FitverseTopAppBar
import com.example.presentation.theme.PADDING_TOPAPPBAR_DEFAULT_HORIZONTAL
import com.example.presentation.theme.PADDING_TOPAPPBAR_DEFAULT_VERTICAL

data class UserProfile(
    val id: String,
    val name: String,
    val username: String,
    val mutualConnections: Int = 0,
    val isFollowing: Boolean = false,
    val streak: Int = 0 // Elemento de gamificação
)

// Dados falsos para visualização
val mockFriends = listOf(
    UserProfile("1", "Carlos Silva", "@carlos_fit", streak = 12),
    UserProfile("2", "Ana Paula", "@ana.paula", streak = 5),
    UserProfile("3", "Marcos V.", "@marcos_v", streak = 30)
)

val mockSuggestions = listOf(
    UserProfile("4", "Julia Costa", "@juliac", mutualConnections = 4),
    UserProfile("5", "Pedro Lima", "@pedrol", mutualConnections = 2),
    UserProfile("6", "Rafael D.", "@rafael_d", mutualConnections = 8)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendsScreen(
    onBack: () -> Unit,
    friends: List<UserProfile> = mockFriends,
    suggestions: List<UserProfile> = mockSuggestions
) {
    var friendCode by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            FitverseTopAppBar(
                title = "FRIENDS",
                onBack = onBack,
                actions = {
                    IconButton(onClick = {
                        /* TODO: Abrir leitor de QR Code ou Share */
                    }) {
                        Icon(Icons.Default.QrCode, contentDescription = "Meu Código")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Seção 1: Adicionar por Código
            item {
                AddFriendCard(
                    code = friendCode,
                    onCodeChange = { friendCode = it },
                    onAddClick = {
                        /* TODO: Lógica para adicionar amigo */
                    }
                )
            }

            // Seção 2: Sugestões (Amigos em Comum)
            if (suggestions.isNotEmpty()) {
                item {
                    SectionTitle(title = "Pessoas que você talvez conheça")
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(suggestions) { suggestion ->
                            SuggestionCard(user = suggestion)
                        }
                    }
                }
            }

            // Seção 3: Lista de Amigos
            item {
                var mutableString by remember { mutableStateOf("A-Z") }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    SectionTitle(title = "Seus Amigos (${friends.size})")
                    Text(
                        modifier = Modifier
                            .padding(16.dp, 12.dp)
                            .clickable(enabled = true){
                                mutableString = when (mutableString) {
                                    "A-Z" -> "Z-A"
                                    "Z-A" -> "A-Z"
                                    else -> "A-Z"
                                }
                            }
                                .padding(5.dp)
                                .clip(RoundedCornerShape(25)),
                        text = mutableString,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
            items(friends) { friend ->
                FriendListItem(user = friend)
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
            }
        }
    }
}

// --- Componentes Menores para manter o Clean Code ---

@Composable
fun AddFriendCard(code: String, onCodeChange: (String) -> Unit, onAddClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Adicionar conexão",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = code,
                    onValueChange = onCodeChange,
                    placeholder = { Text("Ex: FIT-1234") },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onAddClick,
                    enabled = code.isNotBlank(),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(56.dp) // Alinha com o TextField
                ) {
                    Icon(Icons.Default.PersonAdd, contentDescription = "Adicionar")
                }
            }
        }
    }
}

@Composable
fun SuggestionCard(user: UserProfile) {
    Card(
        modifier = Modifier.width(140.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UserAvatar(initials = user.name.take(1))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = user.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            Text(
                text = "${user.mutualConnections} em comum",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = { /* TODO: Adicionar */ },
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("Adicionar", style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Composable
fun FriendListItem(user: UserProfile) {
    ListItem(
        headlineContent = { Text(user.name, fontWeight = FontWeight.SemiBold) },
        supportingContent = { Text(user.username) },
        leadingContent = { UserAvatar(initials = user.name.take(1)) },
        trailingContent = {
            // Exemplo de elemento gamificado
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = "Streak",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "${user.streak}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.background)
    )
}

@Composable
fun UserAvatar(initials: String) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initials,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(16.dp, 12.dp)
    )
}