package org.fitverse.project

import androidx.compose.ui.window.ComposeUIViewController
import com.example.expect.AppDataStoreDb
import com.example.expect.DatabaseDriverFactory
import org.fitverse.project.navigation.FitverseRootNavigation


fun MainViewController() = ComposeUIViewController {
    FitverseRootNavigation(
        dbHelper = DatabaseDriverFactory(),
        appDataStore = AppDataStoreDb
    )
}