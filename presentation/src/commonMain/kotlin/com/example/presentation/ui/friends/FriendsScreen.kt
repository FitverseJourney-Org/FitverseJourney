package com.example.presentation.ui.friends

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.theme.FVExtension
import com.example.presentation.widgets.FVCard
import com.example.presentation.widgets.FVFilterPill
import com.example.presentation.widgets.FVSectionLabel
import com.example.presentation.widgets.FitverseTopAppBar


private enum class FriendStatus { ONLINE, OFFLINE }
private enum class FriendActivity { TRAINING, NUTRITION, MISSION, IDLE }

private data class Friend(
    val name:      String,
    val id:        String,
    val level:     Int,
    val className: String,
    val classColor: Color,
    val status:    FriendStatus,
    val activity:  FriendActivity  = FriendActivity.IDLE,
    val streak:    Int             = 0,
    val lastSeen:  String          = ""
)

private data class FriendRequest(
    val name:      String,
    val id:        String,
    val level:     Int,
    val className: String,
    val classColor: Color
)

private val FRIENDS = listOf(
    Friend("Lucas M.", "@lucas.m",    18, "Titã",   Color(0xFFFF4D1C), FriendStatus.ONLINE,  FriendActivity.TRAINING,  streak = 14),
    Friend("Ana S.",   "@ana.sage",   14, "Sábio",  Color(0xFF7C6FFF), FriendStatus.ONLINE,  FriendActivity.NUTRITION, streak = 7),
    Friend("Carla R.", "@carla.r",    16, "Nômade", Color(0xFF00C97A), FriendStatus.ONLINE,  FriendActivity.MISSION,   streak = 21),
    Friend("Pedro A.", "@pedro.fit",  9,  "Titã",   Color(0xFFFF4D1C), FriendStatus.OFFLINE, lastSeen = "há 2h"),
    Friend("Julia F.", "@julia.f",    11, "Nômade", Color(0xFF00C97A), FriendStatus.OFFLINE, lastSeen = "há 5h"),
    Friend("Marco V.", "@marco.v",    10, "Sábio",  Color(0xFF7C6FFF), FriendStatus.OFFLINE, lastSeen = "há 1d")
)

private val FRIEND_REQUESTS = listOf(
    FriendRequest("Sofia L.", "@sofia.l",   7, "Nômade", Color(0xFF00C97A)),
    FriendRequest("Bruno T.", "@bruno.t",   8, "Titã",   Color(0xFFFF4D1C))
)

private enum class FriendsTab { FRIENDS, REQUESTS, SUGGESTED, SEARCH }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendsScreen(modifier: Modifier = Modifier, onBack: () -> Unit) {
    var activeTab by remember { mutableStateOf(FriendsTab.FRIENDS) }
    var search    by remember { mutableStateOf("") }
    var dismissed by remember { mutableStateOf(setOf<String>()) }

    Scaffold(
        modifier = modifier,
        containerColor = FVExtension.bg,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            FitverseTopAppBar(
                title = "AMIGOS",
                onBack = onBack,
                subtitle = {
                    Text(
                        text = "${FRIENDS.size} amigos",
                        fontSize = 12.sp,
                        color = FVExtension.textMuted
                    )
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(FVExtension.radiusBtn))
                            .background(FVExtension.primary.copy(0.1f))
                            .border(1.dp, FVExtension.primary.copy(0.3f), RoundedCornerShape(FVExtension.radiusBtn))
                            .clickable { }
                            .padding(horizontal = 12.dp, vertical = 7.dp)
                    ) {
                        Text("+ Adicionar", fontSize = 12.sp, color = FVExtension.primary, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
        ) {
            // ── Search bar
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = FVExtension.margin, vertical = 10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(FVExtension.surface2)
                    .border(1.dp, FVExtension.outline, RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("🔍", fontSize = 14.sp, color = FVExtension.textMuted)
                Spacer(Modifier.width(8.dp))
                BasicTextField(
                    value = search,
                    onValueChange = { search = it },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 13.sp, color = FVExtension.text),
                    decorationBox = { inner ->
                        if (search.isEmpty()) Text("Buscar por nome ou @id...", fontSize = 13.sp, color = FVExtension.textMuted)
                        inner()
                    }
                )
            }

            // ── Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = FVExtension.margin),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                FriendsTab.values().forEach { tab ->
                    val label = when (tab) {
                        FriendsTab.FRIENDS   -> "Amigos"
                        FriendsTab.REQUESTS  -> "Pedidos (${FRIEND_REQUESTS.size - dismissed.size})"
                        FriendsTab.SUGGESTED -> "Sugeridos"
                        FriendsTab.SEARCH    -> "Buscar"
                    }
                    FVFilterPill(label = label, selected = tab == activeTab) { activeTab = tab }
                }
            }

