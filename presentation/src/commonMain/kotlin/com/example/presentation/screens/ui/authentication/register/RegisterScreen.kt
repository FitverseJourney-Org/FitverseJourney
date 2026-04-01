package com.example.presentation.screens.ui.authentication.register

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Cake
import androidx.compose.material.icons.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.Functions
import androidx.compose.material.icons.rounded.Height
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.MonitorWeight
import androidx.compose.material.icons.rounded.PieChart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.key.Key.Companion.Calendar
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.authentication.register.FitnessGoal
import com.example.domain.model.authentication.register.FitnessLevel
import com.example.domain.model.authentication.register.Gender
import com.example.domain.model.authentication.register.RegisterPage
import com.example.expect.DateTimeManager
import com.example.presentation.screens.ui.authentication.login.components.FitverseOutlinedTextField
import com.example.presentation.screens.ui.authentication.register.actions.RegisterAction
import com.example.presentation.screens.ui.authentication.register.pages.RegisterPageSuccess
import com.example.presentation.screens.ui.authentication.register.state.RegisterState
import com.example.presentation.screens.widgets.FitVerseButton
import com.example.presentation.screens.widgets.FitVerseSpacer
import com.example.presentation.screens.widgets.FitverseIconBack
import com.example.presentation.screens.widgets.FitverseIconClose
import fitversejourneyapp.presentation.generated.resources.Res
import fitversejourneyapp.presentation.generated.resources.ico_assasin
import fitversejourneyapp.presentation.generated.resources.ico_avengers
import fitversejourneyapp.presentation.generated.resources.ico_logan
import fitversejourneyapp.presentation.generated.resources.ico_logo
import fitversejourneyapp.presentation.generated.resources.ico_marvel
import fitversejourneyapp.presentation.generated.resources.ico_panther
import kotlinx.coroutines.flow.distinctUntilChanged
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import kotlin.math.abs

private val SecondaryBlue = Color(0xFF2563EB)
private val TertiaryGreen = Color(0xFF10B981)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    state: RegisterState,
    animatedProgress: Float,
    snackBarHostState: @Composable () -> Unit,
    onAction: (RegisterAction) -> Unit,
) {
    val cs = MaterialTheme.colorScheme
    val totalPages = RegisterPage.entries.size.toFloat()
    val targetProgress = (state.page.ordinal + 1) / totalPages
    val animatedProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = tween(durationMillis = 500),
        label = "RegisterProgress"
    )
    Scaffold(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        containerColor = Color.Transparent, // Fundo Neutro #0A0B0F
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            ) {
                // Barra de progresso com o gradiente Blue -> Green da sua identidade
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .background(Color.White.copy(alpha = 0.05f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(animatedProgress)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(SecondaryBlue, TertiaryGreen)
                                )
                            )
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        FitverseIconBack(
                            onBack = {
                                onAction(RegisterAction.Back)
                            }
                        )
                        Text(
                            text = "PASSO ${state.page.ordinal + 1} DE ${RegisterPage.entries.size}",
                            color = Color.White.copy(alpha = 0.6f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.5.sp
                        )
                    }
                    FitverseIconClose(
                        onClose = {
                            onAction(RegisterAction.Exit)
                        }
                    )
                }
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier.navigationBarsPadding().padding(24.dp),
                contentAlignment = Alignment.Center
            ){
                FitVerseButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = if(state.page == RegisterPage.Success) "VOLTAR PARA LOGIN" else if(state.page == RegisterPage.Credentials) "FINALIZAR" else "AVANÇAR",
                    topColor = cs.primary,
                    edgeColor = cs.outline, // Ou uma cor mais escura que o primary
                    textColor = cs.onPrimary,
                    isLoading = state.isLoading,
                    onClick = {
                        if(state.page == RegisterPage.Success) {
                            onAction(RegisterAction.Exit)
                        }else{
                            onAction(RegisterAction.Next)
                        }
                    },
                )

            }
        }
    ) { padding ->
        AnimatedContent(
            modifier = Modifier.padding(padding),
            targetState = state.page,
            label = "RegisterPages",
            transitionSpec = {
                val isForward = targetState.ordinal > initialState.ordinal
                if (isForward) {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(400)
                    ) togetherWith
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                                animationSpec = tween(400)
                            )
                } else {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(400)
                    ) togetherWith
                            slideOutOfContainer(
                                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                                animationSpec = tween(400)
                            )
                }
            }
        ) { page ->
            // Mapeamento das páginas no fluxo
            when (page) {
                RegisterPage.Profile -> RegisterPageIntroduction(state, onAction)
                RegisterPage.Metrics -> RegisterPageMetrics(state, onAction)
                RegisterPage.Goals -> RegisterPageGoals(state, onAction)
                RegisterPage.Gender -> StepGender(state, onAction)
                RegisterPage.Level -> RegisterPageLevel(state, onAction)
                RegisterPage.Avatar -> StepAvatar(state, onAction)
                RegisterPage.Macros -> RegisterPageMacros(state, onAction) // NOVA TELA AQUI
                RegisterPage.Credentials -> RegisterPageCredentials(state, onAction)
                RegisterPage.Success -> RegisterPageSuccess(state, onAction)
            }
        }
    }
}



