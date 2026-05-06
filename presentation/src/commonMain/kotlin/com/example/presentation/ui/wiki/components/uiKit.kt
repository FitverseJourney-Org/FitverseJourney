package com.example.presentation.ui.wiki.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.domain.models.wiki.WikiArticle
import com.example.domain.models.wiki.WikiCategory
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction

// ─────────────────────────────────────────────────────────────────────────────
// WikiSearchBar
// ─────────────────────────────────────────────────────────────────────────────


// ─────────────────────────────────────────────────────────────────────────────
// WikiSearchBar (Refatorado para TextField simples)
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Barra de pesquisa simples utilizando [TextField].
 * Filtra a lista reativamente conforme o usuário digita.
 *
 * @param query          Texto atual do campo de busca.
 * @param onQueryChange  Callback chamado a cada caractere digitado.
 */
@Composable
fun WikiSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current

    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = { Text("Buscar: creatina, ABCD, proteínas...") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                tint = MaterialTheme.colorScheme.primary,
            )
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = query.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                IconButton(
                    onClick = {
                        onQueryChange("")
                        focusManager.clearFocus() // Tira o foco ao limpar
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Limpar busca",
                    )
                }
            }
        },
        // Remove a linha inferior padrão do TextField para parecer uma SearchBar
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        // Configura o teclado para exibir o botão "Buscar/Lupa"
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                focusManager.clearFocus() // Esconde o teclado ao apertar "Buscar"
            }
        )
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// WikiCategoryChips
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Linha horizontal de [FilterChip]s para navegação por categoria.
 *
 * Utiliza [LazyRow] para suportar qualquer quantidade de categorias sem overflow.
 * O chip selecionado recebe destaque com a cor primária do tema.
 *
 * @param categories       Lista ordenada de categorias disponíveis.
 * @param selectedCategory Categoria atualmente ativa.
 * @param onCategoryClick  Callback invocado ao selecionar um chip.
 */
@Composable
fun WikiCategoryChips(
    categories: List<WikiCategory>,
    selectedCategory: WikiCategory,
    onCategoryClick: (WikiCategory) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(categories, key = { it.name }) { category ->
            val isSelected = category == selectedCategory

            // Animação suave de escala ao selecionar
            val scale by animateFloatAsState(
                targetValue = if (isSelected) 1.05f else 1f,
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                label = "chip_scale_${category.name}",
            )

            FilterChip(
                selected = isSelected,
                onClick = { onCategoryClick(category) },
                label = {
                    Text(
                        text = "${category.emoji} ${category.displayName}",
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
                modifier = Modifier.scale(scale),
            )
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// WikiArticleCard
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Card reutilizável que representa um artigo na lista da Wiki.
 *
 * Layout:
 * ┌─────────────────────────────────┐
 * │  [Imagem / Emoji Placeholder]   │
 * │─────────────────────────────────│
 * │  [Tag Categoria]    [Bookmark]  │
 * │  [Título]                       │
 * │  [Resumo]                       │
 * │  [⏱ X min]  [⭐ XP pts]         │
 * └─────────────────────────────────┘
 *
 * @param article       Dados do artigo a exibir.
 * @param onClick       Callback ao clicar no card inteiro.
 * @param onBookmark    Callback ao clicar no ícone de bookmark.
 */
@Composable
fun WikiArticleCard(
    article: WikiArticle,
    onClick: () -> Unit,
    onBookmark: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
    ) {
        Column {
            // ── Imagem / Placeholder ──────────────────────────────────────
            ArticleImageSection(
                imageUrl = article.imageUrl,
                emoji = article.iconEmoji,
                category = article.category,
            )

            // ── Conteúdo ──────────────────────────────────────────────────
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // Tag da categoria
                    CategoryTag(category = article.category)

                    // Botão de Bookmark com animação de cor
                    val bookmarkColor by animateColorAsState(
                        targetValue = if (article.isBookmarked)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant,
                        label = "bookmark_color",
                    )
                    IconButton(
                        onClick = onBookmark,
                        modifier = Modifier.size(32.dp),
                    ) {
                        Icon(
                            imageVector = if (article.isBookmarked) Icons.Default.Bookmark
                            else Icons.Default.BookmarkBorder,
                            contentDescription = if (article.isBookmarked) "Remover favorito"
                            else "Adicionar favorito",
                            tint = bookmarkColor,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Título
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Resumo
                Text(
                    text = article.summary,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Footer: tempo de leitura + XP
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    MetaChip(
                        icon = { Icon(Icons.Default.Timer, null, Modifier.size(12.dp)) },
                        text = "${article.readingTimeMinutes} min",
                    )
                    MetaChip(
                        icon = { Icon(Icons.Default.Star, null, Modifier.size(12.dp), tint = Color(0xFFFFC107)) },
                        text = "+${article.xpReward} XP",
                    )
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// WikiFeaturedCard  (Hero card no topo do feed)
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Card de destaque (hero) exibido no topo da lista.
 * Usa gradiente sobre a imagem para legibilidade do texto.
 */
@Composable
fun WikiFeaturedCard(
    article: WikiArticle,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    ) {
        Box {
            // Imagem de fundo
            if (article.imageUrl != null) {
                AsyncImage(
                    model = article.imageUrl,
                    contentDescription = article.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize(),
                )
            } else {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    MaterialTheme.colorScheme.secondaryContainer,
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = article.iconEmoji, fontSize = 64.sp)
                }
            }

            // Gradiente de legibilidade
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                            startY = 80f,
                        )
                    )
            )

            // Conteúdo textual sobre o gradiente
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
            ) {
                CategoryTag(
                    category = article.category,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = article.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = "⭐ Destaque da semana",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.8f),
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Componentes auxiliares privados
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun ArticleImageSection(
    imageUrl: String?,
    emoji: String,
    category: WikiCategory,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
        contentAlignment = Alignment.Center,
    ) {
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize(),
            )
        } else {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = emoji, fontSize = 40.sp)
            }
        }
    }
}

@Composable
private fun CategoryTag(
    category: WikiCategory,
    containerColor: Color = MaterialTheme.colorScheme.tertiaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onTertiaryContainer,
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(containerColor)
            .padding(horizontal = 8.dp, vertical = 3.dp),
    ) {
        Text(
            text = "${category.emoji} ${category.displayName}",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            color = contentColor,
        )
    }
}

@Composable
private fun MetaChip(
    icon: @Composable () -> Unit,
    text: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        icon()
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// WikiSkeletonCard  (Shimmer placeholder durante carregamento)
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Card esqueleto exibido enquanto os artigos estão sendo carregados.
 * O efeito shimmer é simulado com um gradiente animado.
 */
@Composable
fun WikiSkeletonCard(modifier: Modifier = Modifier) {
    val shimmerColor = MaterialTheme.colorScheme.surfaceVariant

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = shimmerColor),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(18.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                )
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                )
                Spacer(Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(14.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                )
            }
        }
    }
}