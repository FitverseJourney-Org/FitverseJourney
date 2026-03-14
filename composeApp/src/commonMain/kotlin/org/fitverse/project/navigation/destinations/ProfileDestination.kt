package org.fitverse.project.navigation.destinations

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.presentation.components.background.PremiumGamifiedBackground
import com.example.presentation.screens.ui.main.profile.ProfileScreenPro


@Composable
fun ProfileDestination(onSignOut: () -> Unit, navigateToPlans: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()){
        PremiumGamifiedBackground()
        ProfileScreenPro(
            navigateToPlans = {
                navigateToPlans()
            },
            onChoosePackage = {

            }
        )
    }
}