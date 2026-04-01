package org.fitverse.project.destinations.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.screens.ui.authentication.home.HomeAuthScreen
import com.example.presentation.screens.widgets.FitVerseButton
import fitversejourneyapp.composeapp.generated.resources.Res
import fitversejourneyapp.composeapp.generated.resources.icon_person
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource



@Composable
fun HomeDestination(
    toLogin: () -> Unit,
    toRegister: () -> Unit
) {
    HomeAuthScreen(
        onSignUp = {
            toRegister()
        },
        onLogin = {
            toLogin()
        },
        onGoogleSignIn = {},
        onFacebookSignIn = {},
        onAppleSignIn = {},
    )
}

