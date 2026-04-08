package com.example.presentation.screens.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.presentation.theme.PADDING_TOPAPPBAR_DEFAULT_HORIZONTAL
import com.example.presentation.theme.PADDING_TOPAPPBAR_DEFAULT_VERTICAL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitverseTopAppBar(
    title: String,
    subtitle: @Composable () -> Unit? = { null },
    onBack: () -> Unit,
    actions: @Composable () -> Unit? = { null },
    scrollBehavior: TopAppBarScrollBehavior? = null
) {
    val cs = MaterialTheme.colorScheme

    // Envelopamos tudo em uma Column para anexar a barra no final
    Column(
        modifier = Modifier
    ) {
        TopAppBar(
            modifier = Modifier
                .padding(
                    horizontal = PADDING_TOPAPPBAR_DEFAULT_HORIZONTAL,
                    vertical = PADDING_TOPAPPBAR_DEFAULT_VERTICAL
                ),
            title = {
                Column(
                    modifier = Modifier.padding(start = 10.dp)
                ) {
                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = cs.onBackground,
                        letterSpacing = 1.sp
                    )
                    subtitle()
                }
            },
            windowInsets = WindowInsets(0, 0, 0, 0),
            navigationIcon = {
                FitverseIconBack(
                    onBack = onBack
                )
            },
            actions = {
                actions()
            },
            // Deixamos transparente para a Column ditar a cor de fundo real
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                scrolledContainerColor = Color.Transparent
            ),
            scrollBehavior = scrollBehavior
        )
    }
}