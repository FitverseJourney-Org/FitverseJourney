package org.fitverse.project.navigation.destinations

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavKey
import com.example.presentation.screens.ui.device.SettingsScreenInMemoryEnhancedV2_Extended

@Composable
fun DevicesDestination(backStack: () -> NavKey?) {
    SettingsScreenInMemoryEnhancedV2_Extended(onClickBack = { backStack() })
}