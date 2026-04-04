package com.example.presentation.screens.ui.authentication.register.pages

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.screens.ui.authentication.register.actions.RegisterAction
import com.example.presentation.screens.ui.authentication.register.components.FormHeader
import com.example.presentation.screens.ui.authentication.register.state.RegisterState

@Composable
fun RegisterPageCredentials(state: RegisterState, onAction: (RegisterAction) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        FormHeader(title = "Dados de acesso", subtitle = "Para salvar seu progresso no Fitverse.")

        OutlinedTextField(
            value = state.email,
            onValueChange = { onAction(RegisterAction.EmailChanged(it)) }, // Exemplo de action
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
            onValueChange = { onAction(RegisterAction.PasswordChanged(it)) }, // Exemplo de action
            label = { Text("Senha") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            visualTransformation = if(state.passwordShown) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            trailingIcon = {
                IconButton(
                    onClick = { onAction(RegisterAction.PasswordShown(state.passwordShown)) },
                    content = {
                        Icon(
                            imageVector = if (state.passwordShown) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (state.passwordShown) "Hide password" else "Show password"
                        )
                    }
                )
            },
            singleLine = true
        )

        // Adicionando o medidor de força da senha
        PasswordStrengthIndicator(password = state.password)
    }
}

// --- 2. INDICADOR DE FORÇA DA SENHA ---
@Composable
fun PasswordStrengthIndicator(password: String) {
    val passwordRequirements = remember(password) {
        listOf(
            PasswordRequirement("Pelo menos 8 caracteres", password.length >= 8),
            PasswordRequirement("Letras maiúsculas e minúsculas", password.any { it.isUpperCase() } && password.any { it.isLowerCase() }),
            PasswordRequirement("Pelo menos um número", password.any { it.isDigit() }),
            PasswordRequirement("Caractere especial (@, #, $, etc.)", password.any { !it.isLetterOrDigit() })
        )
    }

    val metRequirementsCount = passwordRequirements.count { it.isMet }

    val strength = when {
        password.isEmpty() -> PasswordStrength.NONE
        metRequirementsCount <= 1 -> PasswordStrength.WEAK
        metRequirementsCount == 2 -> PasswordStrength.FAIR
        metRequirementsCount == 3 -> PasswordStrength.GOOD
        else -> PasswordStrength.STRONG
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        // Barras de Força e Label
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                modifier = Modifier.weight(1f)
            ) {
                // Cria 4 segmentos de barra
                for (i in 1..4) {
                    val segmentColor by animateColorAsState(
                        targetValue = if (password.isNotEmpty() && i <= metRequirementsCount) strength.color else Color.LightGray.copy(alpha = 0.5f),
                        animationSpec = tween(300),
                        label = "SegmentColor"
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(6.dp)
                            .clip(RoundedCornerShape(50))
                            .background(segmentColor)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = strength.label,
                color = if (strength == PasswordStrength.NONE) Color.Gray else strength.color,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.width(60.dp)
            )
        }

        // Checklist de Requisitos (Esconde se a senha estiver vazia para UI mais limpa)
        if (password.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                passwordRequirements.forEach { req ->
                    RequirementItem(req)
                }
            }
        }
    }
}

@Composable
private fun RequirementItem(requirement: PasswordRequirement) {
    val color by animateColorAsState(
        targetValue = if (requirement.isMet) Color(0xFF4CAF50) else Color.Gray,
        label = "RequirementColor"
    )
    val icon = if (requirement.isMet) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = requirement.label,
            color = color,
            fontSize = 12.sp
        )
    }
}

// --- 3. MODELOS E LÓGICA DE DOMÍNIO ---
data class PasswordRequirement(
    val label: String,
    val isMet: Boolean
)

enum class PasswordStrength(val color: Color, val label: String) {
    NONE(Color.Transparent, ""),
    WEAK(Color(0xFFE53935), "Fraca"),     // Vermelho
    FAIR(Color(0xFFFFB300), "Razoável"),  // Amarelo/Laranja
    GOOD(Color(0xFF43A047), "Boa"),       // Verde claro
    STRONG(Color(0xFF1B5E20), "Forte")    // Verde escuro
}
