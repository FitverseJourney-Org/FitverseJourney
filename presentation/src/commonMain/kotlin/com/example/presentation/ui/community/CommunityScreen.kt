package com.example.presentation.ui.community

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Forum
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.LockOpen
import androidx.compose.material.icons.rounded.VpnKey
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.collectAsState
import com.example.presentation.theme.FitverseColors
import com.example.presentation.ui.community.viewmodel.CommunityIntent
import com.example.presentation.ui.community.viewmodel.CommunityViewModel
import com.example.presentation.ui.dashboard.components.SectionHeader
import com.example.presentation.ui.workout.FitChip

// ── Data ──────────────────────────────────────────────────────────────────────

data class Community(
    val name: String,
    val accentColor: Color,
    val memberCount: Int,
    val isAdmin: Boolean = false,
)

data class Post(
    val initials: String,
    val initialsColor: Color,
    val user: String,
    val communityName: String,
    val time: String,
    val tag: String,
    val tagColor: Color,
    val content: String,
    val likes: Int,
    val comments: Int,
    val xp: String,
)

internal val myCommunities = listOf(
    Community("Warriors GYM", FitverseColors.Accent,  128, isAdmin = true),
    Community("Dieta Flex",   FitverseColors.Blue,     44),
    Community("Cardio Club",  FitverseColors.Orange,   17),
)

internal val samplePosts = listOf(
    Post("LF", FitverseColors.Accent,  "Luna Fitness",  "Warriors GYM", "12min atrás", "TREINO",    FitverseColors.Accent,
        "Treino de pernas destruído! +180 XP ganhos hoje. Quem mais tá na missão de legs?", 34, 8,  "+180XP"),
    Post("MP", FitverseColors.Purple, "Marcos Power",  "Dieta Flex",   "1h atrás",    "CONQUISTA", FitverseColors.Purple,
        "Novo recorde pessoal no supino: 120kg! Level UP alcançado!", 72, 21, "+250XP"),
    Post("SH", FitverseColors.Blue,   "Sofia Healthy", "Cardio Club",  "3h atrás",    "NUTRIÇÃO",  FitverseColors.Blue,
        "Café da manhã pré-treino. Proteínas, carbos complexos e muita água. Prontos pro dia!", 28, 5, "+48XP"),
    Post("PS", FitverseColors.Orange, "Pedro Strong",  "Warriors GYM", "5h atrás",    "TREINO",    FitverseColors.Accent,
        "Semana 4 do programa concluída! Sensação incrível de evolução.", 55, 14, "+120XP"),
)

// ── Root — wires ViewModel ────────────────────────────────────────────────────

@Composable
fun CommunityRoot(
    viewModel:         CommunityViewModel,
    toAddPost:         () -> Unit,
    onSheetStateChange: (Boolean) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                com.example.presentation.ui.community.viewmodel.CommunityEvent.NavigateToAddPost -> toAddPost()
            }
        }
    }

    CommunityScreen(
        communities        = uiState.communities,
        posts              = uiState.posts,
        showCreateSheet    = uiState.showCreateSheet,
        showJoinSheet      = uiState.showJoinSheet,
        onAddPost          = { viewModel.navigateToAddPost() },
        onShowCreateSheet  = { viewModel.onIntent(CommunityIntent.ShowCreateSheet) },
        onShowJoinSheet    = { viewModel.onIntent(CommunityIntent.ShowJoinSheet) },
        onDismissSheets    = { viewModel.onIntent(CommunityIntent.DismissSheets) },
        onSheetStateChange = onSheetStateChange,
    )
}

// ── Screen ────────────────────────────────────────────────────────────────────

