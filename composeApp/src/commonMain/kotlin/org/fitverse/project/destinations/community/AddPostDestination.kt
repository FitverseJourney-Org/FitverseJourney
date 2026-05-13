package org.fitverse.project.destinations.community

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.presentation.components.background.ModernFitverseBackground
import com.example.presentation.ui.community.AddNewPostScreen

@Composable
fun AddPostDestination(toBack: () -> Unit) {

    Box(modifier = Modifier.fillMaxSize()){
        ModernFitverseBackground()
        AddNewPostScreen(
            onBackClick = {
                toBack()
            },
            onPostSuccess = { }
        )
    }
}