@Composable
fun FormHeader(title: String, subtitle: String? = null) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall.copy(
                color = Color.White
            ),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        subtitle?.let {
            Spacer(Modifier.height(8.dp))
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun GridOptionCard(
    text: String,
    icon: ImageVector? = null,
    iconResource: DrawableResource? = null,
    iconBgColor: Color,
    iconTint: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val brandPurple = Color(0xFF4F46E5)

    val scale by animateFloatAsState(if (isSelected) 1.05f else 1f, label = "scale")
    val containerColor by animateColorAsState(
        if (isSelected) brandPurple.copy(alpha = 0.08f) else colors.surfaceVariant.copy(alpha = 0.4f),
        label = "color"
    )

    Surface(
        modifier = Modifier
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .fillMaxWidth()
            .aspectRatio(1.05f)
            .clickable { onClick() },
        shape = RoundedCornerShape(28.dp),
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) brandPurple else colors.outlineVariant.copy(alpha = 0.2f)
        ),
        color = containerColor
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(12.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = if (isSelected) iconTint.copy(alpha = 0.2f) else iconBgColor,
                modifier = Modifier.size(60.dp)
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isSelected) brandPurple else iconTint,
                        modifier = Modifier.padding(16.dp)
                    )
                } else if (iconResource != null) {
                    Image(
                        painter = painterResource(resource = iconResource),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
            }
            if (text.isNotBlank()) {
                Spacer(Modifier.height(16.dp))
                Text(
                    text = text.uppercase(),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Black,
                    color = if (isSelected) brandPurple else colors.onSurface,
                    letterSpacing = 1.sp
                )
            }
        }


    }
}