@Composable
fun CommunityScreen(
    communities:        List<Community>    = myCommunities,
    posts:              List<Post>         = samplePosts,
    showCreateSheet:    Boolean            = false,
    showJoinSheet:      Boolean            = false,
    onAddPost:          () -> Unit         = {},
    onShowCreateSheet:  () -> Unit         = {},
    onShowJoinSheet:    () -> Unit         = {},
    onDismissSheets:    () -> Unit         = {},
    onSheetStateChange: (Boolean) -> Unit  = {},
) {
    LaunchedEffect(showCreateSheet, showJoinSheet) {
        onSheetStateChange(showCreateSheet || showJoinSheet)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier            = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            containerColor      = Color.Transparent,
        ) { _ ->
            LazyColumn(
                modifier       = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 32.dp),
            ) {
                // ── Header ────────────────────────────────────────────────
                item {
                    CommunityHeader(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
                    )
                }

                // ── Action cards ──────────────────────────────────────────
                item {
                    Row(
                        modifier              = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        CommunityActionCard(
                            modifier    = Modifier.weight(1f),
                            icon        = Icons.Rounded.Groups,
                            title       = "Criar",
                            description = "Monte seu grupo de atletas",
                            badgeLabel  = "NOVO GRUPO",
                            accentColor = FitverseColors.Purple,
                            onClick     = onShowCreateSheet,
                        )
                        CommunityActionCard(
                            modifier    = Modifier.weight(1f),
                            icon        = Icons.Rounded.VpnKey,
                            title       = "Entrar",
                            description = "Use um código ou senha",
                            badgeLabel  = "COM CÓDIGO",
                            accentColor = FitverseColors.Green,
                            onClick     = onShowJoinSheet,
                        )
                    }
                }

                // ── My communities ────────────────────────────────────────
                item { Spacer(Modifier.height(28.dp)) }
                item {
                    Column {
                        Box(Modifier.padding(horizontal = 16.dp)) {
                            SectionHeader(
                                title      = "MINHAS COMUNIDADES",
                                actionText = "VER TODAS",
                            )
                        }
                        Spacer(Modifier.height(4.dp))
                        LazyRow(
                            contentPadding        = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(14.dp),
                        ) {
                            items(communities) { community ->
                                CommunityAvatar(community = community)
                            }
                            item { AddCommunityAvatar(onClick = onShowCreateSheet) }
                        }
                    }
                }

                // ── Feed ──────────────────────────────────────────────────
                item { Spacer(Modifier.height(28.dp)) }
                item {
                    Box(Modifier.padding(horizontal = 16.dp)) {
                        SectionHeader(
                            title          = "FEED SOCIAL",
                            actionText     = "+ POSTAR",
                            onActionClick  = onAddPost,
                        )
                    }
                }
                items(posts) { post ->
                    PostCard(
                        post     = post,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 5.dp),
                    )
                }
            }
        }

        // ── Bottom sheets ─────────────────────────────────────────────────
        if (showCreateSheet) {
            CreateCommunitySheet(onDismiss = onDismissSheets)
        }
        if (showJoinSheet) {
            JoinCommunitySheet(onDismiss = onDismissSheets)
        }
    }
}

// ── Header ────────────────────────────────────────────────────────────────────

@Composable
private fun CommunityHeader(modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            "SOCIAL",
            color         = FitverseColors.TextMuted,
            fontSize      = 11.sp,
            fontWeight    = FontWeight.Medium,
            letterSpacing = 1.5.sp,
        )
        Text(
            "COMUNIDADE",
            color         = FitverseColors.TextPrimary,
            fontSize      = 28.sp,
            fontWeight    = FontWeight.Black,
            letterSpacing = (-0.5).sp,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FitChip("3 GRUPOS",    FitverseColors.AccentDim,  textColor = FitverseColors.Accent)
            FitChip("317 MEMBROS", FitverseColors.PurpleDim,  textColor = FitverseColors.Purple)
        }
    }
}

// ── Action Card ───────────────────────────────────────────────────────────────

