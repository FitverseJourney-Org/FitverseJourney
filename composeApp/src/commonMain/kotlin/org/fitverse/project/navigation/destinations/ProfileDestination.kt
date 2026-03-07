package org.fitverse.project.navigation.destinations

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.presentation.components.background.PremiumGamifiedBackground
import com.example.presentation.screens.ui.main.profile.ProfileScreenV2
import com.example.presentation.screens.ui.onboarding.components.PremiumGamifiedBackground


@Composable
fun ProfileDestination(onSignOut: () -> Unit, navigateToPlans: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()){
        ProfileScreenV2(
            modifier = Modifier,
            navigateToPlans = {
                navigateToPlans()
            },
            onChoosePackage = {

            }
        )
    }
}