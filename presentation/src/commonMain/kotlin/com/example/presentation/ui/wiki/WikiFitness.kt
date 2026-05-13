@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.presentation.ui.wiki

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.widgets.FitverseScreenTitle
import com.example.presentation.widgets.FitverseTopAppBar
import com.example.presentation.theme.FitverseColors
import com.example.presentation.theme.ShapeCard
import com.example.presentation.theme.ShapeTag

// ── Models ────────────────────────────────────────────────────────────────────

data class WikiArticle(
    val tag: String,
    val tagColor: Color,
    val tagTextColor: Color = FitverseColors.Bg,
    val title: String,
    val views: String,
    val readTime: String,
)

private val sampleArticles = listOf(
    WikiArticle("Força",    FitverseColors.TagForca,    title = "Hipertrofia: O Guia Definitivo",     views = "12.4k", readTime = "8 min"),
    WikiArticle("Treino",   FitverseColors.TagTreino,   title = "Periodização para Iniciantes",        views = "8.2k",  readTime = "5 min"),
    WikiArticle("Nutrição", FitverseColors.TagNutricao, tagTextColor = FitverseColors.TextPrimary,
        title = "Proteínas: Timing e Quantidade",      views = "15.1k", readTime = "6 min"),
    WikiArticle("Recovery", FitverseColors.TagRecovery, tagTextColor = FitverseColors.TextPrimary,
        title = "Descanso e Recuperação Muscular",     views = "9.7k",  readTime = "4 min"),
    WikiArticle("Cardio",   FitverseColors.TagCardio,   tagTextColor = FitverseColors.TextPrimary,
        title = "HIIT vs Cardio Tradicional",          views = "11.3k", readTime = "7 min"),
    WikiArticle("Força",    FitverseColors.TagForca,    title = "Overload Progressivo na Prática",     views = "7.9k",  readTime = "6 min"),
    WikiArticle("Nutrição", FitverseColors.TagNutricao, tagTextColor = FitverseColors.TextPrimary,
        title = "Déficit Calórico sem Perder Massa",   views = "18.3k", readTime = "9 min"),
)

// ── Screen ────────────────────────────────────────────────────────────────────

@Composable
fun WikiFitnessScreen(
    onBack: () -> Unit,
    onArticleClick: (WikiArticle) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var query by remember { mutableStateOf("") }

    val filtered = remember(query) {
        if (query.isBlank()) sampleArticles
        else sampleArticles.filter {
            it.title.contains(query, ignoreCase = true) ||
                    it.tag.contains(query, ignoreCase = true)
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            FitverseTopAppBar(
                title = "Wiki Fitness",
                onBack = onBack
            )
        }
    ){
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        ) {
            item {
                FitverseScreenTitle(
                    title = "Wiki Fitness",
                    subtitle = "Ciência aplicada ao seu treino"
                )
                Spacer(Modifier.height(18.dp))
                WikiSearchBar(
                    query    = query,
                    onChange = { query = it },
                )
                Spacer(Modifier.height(20.dp))
            }

            items(filtered, key = { it.title }) { article ->
                WikiArticleRow(
                    article  = article,
                    onClick  = { onArticleClick(article) },
                    modifier = Modifier.padding(bottom = 10.dp),
                )
            }

            item { Spacer(Modifier.height(24.dp)) }
        }

    }
}

// ── Search bar ────────────────────────────────────────────────────────────────

@Composable
private fun WikiSearchBar(query: String, onChange: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, FitverseColors.Border, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(FitverseColors.Surface)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector        = Icons.Default.Search,
            contentDescription = null,
            tint               = FitverseColors.TextMuted2,
            modifier           = Modifier.size(18.dp),
        )
        Spacer(Modifier.width(8.dp))
        BasicTextField(
            value         = query,
            onValueChange = onChange,
            singleLine    = true,
            cursorBrush   = SolidColor(FitverseColors.Accent),
            textStyle     = LocalTextStyle.current.copy(
                color    = FitverseColors.TextPrimary,
                fontSize = 13.sp,
            ),
            decorationBox = { inner ->
                Box {
                    if (query.isEmpty()) {
                        Text("Buscar artigos...", color = FitverseColors.TextMuted2, fontSize = 13.sp)
                    }
                    inner()
                }
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

// ── Article row ───────────────────────────────────────────────────────────────

@Composable
private fun WikiArticleRow(
    article: WikiArticle,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, FitverseColors.Border, ShapeCard)
            .clip(ShapeCard)
            .background(FitverseColors.Surface)
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Tag badge
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(ShapeTag)
                .background(article.tagColor),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text       = article.tag.uppercase(),
                color      = article.tagTextColor,
                fontSize   = 10.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.8.sp,
            )
        }

        Spacer(Modifier.width(14.dp))

        Column {
            Text(
                text       = article.title,
                fontSize   = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color      = FitverseColors.TextPrimary,
            )
            Spacer(Modifier.height(5.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("👁 ${article.views}", fontSize = 11.sp, color = FitverseColors.TextMuted)
                Text("⏱ ${article.readTime}", fontSize = 11.sp, color = FitverseColors.TextMuted)
            }
        }
    }
}