@Composable
private fun CommunityActionCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    description: String,
    badgeLabel: String,
    accentColor: Color,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .border(1.dp, accentColor.copy(alpha = 0.25f), RoundedCornerShape(20.dp))
            .background(FitverseColors.SurfaceCard)
            .clickable(onClick = onClick),
    ) {
        // Accent gradient top strip
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color.Transparent,
                            accentColor.copy(alpha = 0.5f),
                            accentColor,
                            accentColor.copy(alpha = 0.5f),
                            Color.Transparent,
                        )
                    )
                )
        )

        Column(modifier = Modifier.padding(16.dp)) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(accentColor.copy(alpha = 0.10f))
                    .border(1.dp, accentColor.copy(alpha = 0.25f), RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector        = icon,
                    contentDescription = null,
                    tint               = accentColor,
                    modifier           = Modifier.size(24.dp),
                )
            }

            Spacer(Modifier.height(14.dp))

            Text(
                title,
                color         = accentColor,
                fontSize      = 18.sp,
                fontWeight    = FontWeight.Black,
                letterSpacing = 0.5.sp,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                description,
                color      = FitverseColors.TextMuted,
                fontSize   = 11.sp,
                lineHeight = 16.sp,
            )
            Spacer(Modifier.height(14.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(accentColor.copy(alpha = 0.12f))
                    .padding(horizontal = 8.dp, vertical = 3.dp),
            ) {
                Text(
                    badgeLabel,
                    color         = accentColor,
                    fontSize      = 10.sp,
                    fontWeight    = FontWeight.Bold,
                    letterSpacing = 0.5.sp,
                )
            }
        }
    }
}

// ── My Communities ────────────────────────────────────────────────────────────

@Composable
private fun CommunityAvatar(community: Community) {
    val initials = community.name.split(" ").take(2).joinToString("") { it.first().toString() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(20.dp))
                .border(1.5.dp, community.accentColor.copy(alpha = 0.45f), RoundedCornerShape(20.dp))
                .background(community.accentColor.copy(alpha = 0.10f)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                initials,
                color      = community.accentColor,
                fontSize   = 20.sp,
                fontWeight = FontWeight.Black,
            )
            if (community.isAdmin) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(community.accentColor),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        Icons.Rounded.Check,
                        contentDescription = null,
                        tint     = FitverseColors.Bg,
                        modifier = Modifier.size(10.dp),
                    )
                }
            }
        }
        Text(
            community.name.split(" ").first(),
            color     = FitverseColors.TextMuted,
            fontSize  = 11.sp,
            textAlign = TextAlign.Center,
        )
        Text(
            "${community.memberCount} membros",
            color      = community.accentColor,
            fontSize   = 10.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}

@Composable
private fun AddCommunityAvatar(onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(20.dp))
                .border(1.dp, Color(0xFF2a2a35), RoundedCornerShape(20.dp))
                .background(FitverseColors.Surface2)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Nova comunidade",
                tint     = FitverseColors.TextMuted,
                modifier = Modifier.size(24.dp),
            )
        }
        Text("Nova",   color = FitverseColors.TextMuted,  fontSize = 11.sp)
        Text("Criar",  color = FitverseColors.TextMuted2, fontSize = 10.sp)
    }
}

// ── Post card ─────────────────────────────────────────────────────────────────