            Spacer(Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                when (activeTab) {
                    FriendsTab.FRIENDS   -> FriendsListTab(friends = FRIENDS)
                    FriendsTab.REQUESTS  -> RequestsTab(
                        requests  = FRIEND_REQUESTS.filter { it.id !in dismissed },
                        onAccept  = { id -> dismissed = dismissed + id },
                        onDecline = { id -> dismissed = dismissed + id }
                    )
                    FriendsTab.SUGGESTED -> SuggestedTab()
                    FriendsTab.SEARCH    -> AddFriendTab(query = search)
                }
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun FriendsListTab(friends: List<Friend>) {
    val online  = friends.filter { it.status == FriendStatus.ONLINE }
    val offline = friends.filter { it.status == FriendStatus.OFFLINE }

    if (online.isNotEmpty()) {
        FVSectionLabel(title = "Online Agora", action = "${online.size}")
        online.forEach { f -> FriendRow(friend = f) }
        Spacer(Modifier.height(8.dp))
    }
    if (offline.isNotEmpty()) {
        FVSectionLabel(title = "Offline")
        offline.forEach { f -> FriendRow(friend = f) }
    }
}

@Composable
private fun FriendRow(friend: Friend) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = FVExtension.margin, vertical = 4.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(FVExtension.surface)
            .border(1.dp, FVExtension.outline, RoundedCornerShape(10.dp))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar + presence dot
        Box(modifier = Modifier.size(42.dp)) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.TopStart)
                    .clip(CircleShape)
                    .background(friend.classColor.copy(0.12f))
                    .border(
                        if (friend.status == FriendStatus.ONLINE) 2.dp else 1.dp,
                        if (friend.status == FriendStatus.ONLINE) friend.classColor.copy(0.5f)
                        else friend.classColor.copy(0.2f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(friend.name.first().toString(), fontSize = 16.sp, color = FVExtension.text)
            }
            // Presence dot
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .align(Alignment.BottomEnd)
                    .clip(CircleShape)
                    .background(FVExtension.bg)
                    .padding(1.5.dp)
                    .clip(CircleShape)
                    .background(
                        if (friend.status == FriendStatus.ONLINE) FVExtension.secondary
                        else Color(0xFF4A4A52)
                    )
            )
        }
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                friend.name,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = FVExtension.text
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(friend.id, fontSize = 10.sp, color = FVExtension.textMuted)
                if (friend.streak > 0) {
                    Spacer(Modifier.width(6.dp))
                    Text("🔥${friend.streak}", fontSize = 10.sp, color = FVExtension.danger)
                }
            }
            if (friend.status == FriendStatus.ONLINE) {
                val actLabel = when (friend.activity) {
                    FriendActivity.TRAINING  -> "Treinando agora"
                    FriendActivity.NUTRITION -> "Na nutrição"
                    FriendActivity.MISSION   -> "Missão ativa"
                    else                     -> "Online"
                }
                val actColor = when (friend.activity) {
                    FriendActivity.TRAINING  -> FVExtension.secondary
                    FriendActivity.NUTRITION -> Color(0xFFF5C518)
                    FriendActivity.MISSION   -> FVExtension.primary
                    else                     -> FVExtension.textMuted
                }
                Text(actLabel, fontSize = 10.sp, color = actColor)
            } else {
                Text("Visto ${friend.lastSeen}", fontSize = 10.sp, color = FVExtension.textMuted)
            }
        }
        // Level badge
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(FVExtension.radiusPill))
                .background(friend.classColor.copy(0.1f))
                .border(1.dp, friend.classColor.copy(0.25f), RoundedCornerShape(FVExtension.radiusPill))
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                "Lv ${friend.level}",
                fontSize = 10.sp,
                color = friend.classColor,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.width(6.dp))
        // Actions button
        Text("⋯", color = FVExtension.textMuted, fontSize = 18.sp)
    }
}

