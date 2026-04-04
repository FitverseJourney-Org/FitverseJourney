package com.example.presentation.screens.ui.authentication.register.pages

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.BakeryDining
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Opacity
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.rounded.DirectionsRun
import androidx.compose.material.icons.rounded.Functions
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.PieChart
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.screens.ui.authentication.register.actions.RegisterAction
import com.example.presentation.screens.ui.authentication.register.components.FormHeader
import com.example.presentation.screens.ui.authentication.register.state.RegisterState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterPageMacros(state: RegisterState, onAction: (RegisterAction) -> Unit) {
    var isEditing by remember { mutableStateOf(false) }
    var showInfoSheet by remember { mutableStateOf(false) } // Estado para o modal
    val sheetState = rememberModalBottomSheetState()
    val colors = MaterialTheme.colorScheme

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
        modifier = Modifier.fillMaxSize()
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(32.dp))

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
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

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center // Isso garante que o conteúdo fique 100% no meio
        ) {
            MacroCardRefined(
                modifier = Modifier.fillMaxWidth(0.65f),
                title = "Calorias",
                value = state.targetCalories,
                unit = "kcal",
                icon = Icons.Default.LocalFireDepartment,
                accentColor = Color(0xFFFF9800),
                isEditing = isEditing,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        if(!isEditing){
                            onAction(RegisterAction.UpdateMacros(state.macroGoals.copy(calories = newValue.toIntOrNull() ?: 0)))
                        }else{
                            onAction(RegisterAction.UpdateCalories(newValue))
                        }
                    }

                }
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
                value = state.targetCarbs,
                unit = "g",
                icon = Icons.Default.BakeryDining,
                accentColor = Color(0xFF4CAF50),
                isEditing = isEditing,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        if(!isEditing){
                            onAction(RegisterAction.UpdateMacros(macros = state.macroGoals.copy(carbohydrates = newValue.toIntOrNull() ?: 0)))
                        }else{
                            onAction(RegisterAction.UpdateCarbs(newValue))
                        }
                    }
                }
            )
            MacroCardRefined(
                modifier = Modifier.weight(1f),
                title = "Proteína",
                value = state.targetProteins,
                unit = "g",
                icon = Icons.Default.Restaurant,
                accentColor = Color(0xFFE91E63),
                isEditing = isEditing,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        if(!isEditing){
                            onAction(RegisterAction.UpdateMacros(state.macroGoals.copy(proteins = newValue.toIntOrNull() ?: 0)))
                        }else{
                            onAction(RegisterAction.UpdateProteins(newValue))
                        }
                    }
                }
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
                value = state.targetFats,
                unit = "g",
                icon = Icons.Default.Opacity,
                accentColor = Color(0xFF2196F3),
                isEditing = isEditing,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        if(!isEditing){
                            onAction(RegisterAction.UpdateMacros(state.macroGoals.copy(fats = newValue.toIntOrNull() ?: 0)))
                        }else{
                            onAction(RegisterAction.UpdateFats(newValue))
                        }

                    }
                }
            )

            MacroCardRefined(
                modifier = Modifier.weight(1f),
                title = "Água",
                value = state.targetWater,
                unit = "ml",
                icon = Icons.Default.WaterDrop,
                accentColor = Color(0xFF00BCD4),
                isEditing = isEditing,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        if(!isEditing){
                            onAction(RegisterAction.UpdateMacros(state.macroGoals.copy(waterMl = newValue.toIntOrNull() ?: 0)))
                        }else{
                            onAction(RegisterAction.UpdateWater(newValue))
                        }
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Botão de Ajuste
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Botão 1: Alternar Modo de Edição
            OutlinedButton(
                onClick = { isEditing = !isEditing },
                shape = CircleShape,
                colors = ButtonDefaults.outlinedButtonColors(
                    // Fundo Glass: Usa a cor com baixa opacidade. Mais forte se estiver ativo.
                    containerColor = if (isEditing)
                        colors.primary.copy(alpha = 0.10f)
                    else
                        colors.onSurface.copy(alpha = 0.08f),
                    // Cor do conteúdo (ícone e texto)
                    contentColor = if (isEditing) colors.primary else colors.onSurface
                ),
                border = BorderStroke(
                    width = 1.dp,
                    // Borda Glass: Fina e semi-transparente para dar o brilho do vidro
                    color = if (isEditing)
                        colors.primary.copy(alpha = 0.50f)
                    else
                        colors.onSurface.copy(alpha = 0.15f)
                ),
                // Opcional, mas recomendado: padroniza a largura dos botões na coluna
                modifier = Modifier.fillMaxWidth(0.85f)
            ) {
                Icon(
                    imageVector = if (isEditing) Icons.Default.Check else Icons.Default.Tune,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(if (isEditing) "SALVAR METAS" else "AJUSTAR MANUALMENTE")
            }

            // Botão 2: Ajuste Automático
            OutlinedButton(
                onClick = {
                    onAction(RegisterAction.AutoAdjustMacros)
                },
                shape = CircleShape,
                colors = ButtonDefaults.outlinedButtonColors(
                    // Fundo Glass neutro
                    containerColor = colors.onSurface.copy(alpha = 0.02f),
                    contentColor = colors.onSurface
                ),
                border = BorderStroke(
                    width = 1.dp,
                    // Borda sutil
                    color = colors.onSurface.copy(alpha = 0.15f)
                ),
                modifier = Modifier.fillMaxWidth(0.85f)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoFixHigh,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("AJUSTAR AUTOMATICAMENTE")
            }
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

