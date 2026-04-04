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
import com.example.domain.model.authentication.register.RegisterAvatar
import com.example.presentation.screens.ui.authentication.register.actions.RegisterAction
import com.example.presentation.screens.ui.authentication.register.components.FormHeader
import com.example.presentation.screens.ui.authentication.register.components.GridOptionCard
import com.example.presentation.screens.ui.authentication.register.state.RegisterState
import fitversejourneyapp.presentation.generated.resources.Res
import fitversejourneyapp.presentation.generated.resources.ico_deadpool
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
                Avatar(RegisterAvatar.SPIDERMAN, "Spider-Man", Res.drawable.ico_marvel),
                Avatar(RegisterAvatar.DEADPOOL, "Deadpool", Res.drawable.ico_deadpool),
                Avatar(RegisterAvatar.IRON_MAN, "Iron Man", Res.drawable.ico_avengers),
                Avatar(RegisterAvatar.WOLVERINE, "Wolverine", Res.drawable.ico_logan),
                Avatar(RegisterAvatar.BLACK_PANTHER, "Black Panther", Res.drawable.ico_panther)
            )
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(10.dp),
            modifier = Modifier.fillMaxSize().padding(10.dp)
        ) {
            items(avatars) { avatar ->
                GridOptionCard(
                    text = avatar.name,
                    iconResource = avatar.imageRes,
                    iconBgColor = Color.White.copy(alpha = 0.15f),
                    iconTint = Color.White,
                    isSelected = state.selectedAvatar == avatar.type,
                    onClick = { onAction(RegisterAction.AvatarChanged(avatar.type)) },
                    openDialogStatusAvatar = {
                        onAction(RegisterAction.DialogStatusAvatar(value = true))
                    }
                )
            }
        }
    }
}

data class Avatar(
    val type: RegisterAvatar, // Mudamos de String para o Enum que o UseCase exige
    val name: String,
    val imageRes: DrawableResource,
    val isLocked: Boolean = false
)
