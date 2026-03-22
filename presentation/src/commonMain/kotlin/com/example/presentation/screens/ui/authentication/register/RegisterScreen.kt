package com.example.presentation.screens.ui.authentication.register

import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.authentication.register.FitnessGoal
import com.example.domain.model.authentication.register.FitnessLevel
import com.example.domain.model.authentication.register.Gender
import com.example.domain.model.authentication.register.RegisterAction
import com.example.domain.model.authentication.register.RegisterPage
import com.example.presentation.screens.ui.authentication.register.pages.RegisterPageSuccess
import com.example.presentation.screens.ui.authentication.register.state.RegisterState
import com.example.presentation.screens.widgets.FitVerseButton


enum class RegisterPage {
    Profile, Goals, Gender, Level, Avatar, Macros, Credentials, Success
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    state: RegisterState,
    animatedProgress: Float,
    snackBarHostState: @Composable () -> Unit,
    onAction: (RegisterAction) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        topBar = {
            Column(modifier = Modifier.fillMaxWidth().statusBarsPadding()) {
                // Barra de progresso ultra-fina no topo
                LinearProgressIndicator(
                    progress = { animatedProgress },
                    modifier = Modifier.fillMaxWidth().height(8.dp),
                    color = Color(0xFF4CAF50),
                    trackColor = Color(0xFFE0E0E0)
                )

                // Botão voltar minimalista
                IconButton(onClick = { onAction(RegisterAction.Back) }) {
                    Icon(Icons.Default.ChevronLeft, "Voltar", tint = Color.White)
                }
            }
        },
        bottomBar = {
            // Botão "Continuar" único e grande
            Box(modifier = Modifier.padding(24.dp).navigationBarsPadding()) {
                FitVerseButton(
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    text = if (state.page == RegisterPage.Credentials) "FINALIZAR" else "CONTINUAR",
                    onClick = { onAction(RegisterAction.Next) },
                    topColor = MaterialTheme.colorScheme.secondary,    // Roxo Elétrico
                    edgeColor = Color(0xFF4F46E5),                     // Roxo mais escuro para a base 3D
                    textColor = Color.White,                           // Texto branco para contraste máximo
                )
            }
        }
    ) { padding ->
        AnimatedContent(
            modifier = Modifier.padding(padding).padding(horizontal = 16.dp),
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
                RegisterPage.Profile -> RegisterPageProfile(state, onAction)
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
    icon: ImageVector,
    iconBgColor: Color,
    iconTint: Color = MaterialTheme.colorScheme.secondary, // Roxo por padrão
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // Animações de estado para suavidade
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFF4F46E5) else Color(0xFFE2E8F0),
        label = "BorderColor"
    )
    val borderWidth by animateDpAsState(
        targetValue = if (isSelected) 2.dp else 1.dp,
        label = "BorderWidth"
    )
    val elevation by animateDpAsState(
        targetValue = if (isSelected) 8.dp else 0.dp,
        label = "Elevation"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp), // Bordas mais modernas
        border = BorderStroke(borderWidth, borderColor),
        color = MaterialTheme.colorScheme.surfaceVariant, // BRANCO SÓLIDO: Bloqueia a animação atrás do texto para melhor leitura
        shadowElevation = elevation // Sombra apenas quando selecionado
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(12.dp)
        ) {
            // Container do Ícone
            Surface(
                shape = CircleShape,
                color = iconBgColor,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.padding(14.dp)
                )
            }

            Spacer(Modifier.height(14.dp))

            // Texto com peso ExtraBold para a estética "Performance"
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp,
                color = if (isSelected) Color(0xFF4F46E5) else Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}

// --- TELAS ---

@Composable
fun RegisterPageProfile(state: RegisterState, onAction: (RegisterAction) -> Unit) {
    Column {
        FormHeader(
            title = "Bem-vindo ao Fitverse!",
            subtitle = "Vamos criar um plano de treino focado em você."
        )
    }
}

