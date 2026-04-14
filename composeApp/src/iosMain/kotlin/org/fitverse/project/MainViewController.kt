package org.fitverse.project

import androidx.compose.ui.window.ComposeUIViewController
import com.example.data.database.sqldelight.DatabaseFactory
import org.fitverse.project.navigation.FitverseRootNavigation


fun MainViewController() = ComposeUIViewController {
    FitverseRootNavigation(
        dbHelper = DatabaseFactory(),
    )
}