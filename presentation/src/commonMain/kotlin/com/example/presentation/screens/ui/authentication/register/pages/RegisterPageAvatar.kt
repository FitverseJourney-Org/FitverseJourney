package com.example.presentation.screens.ui.authentication.register.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.presentation.screens.ui.authentication.register.actions.RegisterAction
import com.example.presentation.screens.ui.authentication.register.components.FormHeader
import com.example.presentation.screens.ui.authentication.register.components.GridOptionCard
import com.example.presentation.screens.ui.authentication.register.state.RegisterState
import fitversejourneyapp.presentation.generated.resources.Res
import fitversejourneyapp.presentation.generated.resources.ico_assasin
import fitversejourneyapp.presentation.generated.resources.ico_avengers
import fitversejourneyapp.presentation.generated.resources.ico_logan
import fitversejourneyapp.presentation.generated.resources.ico_marvel
import fitversejourneyapp.presentation.generated.resources.ico_panther
import org.jetbrains.compose.resources.DrawableResource

@Composable
fun RegisterPageAvatar(state: RegisterState, onAction: (RegisterAction) -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        FormHeader(
            title = "Escolha seu avatar",
            subtitle = "Sua identidade no Fitverse. Você poderá mudar depois."
        )

        // Lista tipada em vez de Triple
        val avatars = remember {
            listOf(
                Avatar("spiderman", "Spider-Man", Res.drawable.ico_marvel),
                Avatar("assassin", "Deadpool", Res.drawable.ico_assasin),
                Avatar("avengers", "Iron Man", Res.drawable.ico_avengers),
                Avatar("logan", "Wolverine", Res.drawable.ico_logan),
                Avatar("panther", "Black Panther", Res.drawable.ico_panther)
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(10.dp),
            modifier = Modifier.fillMaxSize().padding(10.dp)
        ) {
            items(
                items = avatars,
                key = { it.id } // Boa prática para performance em listas
            ) { avatar ->
                GridOptionCard(
                    text = avatar.name,
                    iconResource = avatar.imageRes,
                    iconBgColor = Color.White.copy(alpha = 0.15f),
                    iconTint = Color.White,
                    isSelected = state.selectedAvatarId == avatar.id,
                    onClick = { onAction(RegisterAction.AvatarChanged(avatar.id)) },
                    openDialogStatusAvatar = {
                        onAction(RegisterAction.DialogStatusAvatar(value = true))
                    }
                )
            }
        }
    }
}

data class Avatar(
    val id: String,
    val name: String,
    val imageRes: DrawableResource,
    val isLocked: Boolean = false // Exemplo: para futuras mecânicas de XP/Gamificação
)
