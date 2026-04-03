package com.example.presentation.screens.ui.authentication.register.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.presentation.screens.ui.authentication.login.components.FitverseOutlinedTextField
import com.example.presentation.screens.ui.authentication.register.actions.RegisterAction
import com.example.presentation.screens.ui.authentication.register.components.FormHeader
import com.example.presentation.screens.ui.authentication.register.state.RegisterState
import com.example.presentation.screens.widgets.FitVerseSpacer

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