// --- TELAS ---
@Composable
fun RegisterPageMetrics(state: RegisterState, onAction: (RegisterAction) -> Unit) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FormHeader(
            title = "Biometria do Avatar",
            subtitle = "Calibre seus dados para receber missões de treino adequadas e calcular seu gasto calórico com precisão."
        )
        // 1. Bloco de Data de Nascimento
        MetricSectionCard(
            title = "Data de Nascimento",
            icon = Icons.Rounded.Cake
        ) {
            FitverseDatePicker(
                onDateSelected = { day, month, year ->
                    // onAction(RegisterAction.DateOfBirthChanged(day, month, year))
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 2. Bloco de Peso
        MetricSectionCard(
            title = "Peso Corporal",
            icon = Icons.Rounded.MonitorWeight
        ) {
            FitverseWeightPicker(
                initialWeight = 70,
                onWeightSelected = { weight ->
                    // onAction(RegisterAction.WeightChanged(weight))
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 3. Bloco de Altura
        MetricSectionCard(
            title = "Altura",
            icon = Icons.Rounded.Height
        ) {
            FitverseHeightPicker(
                initialHeight = 170,
                onHeightSelected = { height ->
                    // onAction(RegisterAction.HeightChanged(height))
                }
            )
        }

        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Composable
private fun MetricSectionCard(
    title: String,
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 20.dp),
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )

            content()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> FitverseWheelPicker(
    items: List<T>,
    initialIndex: Int = 0,
    itemHeight: Dp = 56.dp, // Aumentado levemente para melhor área de toque
    visibleItemsCount: Int = 5,
    textStyle: TextStyle = MaterialTheme.typography.titleLarge,
    selectedTextColor: Color = MaterialTheme.colorScheme.primary,
    unselectedTextColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
    onItemSelected: (T) -> Unit,
    itemContent: @Composable (T) -> String = { it.toString() }
) {
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = initialIndex)
    val hapticFeedback = LocalHapticFeedback.current

    // SnapshotFlow é mais eficiente para observar mudanças contínuas de layout e disparar side-effects
    LaunchedEffect(listState) {
        snapshotFlow {
            val layoutInfo = listState.layoutInfo
            if (layoutInfo.visibleItemsInfo.isEmpty()) return@snapshotFlow initialIndex

            val center = layoutInfo.viewportStartOffset + (layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset) / 2
            layoutInfo.visibleItemsInfo.minByOrNull { abs((it.offset + it.size / 2) - center) }?.index ?: initialIndex
        }
            .distinctUntilChanged()
            .collect { index ->
                if (listState.isScrollInProgress) {
                    hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                }
                onItemSelected(items[index])
            }
    }

    Box(
        modifier = Modifier
            .height(itemHeight * visibleItemsCount)
            .fillMaxWidth()
            .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
            .drawWithContent {
                drawContent()
                // Fading edge mais suave
                drawRect(
                    brush = Brush.verticalGradient(
                        0f to Color.Transparent,
                        0.3f to Color.Black,
                        0.7f to Color.Black,
                        1f to Color.Transparent
                    ),
                    blendMode = BlendMode.DstIn
                )
            },
        contentAlignment = Alignment.Center
    ) {
        // Indicador de Seleção Central Moderno (Atrás do texto)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight)
                .background(
                    color = selectedTextColor.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(16.dp)
                )
        )

        LazyColumn(
            state = listState,
            flingBehavior = rememberSnapFlingBehavior(lazyListState = listState),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = itemHeight * (visibleItemsCount / 2))
        ) {
            items(items.size) { index ->
                Box(
                    modifier = Modifier
                        .height(itemHeight)
                        .fillMaxWidth()
                        .graphicsLayer {
                            val layoutInfo = listState.layoutInfo
                            val visibleInfo = layoutInfo.visibleItemsInfo.find { it.index == index }

                            if (visibleInfo != null) {
                                val viewportHeight = layoutInfo.viewportEndOffset - layoutInfo.viewportStartOffset
                                val centerOfViewport = layoutInfo.viewportStartOffset + viewportHeight / 2f
                                val itemCenter = visibleInfo.offset + visibleInfo.size / 2f

                                val distance = itemCenter - centerOfViewport
                                val fraction = (distance / (viewportHeight / 2f)).coerceIn(-1f, 1f)

                                // Easing suave para o efeito 3D (não linear)
                                val interpolatedFraction = abs(fraction) * abs(fraction)

                                rotationX = fraction * 45f
                                val scale = 1f - 0.25f * interpolatedFraction
                                scaleX = scale
                                scaleY = scale

                                alpha = 1f - 0.6f * interpolatedFraction
                            } else {
                                alpha = 0f
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    // Calculamos a cor baseada no fato de ser o centro ou não, de forma simplificada
                    // A animação real de cor/alfa ocorre no graphicsLayer acima
                    Text(
                        text = itemContent(items[index]),
                        style = textStyle.copy(fontWeight = FontWeight.Bold),
                        color = selectedTextColor,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

// 2. Componente de Peso
@Composable
fun FitverseWeightPicker(
    initialWeight: Int = 70,
    onWeightSelected: (Int) -> Unit
) {
    val weights = remember { (30..200).toList() }
    val initialIndex = weights.indexOf(initialWeight).takeIf { it >= 0 } ?: 40

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            FitverseWheelPicker(
                items = weights,
                initialIndex = initialIndex,
                onItemSelected = onWeightSelected
            )
        }
        Text(
            text = "kg",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
fun FitverseHeightPicker(
    initialHeight: Int = 170,
    onHeightSelected: (Int) -> Unit
) {
    val heights = remember { (100..250).toList() }
    val initialIndex = heights.indexOf(initialHeight).takeIf { it >= 0 } ?: 70

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        Box(modifier = Modifier.weight(1f)) {
            FitverseWheelPicker(
                items = heights,
                initialIndex = initialIndex,
                onItemSelected = onHeightSelected
            )
        }
        Text(
            text = "cm",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
fun FitverseDatePicker(
    onDateSelected: (day: Int, month: Int, year: Int) -> Unit
) {
    // 1. Captura o ano máximo permitido (Ano Atual - 18) usando a API Calendar
    val maxAllowedYear = remember {
        DateTimeManager.getCurrentYear() - 18
    }

    val days = remember { (1..31).toList() }
    val months = remember { listOf("Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez") }

    // 2. Gera a lista de anos de 1950 até o ano máximo permitido para +18
    val years = remember(maxAllowedYear) { (1950..maxAllowedYear).toList() }

    var selectedDay by remember { mutableIntStateOf(15) }
    var selectedMonth by remember { mutableStateOf(months[6]) }

    // 3. Garante que o ano padrão selecionado (1995) seja válido, caso contrário pega o último ano disponível
    var selectedYear by remember {
        mutableIntStateOf(if (years.contains(1995)) 1995 else maxAllowedYear)
    }

    LaunchedEffect(selectedDay, selectedMonth, selectedYear) {
        val monthIndex = months.indexOf(selectedMonth) + 1
        onDateSelected(selectedDay, monthIndex, selectedYear)
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(modifier = Modifier.weight(0.8f)) {
            FitverseWheelPicker(
                items = days,
                initialIndex = days.indexOf(selectedDay).takeIf { it >= 0 } ?: 14,
                onItemSelected = { selectedDay = it }
            )
        }
        Box(modifier = Modifier.weight(1f)) {
            FitverseWheelPicker(
                items = months,
                initialIndex = months.indexOf(selectedMonth).takeIf { it >= 0 } ?: 6,
                onItemSelected = { selectedMonth = it }
            )
        }
        Box(modifier = Modifier.weight(1.2f)) {
            FitverseWheelPicker(
                items = years,
                // Fallback seguro dinâmico para evitar OutOfBounds
                initialIndex = years.indexOf(selectedYear).takeIf { it >= 0 } ?: (years.size / 2),
                onItemSelected = { selectedYear = it }
            )
        }
    }
}

@Composable
fun RegisterPageIntroduction(state: RegisterState, onAction: (RegisterAction) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FormHeader(
            title = "Bem-vindo ao Fitverse!",
            subtitle = "Sua jornada começa agora. Calibre seu avatar."
        )

        FitVerseSpacer(vertical = true, value = 24.dp)

        FitverseOutlinedTextField(
            value = state.firstName, // Ajustado para firstName
            onValueChange = { onAction(RegisterAction.FirstName(it)) },
            placeholder = "Joe",
            singleLine = true,
            label = "First Name"
        )
        FitverseOutlinedTextField(
            value = state.lastName, // Ajustado para lastName
            onValueChange = { onAction(RegisterAction.LastName(it)) },
            placeholder = "Doe",
            singleLine = true,
            label = "Last Name"
        )
    }
}

@Composable
fun StepGender(state: RegisterState, onAction: (RegisterAction) -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp)) {
        FormHeader(
            title = "Como você se identifica?",
            subtitle = "Essencial para o cálculo da taxa metabólica e macros."
        )

        // Definindo cores vibrantes (Tech Blue e Tech Pink) que fogem do "pastel"
        val genders = listOf(
            GenderItem(
                name = "Masculino",
                icon = Icons.Default.Male,
                color = Color(0xFF2196F3), // Azul Tech vibrante
                gender = Gender.MALE
            ),
            GenderItem(
                name = "Feminino",
                icon = Icons.Default.Female,
                color = Color(0xFFE91E63), // Rosa Tech vibrante (quase Magenta)
                gender = Gender.FEMALE
            )
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(10.dp),
        ) {
            items(genders) { item ->
                GridOptionCard(
                    text = item.name,
                    icon = item.icon,
                    iconBgColor = item.color.copy(alpha = 0.12f),
                    iconTint = item.color,
                    isSelected = state.gender == item.gender,
                    onClick = {
                        onAction(RegisterAction.GenderChanged(item.gender))
                    }
                )
            }
        }
    }
}

// Data class auxiliar para organizar a lista
private data class GenderItem(
    val name: String,
    val icon: ImageVector,
    val color: Color,
    val gender: Gender
)

@Composable
fun RegisterPageGoals(state: RegisterState, onAction: (RegisterAction) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        FormHeader(
            title = "Qual seu objetivo?",
            subtitle = "Isso define sua rotina e a base do seu plano alimentar."
        )

        // Paleta Vibrante: Força (Vermelho), Queima (Azul), Performance (Roxo), Vitalidade (Verde)
        val goals = listOf(
            GoalItem(
                "Ganho de Massa",
                Icons.Default.TrendingUp,
                Color(0xFFF43F5E),
                FitnessGoal.GAIN_MUSCLE
            ),
            GoalItem(
                "Perda de Peso",
                Icons.Default.Scale,
                Color(0xFF3B82F6),
                FitnessGoal.LOSE_WEIGHT
            ),
            GoalItem(
                "Performance",
                Icons.Default.Speed,
                Color(0xFF8B5CF6),
                FitnessGoal.MAINTENANCE
            ),
            GoalItem("Saúde", Icons.Default.Favorite, Color(0xFF10B981), FitnessGoal.HEALTH)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize().padding(10.dp),
            contentPadding = PaddingValues(10.dp)
        ) {
            items(goals) { item ->
                GridOptionCard(
                    text = item.title,
                    icon = item.icon,
                    // Fundo do ícone: 15% da cor vibrante
                    iconBgColor = item.color.copy(alpha = 0.15f),
                    // Ícone: Cor sólida vibrante para contraste total
                    iconTint = item.color,
                    isSelected = state.fitnessGoal == item.goal,
                    onClick = { onAction(RegisterAction.GoalsChanged(item.goal)) }
                )
            }
        }
    }
}

private data class GoalItem(
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val goal: FitnessGoal
)

@Composable
fun StepAvatar(state: RegisterState, onAction: (RegisterAction) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        FormHeader(
            title = "Escolha seu avatar",
            subtitle = "Sua identidade no Fitverse. Você poderá mudar depois."
        )

        val avatars = listOf(
            Triple("spiderman", Res.drawable.ico_marvel, "Spider-Man"),
            Triple("assassin", Res.drawable.ico_assasin, "Deadpool"),
            Triple("avengers", Res.drawable.ico_avengers, "Avenger"),
            Triple("logan", Res.drawable.ico_logan, "Wolverine"),
            Triple("panther", Res.drawable.ico_panther, "Black Panther")
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(10.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            items(avatars) { (id, avatarRes, text) ->
                GridOptionCard(
                    text = text,
                    iconResource = avatarRes,
                    iconBgColor = Color.White.copy(alpha = 0.15f),
                    iconTint = Color.White,
                    isSelected = state.selectedAvatarId == id,
                    onClick = { onAction(RegisterAction.UpdateAvatar(id)) }
                )
            }
        }
    }
}

@Composable
fun RegisterPageLevel(state: RegisterState, onAction: (RegisterAction) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        FormHeader(
            title = "Qual sua experiência?",
            subtitle = "Isso ajustará a intensidade dos seus treinos iniciais."
        )

        // Definimos cores que remetem à progressão (Cinza -> Verde -> Roxo -> Dourado)
        val levels = listOf(
            LevelItem(
                "Sedentário",
                Icons.Default.Cloud,
                Color(0xFF94A3B8).copy(alpha = 0.15f),
                Color(0xFF475569),
                FitnessLevel.SEDENTARY
            ),
            LevelItem(
                "Iniciante",
                Icons.Default.DirectionsRun,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                MaterialTheme.colorScheme.primary,
                FitnessLevel.BEGINNER
            ),
            LevelItem(
                "Intermediário",
                Icons.Default.FitnessCenter,
                MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f),
                MaterialTheme.colorScheme.secondary,
                FitnessLevel.INTERMEDIATE
            ),
            LevelItem(
                "Atleta",
                Icons.Default.EmojiEvents,
                Color(0xFFFACC15).copy(alpha = 0.2f),
                Color(0xFFD97706),
                FitnessLevel.ADVANCED
            )
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize().padding(10.dp),
            contentPadding = PaddingValues(10.dp),
        ) {
            items(levels) { item ->
                GridOptionCard(
                    text = item.title,
                    icon = item.icon,
                    iconBgColor = item.bgColor, // Cor sólida/translúcida, nunca branca
                    iconTint = item.tint,
                    isSelected = state.trainingLevel == item.level,
                    onClick = { onAction(RegisterAction.TrainingLevelChanged(item.level)) }
                )
            }
        }
    }
}

// Helper class para organizar os dados da lista
data class LevelItem(
    val title: String,
    val icon: ImageVector,
    val bgColor: Color,
    val tint: Color,
    val level: FitnessLevel
)

@Composable
fun RegisterPageCredentials(state: RegisterState, onAction: (RegisterAction) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        FormHeader(title = "Dados de acesso", subtitle = "Para salvar seu progresso no Fitverse.")
        OutlinedTextField(
            value = state.email,
            onValueChange = { /* Action */ },
            label = { Text("E-mail") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            singleLine = true
        )
        OutlinedTextField(
            value = state.password,
            onValueChange = { /* Action */ },
            label = { Text("Senha") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            singleLine = true
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterPageMacros(state: RegisterState, onAction: (RegisterAction) -> Unit) {
    var isEditing by remember { mutableStateOf(false) }
    var showInfoSheet by remember { mutableStateOf(false) } // Estado para o modal
    val sheetState = rememberModalBottomSheetState()
    val colors = MaterialTheme.colorScheme
// Modal de Informação (Cálculos)
    if (showInfoSheet) {
        ModalBottomSheet(
            onDismissRequest = { showInfoSheet = false },
            sheetState = sheetState,
            containerColor = colors.surface,
            dragHandle = { BottomSheetDefaults.DragHandle(color = colors.onSurfaceVariant.copy(alpha = 0.4f)) }
        ) {
            MacroCalculationInfoContent()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(32.dp))

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            FormHeader(
                title = "Seu Alvo Diário",
                subtitle = "Metas baseadas no seu perfil e nível de atividade."
            )

            // Ícone de Informação posicionado à direita do título
            IconButton(
                onClick = { showInfoSheet = true },
                modifier = Modifier.align(Alignment.TopEnd)
            ){
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Como Calculamos",
                    tint = colors.primary.copy(alpha = 0.7f)
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        // 1. CALORIAS (Destaque Principal)
        // 1. CALORIAS (Destaque Principal)
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center // Isso garante que o conteúdo fique 100% no meio
        ) {
            MacroCardRefined(
                modifier = Modifier.fillMaxWidth(0.65f), // Mantém o tamanho proporcional
                title = "Calorias",
                value = state.targetCalories.toString(),
                unit = "kcal",
                icon = Icons.Default.LocalFireDepartment,
                accentColor = Color(0xFFFF9800), // Laranja
                isEditing = isEditing,
                onValueChange = { onAction(RegisterAction.UpdateCalories(it.toIntOrNull() ?: 0)) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 2. GRID DE MACROS E ÁGUA
        // Linha 1: Carboidratos e Proteínas
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MacroCardRefined(
                modifier = Modifier.weight(1f),
                title = "Carbos",
                value = state.targetCarbs.toString(),
                unit = "g",
                icon = Icons.Default.BakeryDining,
                accentColor = Color(0xFF4CAF50), // Verde
                isEditing = isEditing,
                onValueChange = { onAction(RegisterAction.UpdateCarbs(it.toIntOrNull() ?: 0)) }
            )
            MacroCardRefined(
                modifier = Modifier.weight(1f),
                title = "Proteína",
                value = state.targetProteins.toString(),
                unit = "g",
                icon = Icons.Default.Restaurant,
                accentColor = Color(0xFFE91E63), // Rosa/Red
                isEditing = isEditing,
                onValueChange = { onAction(RegisterAction.UpdateProteins(it.toIntOrNull() ?: 0)) }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Linha 2: Gorduras e Água
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MacroCardRefined(
                modifier = Modifier.weight(1f),
                title = "Gordura",
                value = state.targetFats.toString(),
                unit = "g",
                icon = Icons.Default.Opacity,
                accentColor = Color(0xFF2196F3), // Azul
                isEditing = isEditing,
                onValueChange = { onAction(RegisterAction.UpdateFats(it.toIntOrNull() ?: 0)) }
            )
            MacroCardRefined(
                modifier = Modifier.weight(1f),
                title = "Água",
                value = state.targetWater.toString(),
                unit = "ml",
                icon = Icons.Default.WaterDrop,
                accentColor = Color(0xFF00BCD4), // Ciano/Água
                isEditing = isEditing,
                onValueChange = { onAction(RegisterAction.UpdateWater(it.toIntOrNull() ?: 0)) }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Botão de Ajuste
        OutlinedButton(
            onClick = { isEditing = !isEditing },
            shape = CircleShape,
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = if (isEditing) colors.primaryContainer else Color.Transparent
            ),
            border = BorderStroke(1.dp, if (isEditing) colors.primary else colors.outlineVariant)
        ) {
            Icon(
                if (isEditing) Icons.Default.Check else Icons.Default.Tune,
                null,
                Modifier.size(18.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(if (isEditing) "SALVAR METAS" else "AJUSTAR MANUALMENTE")
        }

        Spacer(Modifier.height(40.dp))
    }
}

@Composable
fun MacroCalculationInfoContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 48.dp)
    ) {
        Text(
            text = "Como calculamos?",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.5).sp
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(24.dp))

        // Início do Timeline/Stepper
        Column(verticalArrangement = Arrangement.spacedBy(0.dp)) {
            CalculationStepItem(
                icon = Icons.Rounded.Functions,
                title = "Taxa Metabólica Basal",
                description = "Cálculo via Mifflin-St Jeor (peso, altura e idade).",
                isLast = false
            )
            CalculationStepItem(
                icon = Icons.Rounded.DirectionsRun,
                title = "Gasto Energético Total",
                description = "Ajuste fino baseado no seu fator de atividade semanal.",
                isLast = false
            )
            CalculationStepItem(
                icon = Icons.Rounded.PieChart,
                title = "Distribuição de Macros",
                isLast = true
            ) {
                // Conteúdo customizado para o último item (os badges)
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MacroBadge("Prot: 2g/kg", MaterialTheme.colorScheme.primary)
                    MacroBadge("Gord: 0.8g/kg", MaterialTheme.colorScheme.secondary)
                }
            }
        }

        Spacer(Modifier.height(32.dp))

        // Nota de rodapé estilo "Callout"
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = "Valores estimados. Para ajustes de performance ou saúde, consulte um nutricionista.",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
private fun CalculationStepItem(
    icon: ImageVector,
    title: String,
    description: String? = null,
    isLast: Boolean = false,
    content: @Composable (() -> Unit)? = null
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        // Coluna do Indicador (Linha e Ponto)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(32.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(60.dp) // Ajuste conforme o conteúdo
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    Color.Transparent
                                )
                            )
                        )
                )
            }
        }

        Spacer(Modifier.width(16.dp))

        // Coluna do Conteúdo
        Column(modifier = Modifier.padding(bottom = if (isLast) 0.dp else 24.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            description?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            content?.invoke()
        }
    }
}

@Composable
private fun MacroBadge(text: String, color: Color) {
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.2f))
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = color
        )
    }
}

@Composable
fun InfoRow(title: String, description: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(text = title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
        Text(text = description, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
    }
}

@Composable
fun MacroCardRefined(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    unit: String,
    icon: ImageVector,
    accentColor: Color,
    isEditing: Boolean,
    onValueChange: (String) -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val animatedBorderColor by animateColorAsState(
        if (isEditing) accentColor else colors.outlineVariant.copy(alpha = 0.2f),
        label = "border"
    )

    Surface(
        modifier = modifier,
        color = colors.surfaceColorAtElevation(2.dp),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.5.dp, animatedBorderColor)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally // Garante alinhamento geral
        ) {
            // Header: Ícone + Título
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(icon, null, tint = accentColor, modifier = Modifier.size(14.dp))
                Spacer(Modifier.width(4.dp))
                Text(
                    text = title.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.onSurfaceVariant,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }

            Spacer(Modifier.height(8.dp))

            // Área de Valor
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center // Trava tudo no centro
            ) {
                if (isEditing) {
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        textStyle = MaterialTheme.typography.titleLarge.copy(
                            textAlign = TextAlign.Center, // Centraliza o texto
                            color = colors.onSurface,
                            fontWeight = FontWeight.Black
                        ),
                        // Removido o IntrinsicSize.Min que estava bugando o cursor
                        modifier = Modifier.widthIn(min = 60.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        cursorBrush = SolidColor(accentColor),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center // Centraliza a caixa de texto em si
                            ) {
                                innerTextField()
                            }
                        }
                    )
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = value,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            color = colors.onSurface
                        )
                        Text(
                            text = unit,
                            style = MaterialTheme.typography.bodySmall,
                            color = colors.onSurfaceVariant,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }
        }
    }
}