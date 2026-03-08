package com.example.presentation.screens.ui.authentication.register.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.domain.model.authentication.register.RegisterAction
import com.example.presentation.screens.ui.authentication.register.components.GenreSelection
import com.example.presentation.screens.ui.authentication.register.components.HeightRangeSelector
import com.example.presentation.screens.ui.authentication.register.components.WeightRangeSelector
import com.example.presentation.screens.ui.authentication.register.state.RegisterState
import com.example.presentation.screens.ui.authentication.login.components.FitverseOutlinedTextField

@Composable
fun RegisterPageProfile(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Let´s create your account",
            color = Color.White,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            fontWeight = FontWeight.SemiBold,
            fontFamily = FontFamily.SansSerif
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Please, fill in the form below to get started.",
            color = Color.LightGray,
            fontSize = MaterialTheme.typography.titleMedium.fontSize,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily.SansSerif
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Name
        FitverseOutlinedTextField(
            value = state.name,
            onValueChange = { onAction(RegisterAction.NameChanged(it)) },
            label = "Name",
            placeholder = "John",
            isError = state.nameErrors.isNotEmpty(),
            errorText = state.nameErrors.firstOrNull()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Age
        FitverseOutlinedTextField(
            value = state.age,
            onValueChange = { onAction(RegisterAction.AgeChanged(it)) },
            label = "Age",
            placeholder = "18",
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            isError = state.ageErrors.isNotEmpty(),
            errorText = state.ageErrors.firstOrNull()
        )

        Spacer(modifier = Modifier.height(10.dp))

        GenreSelection(
            selectedGenre = state.gender,
            onGenreSelected = {
                onAction(RegisterAction.GenderChanged(it))
            },
            errorList = state.genderErrors
        )

        Spacer(modifier = Modifier.height(10.dp))

        HeightRangeSelector(
            height = state.height,
            onHeightChange = {
                onAction(RegisterAction.HeightChanged(it))
            }
        )

        Spacer(Modifier.height(24.dp))

        WeightRangeSelector(
            weight = state.weight,
            onWeightChange = {
                onAction(RegisterAction.WeightChanged(it))
            }
        )
    }
}