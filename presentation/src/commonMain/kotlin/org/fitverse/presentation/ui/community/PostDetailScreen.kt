package org.fitverse.presentation.ui.community

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Forum
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.fitverse.presentation.theme.FitColors

// ── Unified detail model ──────────────────────────────────────────────────────

data class PostDetailModel(
    val id: String,
    val authorInitials: String,
    val authorColor: Color,
    val authorName: String,
    val communityName: String,
    val timeAgo: String,
    val tag: String,
    val tagColor: Color,
    val content: String,
    val likes: Int,
    val comments: Int,
    val xp: String,
    val hasImage: Boolean = false,
    val isLiked: Boolean = false,
)

data class CommentItem(
    val id: String,
    val authorInitials: String,
    val authorColor: Color,
    val authorName: String,
    val text: String,
    val timeAgo: String,
    val likes: Int,
)

// ── Model converters ──────────────────────────────────────────────────────────

fun Post.toDetailModel() = PostDetailModel(
    id             = "${user}_$time",
    authorInitials = initials,
    authorColor    = initialsColor,
    authorName     = user,
    communityName  = communityName,
    timeAgo        = time,
    tag            = tag,
    tagColor       = tagColor,
    content        = content,
    likes          = likes,
    comments       = comments,
    xp             = xp,
)

fun GroupPost.toDetailModel() = PostDetailModel(
    id             = id,
    authorInitials = authorInitials,
    authorColor    = authorColor,
    authorName     = authorName,
    communityName  = "",
    timeAgo        = timeAgo,
    tag            = tag,
    tagColor       = tagColor,
    content        = content,
    likes          = likes,
    comments       = comments,
    xp             = xpGained,
    hasImage       = hasImage,
    isLiked        = isLiked,
)

// ── Mock comments ─────────────────────────────────────────────────────────────

private val mockComments = listOf(
    CommentItem("c1", "AR", FitColors.Orange, "Alex Runner",   "Arrasando demais! Inspiração total 💪",       "2min",  3),
    CommentItem("c2", "MG", FitColors.Purple, "Mari Gains",    "Que evolução! Continua assim warrior 🏆",     "8min",  7),
    CommentItem("c3", "TS", FitColors.Blue,   "Thiago Strong", "Metas! Qual programa você tá seguindo?",      "15min", 2),
    CommentItem("c4", "JF", FitColors.Green,  "Julia Fit",     "Sem dor, sem ganho! 🔥 Vamo que vamo",        "32min", 5),
    CommentItem("c5", "RD", FitColors.Accent, "Rafa Dev",      "Community goals! Que grupo incrível esse 🏆", "1h",    11),
)

// ── Screen ────────────────────────────────────────────────────────────────────

@Composable
fun PostDetailScreen(
    post:     PostDetailModel,
    onBack:   () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isLiked     by remember { mutableStateOf(post.isLiked) }
    var likesCount  by remember { mutableStateOf(post.likes) }
    var commentText by remember { mutableStateOf("") }

    val heartColor by animateColorAsState(
        targetValue   = if (isLiked) FitColors.Red else FitColors.TextMuted,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label         = "heart_color",
    )

    Scaffold(
        modifier            = modifier.fillMaxSize(),
        containerColor      = Color.Transparent,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            CommentInputBar(
                text         = commentText,
                onTextChange = { commentText = it },
                onSend       = { commentText = "" },
                accentColor  = post.tagColor,
            )
        },
    ) { padding ->
        LazyColumn(
            modifier       = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(bottom = 24.dp),
        ) {
            item {
                PostDetailHero(
                    post       = post,
                    isLiked    = isLiked,
                    likesCount = likesCount,
                    heartColor = heartColor,
                    onBack     = onBack,
                    onLike     = {
                        isLiked = !isLiked
                        likesCount += if (isLiked) 1 else -1
                    },
                    onShare = {},
                )
            }

            item {
                CommentsHeader(
                    count    = post.comments,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                )
            }

            items(mockComments, key = { it.id }) { comment ->
                CommentItemCard(
                    comment  = comment,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                )
            }
        }
    }
}

// ── Post hero (header card) ───────────────────────────────────────────────────

