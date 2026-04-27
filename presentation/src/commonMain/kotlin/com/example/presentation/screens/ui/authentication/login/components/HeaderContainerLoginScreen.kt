package com.example.presentation.screens.ui.authentication.login.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.domain.models.local.language.AppLanguageItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeaderContainerLoginScreen(
    showLanguageScreen: () -> Unit,
    currentAppLanguageItem: AppLanguageItem // O ViewModel já te entrega o objeto pronto!
) {
    val colors = MaterialTheme.colorScheme

    TopAppBar(
        modifier = Modifier.padding(horizontal = 5.dp),
        title = {},
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            actionIconContentColor = colors.onBackground
        ),
        actions = {
            ElevatedButton(
                onClick = showLanguageScreen,
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = colors.surfaceVariant,
                    contentColor = colors.onSurfaceVariant,
                ),
                contentPadding = PaddingValues(10.dp),
                shape = RoundedCornerShape(10.dp),
                elevation = ButtonDefaults.elevatedButtonElevation(
                    defaultElevation = 2.dp,
                    pressedElevation = 6.dp
                )
            ) {
                // Use o currentLanguage diretamente aqui!
                LanguageChooser(
                    flagRes = currentAppLanguageItem.flagRes,
                    code = currentAppLanguageItem.name
                )
            }
        }
    )
}