@Composable
fun PostCard(post: Post, modifier: Modifier = Modifier) {
    val cs = MaterialTheme.colorScheme

    Column(
        modifier = modifier
            .border(1.dp, Color(0xFF2a2a35), RoundedCornerShape(20.dp))
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(cs.surface),
    ) {
        // Gradient top strip keyed by tag color
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(2.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color.Transparent,
                            post.tagColor.copy(alpha = 0.5f),
                            post.tagColor.copy(alpha = 0.85f),
                            post.tagColor.copy(alpha = 0.5f),
                            Color.Transparent,
                        )
                    )
                )
        )

        Column(modifier = Modifier.padding(16.dp)) {
            // ── Header ────────────────────────────────────────────
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(post.initialsColor.copy(alpha = 0.12f))
                        .border(1.dp, post.initialsColor.copy(alpha = 0.30f), CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        post.initials,
                        color      = post.initialsColor,
                        fontSize   = 14.sp,
                        fontWeight = FontWeight.Black,
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(Modifier.weight(1f)) {
                    Text(
                        post.user,
                        color      = FitverseColors.TextPrimary,
                        fontSize   = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Row(
                        verticalAlignment     = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Icon(
                            Icons.Rounded.Groups,
                            contentDescription = null,
                            tint     = FitverseColors.TextMuted2,
                            modifier = Modifier.size(11.dp),
                        )
                        Text(post.communityName, color = FitverseColors.TextMuted2, fontSize = 11.sp)
                        Text("·",                color = FitverseColors.TextMuted2, fontSize = 11.sp)
                        Text(post.time,           color = FitverseColors.TextMuted2, fontSize = 11.sp)
                    }
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(post.tagColor.copy(alpha = 0.15f))
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                ) {
                    Text(
                        post.tag,
                        color         = post.tagColor,
                        fontSize      = 10.sp,
                        fontWeight    = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                    )
                }
            }

            // ── Content ───────────────────────────────────────────
            Spacer(Modifier.height(12.dp))
            Text(
                post.content,
                color      = FitverseColors.TextPrimary.copy(alpha = 0.9f),
                fontSize   = 14.sp,
                lineHeight = 21.sp,
            )

            // ── Footer ────────────────────────────────────────────
            Spacer(Modifier.height(14.dp))
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment     = Alignment.CenterVertically,
                ) {
                    PostStat(icon = Icons.Rounded.FavoriteBorder, count = post.likes,    tint = FitverseColors.Red.copy(alpha = 0.75f))
                    PostStat(icon = Icons.Rounded.Forum,          count = post.comments, tint = FitverseColors.TextMuted)
                }
                Spacer(Modifier.weight(1f))
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(FitverseColors.AccentDim.copy(alpha = 0.20f))
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Icon(
                        Icons.Rounded.EmojiEvents,
                        contentDescription = null,
                        tint     = FitverseColors.Accent,
                        modifier = Modifier.size(13.dp),
                    )
                    Text(post.xp, color = FitverseColors.Accent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun PostStat(icon: ImageVector, count: Int, tint: Color) {
    Row(
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(15.dp))
        Text("$count", color = FitverseColors.TextMuted, fontSize = 13.sp)
    }
}

// ── Create community sheet ────────────────────────────────────────────────────

@Composable
private fun CreateCommunitySheet(onDismiss: () -> Unit) {
    var communityName by remember { mutableStateOf("") }
    var isPrivate     by remember { mutableStateOf(false) }
    val isValid = communityName.isNotBlank()

    CommunityBottomSheet(onDismiss = onDismiss) {
        Text(
            "CRIAR COMUNIDADE",
            color         = FitverseColors.TextPrimary,
            fontSize      = 17.sp,
            fontWeight    = FontWeight.Black,
            letterSpacing = 0.5.sp,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            "Monte seu grupo de atletas e evolua junto",
            color    = FitverseColors.TextMuted,
            fontSize = 13.sp,
        )

        Spacer(Modifier.height(24.dp))

        CommunityTextField(
            value         = communityName,
            onValueChange = { communityName = it },
            label         = "Nome da comunidade",
            placeholder   = "Ex: Warriors GYM",
        )

        Spacer(Modifier.height(20.dp))

        Text(
            "VISIBILIDADE",
            color         = FitverseColors.TextMuted,
            fontSize      = 10.sp,
            fontWeight    = FontWeight.Bold,
            letterSpacing = 1.2.sp,
        )
        Spacer(Modifier.height(10.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            PrivacyOption(
                modifier    = Modifier.weight(1f),
                icon        = Icons.Rounded.LockOpen,
                label       = "Pública",
                description = "Qualquer atleta pode entrar",
                isSelected  = !isPrivate,
                color       = FitverseColors.Green,
                onClick     = { isPrivate = false },
            )
            PrivacyOption(
                modifier    = Modifier.weight(1f),
                icon        = Icons.Rounded.Lock,
                label       = "Privada",
                description = "Apenas por convite ou código",
                isSelected  = isPrivate,
                color       = FitverseColors.Purple,
                onClick     = { isPrivate = true },
            )
        }

        Spacer(Modifier.height(28.dp))

        Button(
            onClick  = onDismiss,
            enabled  = isValid,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape    = RoundedCornerShape(16.dp),
            colors   = ButtonDefaults.buttonColors(
                containerColor         = FitverseColors.Purple,
                contentColor           = FitverseColors.TextPrimary,
                disabledContainerColor = FitverseColors.Surface2,
                disabledContentColor   = FitverseColors.TextMuted2,
            ),
        ) {
            Icon(Icons.Rounded.Groups, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text("CRIAR COMUNIDADE", fontSize = 15.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
        }

        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun PrivacyOption(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String,
    description: String,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .border(
                width = 1.dp,
                color = if (isSelected) color.copy(alpha = 0.50f) else Color(0xFF2a2a35),
                shape = RoundedCornerShape(14.dp),
            )
            .background(if (isSelected) color.copy(alpha = 0.10f) else FitverseColors.SurfaceCard)
            .clickable(onClick = onClick)
            .padding(14.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                icon,
                contentDescription = null,
                tint     = if (isSelected) color else FitverseColors.TextMuted,
                modifier = Modifier.size(16.dp),
            )
            Spacer(Modifier.width(6.dp))
            Text(
                label,
                color      = if (isSelected) color else FitverseColors.TextMuted,
                fontSize   = 13.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            )
        }
        Spacer(Modifier.height(5.dp))
        Text(
            description,
            color      = FitverseColors.TextMuted2,
            fontSize   = 11.sp,
            lineHeight = 15.sp,
        )
    }
}

// ── Join community sheet ──────────────────────────────────────────────────────

@Composable
private fun JoinCommunitySheet(onDismiss: () -> Unit) {
    var code by remember { mutableStateOf("") }
    val isValid = code.isNotBlank()

    CommunityBottomSheet(onDismiss = onDismiss) {
        Text(
            "ENTRAR NA COMUNIDADE",
            color         = FitverseColors.TextPrimary,
            fontSize      = 17.sp,
            fontWeight    = FontWeight.Black,
            letterSpacing = 0.5.sp,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            "Insira o código ou senha fornecido pelo administrador",
            color      = FitverseColors.TextMuted,
            fontSize   = 13.sp,
            lineHeight = 18.sp,
        )

        Spacer(Modifier.height(24.dp))

        CommunityTextField(
            value           = code,
            onValueChange   = { if (it.length <= 12) code = it.uppercase() },
            label           = "Código ou senha",
            placeholder     = "Ex: FIT-2024",
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Characters),
            accentColor     = FitverseColors.Green,
        )

        Spacer(Modifier.height(8.dp))
        Text(
            "Solicite o código ao administrador da comunidade",
            color    = FitverseColors.TextMuted2,
            fontSize = 11.sp,
        )

        Spacer(Modifier.height(28.dp))

        Button(
            onClick  = onDismiss,
            enabled  = isValid,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape    = RoundedCornerShape(16.dp),
            colors   = ButtonDefaults.buttonColors(
                containerColor         = FitverseColors.Green,
                contentColor           = FitverseColors.Bg,
                disabledContainerColor = FitverseColors.Surface2,
                disabledContentColor   = FitverseColors.TextMuted2,
            ),
        ) {
            Icon(Icons.Rounded.VpnKey, contentDescription = null, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(8.dp))
            Text("ENTRAR NA COMUNIDADE", fontSize = 15.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
        }

        Spacer(Modifier.height(8.dp))
    }
}

// ── Shared composables ────────────────────────────────────────────────────────

@Composable
private fun CommunityBottomSheet(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.60f))
            .clickable(
                indication        = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick           = onDismiss,
            ),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    indication        = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick           = {},
                )
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(FitverseColors.Surface)
                .padding(horizontal = 20.dp, vertical = 24.dp),
        ) {
            Box(
                modifier = Modifier
                    .width(36.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(FitverseColors.Border2)
                    .align(Alignment.CenterHorizontally),
            )
            Spacer(Modifier.height(20.dp))
            content()
        }
    }
}

@Composable
private fun CommunityTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    accentColor: Color = FitverseColors.Accent,
    keyboardOptions: KeyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
) {
    OutlinedTextField(
        value           = value,
        onValueChange   = onValueChange,
        label           = { Text(label, fontSize = 12.sp) },
        placeholder     = { Text(placeholder, color = FitverseColors.TextMuted2, fontSize = 14.sp) },
        modifier        = modifier.fillMaxWidth(),
        singleLine      = true,
        shape           = RoundedCornerShape(14.dp),
        keyboardOptions = keyboardOptions,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor   = accentColor,
            unfocusedBorderColor = Color(0xFF2a2a35),
            focusedLabelColor    = accentColor,
            unfocusedLabelColor  = FitverseColors.TextMuted,
            cursorColor          = accentColor,
            focusedTextColor     = FitverseColors.TextPrimary,
            unfocusedTextColor   = FitverseColors.TextPrimary,
        ),
    )
}