@Composable
fun StepGender(state: RegisterState, onAction: (RegisterAction) -> Unit) {
    Column {
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
            modifier = Modifier.fillMaxWidth()
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
    Column {
        FormHeader(
            title = "Qual seu objetivo?",
            subtitle = "Isso define sua rotina e a base do seu plano alimentar."
        )

        // Paleta Vibrante: Força (Vermelho), Queima (Azul), Performance (Roxo), Vitalidade (Verde)
        val goals = listOf(
            GoalItem("Ganho de Massa", Icons.Default.TrendingUp, Color(0xFFF43F5E), FitnessGoal.GAIN_MUSCLE),
            GoalItem("Perda de Peso", Icons.Default.Scale, Color(0xFF3B82F6), FitnessGoal.LOSE_WEIGHT),
            GoalItem("Performance", Icons.Default.Speed, Color(0xFF8B5CF6), FitnessGoal.MAINTENANCE),
            GoalItem("Saúde", Icons.Default.Favorite, Color(0xFF10B981), FitnessGoal.HEALTH)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
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
        // Cabeçalho Profissional e Centralizado
        FormHeader(
            title = "Escolha seu avatar",
            subtitle = "Sua identidade no Fitverse. Você poderá mudar depois."
        )

        // Lista de avatares com ícones e cores temáticas pastéis
        val avatars = listOf(
            Triple("runner", Icons.Default.DirectionsRun, Color(0xFFBBDEFB)), // Azul suave
            Triple("lifter", Icons.Default.FitnessCenter, Color(0xFFFFCDD2)), // Vermelho suave
            Triple("yoga", Icons.Default.SelfImprovement, Color(0xFFC8E6C9)), // Verde suave
            Triple("generic_fit", Icons.Default.Person, Color(0xFFCFD8DC))       // Cinza suave
        )

        // Grid 2x2 para exibição simétrica dos avatares
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(avatars) { (id, icon, color) ->
                GridOptionCard(
                    text = "", // O padrão Brilliant/Flo remove o texto se o ícone for grande
                    icon = icon,
                    iconBgColor = color,
                    isSelected = state.selectedAvatarId == id, // Link com o estado
                    onClick = {
                        // Dispara a ação para atualizar o avatar selecionado
                        onAction(RegisterAction.UpdateAvatar(id))
                    }
                )
            }
        }
    }
}

@Composable
fun RegisterPageLevel(state: RegisterState, onAction: (RegisterAction) -> Unit) {
    Column {
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
            modifier = Modifier.fillMaxWidth()
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


@Composable
fun RegisterPageMacros(state: RegisterState, onAction: (RegisterAction) -> Unit) {
    // Estado local para controlar se o modo de edição está ativo
    var isEditing by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FormHeader(
            title = if (isEditing) "Ajuste suas metas" else "Seu Plano Ideal",
            subtitle = if (isEditing)
                "Insira os valores que você já utiliza no seu dia a dia."
            else "Com base nas suas escolhas, este é o seu alvo diário."
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Card de Calorias (Editável ou Estático)
        EditableMacroCard(
            title = "Calorias Diárias",
            value = state.targetCalories.toString(),
            unit = "kcal",
            icon = Icons.Default.LocalFireDepartment,
            iconColor = Color(0xFFFF9800),
            bgColor = Color(0xFFFFF3E0),
            isEditing = isEditing,
            onValueChange = { onAction(RegisterAction.UpdateCalories(it.toIntOrNull() ?: 0)) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            EditableMacroCard(
                modifier = Modifier.weight(1f),
                title = "Proteínas",
                value = state.targetProteins.toString(),
                unit = "g",
                icon = Icons.Default.Restaurant,
                iconColor = Color(0xFFE91E63),
                bgColor = Color(0xFFFCE4EC),
                isEditing = isEditing,
                onValueChange = { onAction(RegisterAction.UpdateProteins(it.toIntOrNull() ?: 0)) }
            )

            EditableMacroCard(
                modifier = Modifier.weight(1f),
                title = "Gorduras",
                value = state.targetFats.toString(),
                unit = "g",
                icon = Icons.Default.Opacity,
                iconColor = Color(0xFF2196F3),
                bgColor = Color(0xFFE3F2FD),
                isEditing = isEditing,
                onValueChange = { onAction(RegisterAction.UpdateFats(it.toIntOrNull() ?: 0)) }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Botão de Alternância (Editar / Salvar)
        TextButton(
            onClick = { isEditing = !isEditing },
            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary)
        ) {
            Icon(
                imageVector = if (isEditing) Icons.Default.Check else Icons.Default.Edit,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = if (isEditing) "Salvar ajustes" else "Prefiro definir metas manuais",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun EditableMacroCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    unit: String,
    icon: ImageVector,
    iconColor: Color,
    bgColor: Color,
    isEditing: Boolean,
    onValueChange: (String) -> Unit
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = bgColor,
        border = BorderStroke(
            if (isEditing) 2.dp else 1.dp,
            if (isEditing) iconColor else iconColor.copy(alpha = 0.2f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, null, tint = iconColor, modifier = Modifier.size(24.dp))
            Text(title, style = MaterialTheme.typography.labelMedium, color = Color.Gray)

            Spacer(modifier = Modifier.height(8.dp))

            if (isEditing) {
                TextField(
                    value = value,
                    onValueChange = onValueChange,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = iconColor
                    ),
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    modifier = Modifier.width(80.dp)
                )
            } else {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(value, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                    Text(
                        unit,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(bottom = 4.dp, start = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MacroCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    unit: String,
    icon: ImageVector,
    iconColor: Color,
    bgColor: Color
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = bgColor,
        border = BorderStroke(1.dp, iconColor.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconColor,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.DarkGray
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = value,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = Color.Black
                )
                Text(
                    text = " $unit",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
            }
        }
    }
}