@Composable
private fun PostDetailHero(
    post:       PostDetailModel,
    isLiked:    Boolean,
    likesCount: Int,
    heartColor: Color,
    onBack:     () -> Unit,
    onLike:     () -> Unit,
    onShare:    () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            .background(FitColors.Surface)
            .border(
                width = 0.5.dp,
                color = post.tagColor.copy(alpha = 0.12f),
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
            ),
    ) {
        // Gradient accent strip
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(3.dp)
                .background(
                    Brush.horizontalGradient(
                        listOf(
                            Color.Transparent,
                            post.tagColor.copy(alpha = 0.5f),
                            post.tagColor,
                            post.tagColor.copy(alpha = 0.5f),
                            Color.Transparent,
                        )
                    )
                )
        )

        Column(modifier = Modifier.padding(16.dp)) {
            // ── Back + tag + xp ───────────────────────────────────────
            Row(
                modifier          = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(FitColors.Surface2)
                        .clickable(onClick = onBack),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "Voltar",
                        tint               = FitColors.TextPrimary,
                        modifier           = Modifier.size(18.dp),
                    )
                }

                Spacer(Modifier.weight(1f))

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
                        fontWeight    = FontWeight.Black,
                        letterSpacing = 0.5.sp,
                    )
                }

                Spacer(Modifier.width(8.dp))

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(FitColors.AccentDim.copy(alpha = 0.25f))
                        .padding(horizontal = 10.dp, vertical = 4.dp),
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Icon(Icons.Rounded.EmojiEvents, contentDescription = null, tint = FitColors.Accent, modifier = Modifier.size(13.dp))
                    Text(post.xp, color = FitColors.Accent, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Author row ────────────────────────────────────────────
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(post.authorColor.copy(alpha = 0.12f))
                        .border(1.5.dp, post.authorColor.copy(alpha = 0.35f), CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        post.authorInitials,
                        color      = post.authorColor,
                        fontSize   = 15.sp,
                        fontWeight = FontWeight.Black,
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        post.authorName,
                        color      = FitColors.TextPrimary,
                        fontSize   = 15.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Row(
                        verticalAlignment     = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        if (post.communityName.isNotEmpty()) {
                            Text(post.communityName, color = FitColors.TextDisabled, fontSize = 11.sp)
                            Text("·", color = FitColors.TextDisabled, fontSize = 11.sp)
                        }
                        Text(post.timeAgo, color = FitColors.TextDisabled, fontSize = 11.sp)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Content ───────────────────────────────────────────────
            Text(
                post.content,
                color      = FitColors.TextPrimary.copy(alpha = 0.9f),
                fontSize   = 15.sp,
                lineHeight = 23.sp,
            )

            // ── Photo placeholder ─────────────────────────────────────
            if (post.hasImage) {
                Spacer(Modifier.height(14.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(
                                    post.tagColor.copy(alpha = 0.20f),
                                    post.authorColor.copy(alpha = 0.10f),
                                )
                            )
                        )
                        .border(1.dp, post.tagColor.copy(alpha = 0.15f), RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        Icon(
                            Icons.Rounded.CameraAlt,
                            contentDescription = null,
                            tint               = post.tagColor.copy(alpha = 0.6f),
                            modifier           = Modifier.size(32.dp),
                        )
                        Text("Foto do treino", color = post.tagColor.copy(alpha = 0.6f), fontSize = 11.sp)
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // ── Actions ───────────────────────────────────────────────
            HorizontalDivider(color = FitColors.Outline, thickness = 0.5.dp)
            Spacer(Modifier.height(12.dp))

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable(onClick = onLike)
                        .padding(horizontal = 10.dp, vertical = 7.dp),
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Icon(
                        imageVector        = if (isLiked) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder,
                        contentDescription = "Curtir",
                        tint               = heartColor,
                        modifier           = Modifier.size(18.dp),
                    )
                    Text(
                        "$likesCount",
                        color      = heartColor,
                        fontSize   = 14.sp,
                        fontWeight = if (isLiked) FontWeight.SemiBold else FontWeight.Normal,
                    )
                }

                Spacer(Modifier.width(4.dp))

                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                    verticalAlignment     = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Icon(Icons.Rounded.Forum, contentDescription = null, tint = FitColors.TextMuted, modifier = Modifier.size(17.dp))
                    Text("${post.comments}", color = FitColors.TextMuted, fontSize = 14.sp)
                }

                Spacer(Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(FitColors.Surface2)
                        .clickable(onClick = onShare),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(Icons.Rounded.Share, contentDescription = "Compartilhar", tint = FitColors.TextMuted, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

// ── Comments section header ───────────────────────────────────────────────────

@Composable
private fun CommentsHeader(count: Int, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .width(2.5.dp)
                .height(12.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(FitColors.Blue.copy(alpha = 0.5f))
        )
        Spacer(Modifier.width(8.dp))
        Text(
            "COMENTÁRIOS",
            color         = FitColors.TextMuted,
            fontSize      = 10.sp,
            fontWeight    = FontWeight.Black,
            letterSpacing = 1.5.sp,
            modifier      = Modifier.weight(1f),
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .background(FitColors.BlueDim)
                .padding(horizontal = 8.dp, vertical = 3.dp),
        ) {
            Text("$count", color = FitColors.Blue, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// ── Comment item card ─────────────────────────────────────────────────────────

@Composable
private fun CommentItemCard(comment: CommentItem, modifier: Modifier = Modifier) {
    Row(
        modifier              = modifier.fillMaxWidth(),
        verticalAlignment     = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(comment.authorColor.copy(alpha = 0.12f))
                .border(1.dp, comment.authorColor.copy(alpha = 0.25f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                comment.authorInitials,
                color      = comment.authorColor,
                fontSize   = 11.sp,
                fontWeight = FontWeight.Black,
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .clip(
                    RoundedCornerShape(
                        topStart    = 4.dp,
                        topEnd      = 16.dp,
                        bottomStart = 16.dp,
                        bottomEnd   = 16.dp,
                    )
                )
                .background(FitColors.Surface2)
                .padding(horizontal = 12.dp, vertical = 10.dp),
        ) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    comment.authorName,
                    color      = FitColors.TextPrimary,
                    fontSize   = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier   = Modifier.weight(1f),
                )
                Text(comment.timeAgo, color = FitColors.TextDisabled, fontSize = 10.sp)
            }
            Spacer(Modifier.height(4.dp))
            Text(
                comment.text,
                color      = FitColors.TextPrimary.copy(alpha = 0.85f),
                fontSize   = 13.sp,
                lineHeight = 19.sp,
            )
            Spacer(Modifier.height(6.dp))
            Row(
                verticalAlignment     = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Icon(Icons.Rounded.FavoriteBorder, contentDescription = null, tint = FitColors.TextDisabled, modifier = Modifier.size(11.dp))
                Text("${comment.likes}", color = FitColors.TextDisabled, fontSize = 10.sp)
            }
        }
    }
}

// ── Comment input bar ─────────────────────────────────────────────────────────

@Composable
private fun CommentInputBar(
    text:         String,
    onTextChange: (String) -> Unit,
    onSend:       () -> Unit,
    accentColor:  Color = FitColors.Accent,
) {
    Surface(
        color           = FitColors.Surface,
        tonalElevation  = 0.dp,
        shadowElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment     = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            OutlinedTextField(
                value           = text,
                onValueChange   = onTextChange,
                placeholder     = {
                    Text("Adicionar comentário...", color = FitColors.TextDisabled, fontSize = 13.sp)
                },
                modifier        = Modifier.weight(1f),
                singleLine      = true,
                shape           = RoundedCornerShape(24.dp),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                textStyle       = LocalTextStyle.current.copy(fontSize = 13.sp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor   = accentColor.copy(alpha = 0.6f),
                    unfocusedBorderColor = FitColors.Outline,
                    focusedTextColor     = FitColors.TextPrimary,
                    unfocusedTextColor   = FitColors.TextPrimary,
                    cursorColor          = accentColor,
                ),
            )

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(if (text.isNotBlank()) accentColor else FitColors.Surface2)
                    .clickable(enabled = text.isNotBlank(), onClick = onSend),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    Icons.AutoMirrored.Rounded.Send,
                    contentDescription = "Enviar",
                    tint               = if (text.isNotBlank()) FitColors.Bg else FitColors.TextDisabled,
                    modifier           = Modifier.size(18.dp),
                )
            }
        }
    }
}
