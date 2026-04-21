package org.fitverse.project

import androidx.compose.ui.window.ComposeUIViewController
import org.fitverse.project.navigation.FitverseRootNavigation


fun MainViewController() = ComposeUIViewController {
    FitverseRootNavigation(
    )
}