@Composable
private fun RequestsTab(
    requests: List<FriendRequest>,
    onAccept: (String) -> Unit,
    onDecline: (String) -> Unit
) {
    FVSectionLabel("Solicitações Pendentes", action = "${requests.size}")
    if (requests.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("Nenhuma solicitação pendente", color = FVExtension.textMuted, fontSize = 13.sp)
        }
        return
    }
    requests.forEach { req ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = FVExtension.margin, vertical = 4.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(FVExtension.surface)
                .border(1.dp, FVExtension.danger.copy(0.15f), RoundedCornerShape(10.dp))
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(req.classColor.copy(0.1f))
                    .border(1.dp, req.classColor.copy(0.3f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(req.name.first().toString(), fontSize = 15.sp, color = FVExtension.text)
            }
            Spacer(Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(req.name, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = FVExtension.text)
                Text(
                    text = "Nível ${req.level} · ${req.className}",
                    fontSize = 10.sp,
                    color = FVExtension.textMuted
                )
                Text(
                    "Quer ser seu amigo no FitVerse",
                    fontSize = 10.sp,
                    color = FVExtension.textMuted,
                    fontStyle = FontStyle.Italic
                )
            }
            // Accept
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(FVExtension.primary.copy(0.1f))
                    .border(1.dp, FVExtension.primary.copy(0.3f), RoundedCornerShape(8.dp))
                    .clickable { onAccept(req.id) },
                contentAlignment = Alignment.Center
            ) {
                Text("✓", fontSize = 14.sp, color = FVExtension.primary, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.width(6.dp))
            // Decline
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(FVExtension.danger.copy(0.1f))
                    .border(1.dp, FVExtension.danger.copy(0.25f), RoundedCornerShape(8.dp))
                    .clickable { onDecline(req.id) },
                contentAlignment = Alignment.Center
            ) {
                Text("✕", fontSize = 14.sp, color = FVExtension.danger, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun SuggestedTab() {
    FVSectionLabel("Sugestões Para Você")
    val suggested = listOf(
        Triple("Lara C.",  "Nômade", Color(0xFF00C97A)),
        Triple("Felipe M.", "Titã",  Color(0xFFFF4D1C)),
        Triple("Bia S.",   "Sábio",  Color(0xFF7C6FFF)),
        Triple("Thiago R.", "Nômade", Color(0xFF00C97A))
    )
    LazyVerticalGrid(
        columns   = GridCells.Fixed(2),
        modifier  = Modifier
            .fillMaxWidth()
            .height(340.dp)
            .padding(horizontal = FVExtension.margin),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement   = Arrangement.spacedBy(10.dp)
    ) {
        items(suggested) { (name, cls, color) ->
            Column(
                modifier = Modifier
                    .clip(RoundedCornerShape(FVExtension.radius))
                    .background(FVExtension.surface)
                    .border(1.dp, FVExtension.outline, RoundedCornerShape(FVExtension.radius))
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(color.copy(0.1f))
                        .border(1.dp, color.copy(0.3f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(name.first().toString(), fontSize = 20.sp, color = FVExtension.text)
                }
                Spacer(Modifier.height(8.dp))
                Text(name, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = FVExtension.text)
                Text("2 amigos em comum", fontSize = 10.sp, color = FVExtension.textMuted)
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(FVExtension.radiusBtn))
                        .background(FVExtension.primary.copy(0.1f))
                        .border(1.dp, FVExtension.primary.copy(0.3f), RoundedCornerShape(FVExtension.radiusBtn))
                        .clickable { }
                        .padding(vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("+ Adicionar", fontSize = 11.sp, color = FVExtension.primary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun AddFriendTab(query: String) {
    FVSectionLabel("Buscar por @ID")
    Column(modifier = Modifier.padding(horizontal = FVExtension.margin)) {
        FVCard {
            Text(
                "Digite o @id único do usuário para enviar uma solicitação de amizade",
                fontSize = 12.sp,
                color = FVExtension.textMuted,
                lineHeight = 18.sp
            )
            Spacer(Modifier.height(12.dp))
            if (query.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(FVExtension.surface2)
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(FVExtension.primary.copy(0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("?", fontSize = 16.sp, color = FVExtension.primary)
                    }
                    Spacer(Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(query, fontSize = 13.sp, color = FVExtension.text)
                        Text("Procurando usuário...", fontSize = 10.sp, color = FVExtension.textMuted)
                    }
                }
            }
            Spacer(Modifier.height(14.dp))
            // QR option
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(FVExtension.surface2)
                    .border(1.dp, FVExtension.outline, RoundedCornerShape(10.dp))
                    .clickable { }
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("⬛", fontSize = 20.sp)
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Escanear QR Code", fontSize = 13.sp, color = FVExtension.text, fontWeight = FontWeight.Medium)
                    Text("Aponte para o QR de um amigo", fontSize = 11.sp, color = FVExtension.textMuted)
                }
            }
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(FVExtension.surface2)
                    .border(1.dp, FVExtension.outline, RoundedCornerShape(10.dp))
                    .clickable { }
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("🔗", fontSize = 20.sp)
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("Compartilhar meu link", fontSize = 13.sp, color = FVExtension.text, fontWeight = FontWeight.Medium)
                    Text("fitverse.app/@seuid · toque para copiar", fontSize = 11.sp, color = FVExtension.textMuted)
                }
            }
        }
    }
}