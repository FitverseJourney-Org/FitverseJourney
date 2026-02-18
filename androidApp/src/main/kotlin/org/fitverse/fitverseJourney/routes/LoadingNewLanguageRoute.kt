package org.fitverse.fitverseJourney.routes

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.example.domain.model.dbLocal.language.TagLanguage
import com.example.domain.model.onboarding.OnboardingAnimationTopics
import com.example.expect.FitnessLottieAnimation
import com.example.presentation.presenter.AppPresenter

@Composable
fun LoadingNewLanguageRoute(
    appPresenter: AppPresenter,
    navController: NavController,
    backStackEntry: NavBackStackEntry
) {
    // extrai o iso enviado pela rota
    val langIso = backStackEntry.arguments?.getString("langIso")
    val targetLanguage: TagLanguage = runCatching {
        // use sua função utilitária se existir
        TagLanguage.fromIso(langIso ?: "")
    }.getOrNull() ?: TagLanguage.SYSTEM // fallback

    // controla se a operação está em andamento
    val isRunning = remember { mutableStateOf(false) }

    // bloquear o back enquanto executar
    BackHandler(enabled = isRunning.value) { /* ignorar */ }

    // UI: full-screen loading (simples)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // sua animação (Lottie) ou CircularProgressIndicator
            FitnessLottieAnimation(
                modifier = Modifier
                    .height(160.dp)
                    .fillMaxWidth(),
                animation = OnboardingAnimationTopics.AI
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Applying language…", // troque por stringResource se quiser
                color = Color.White
            )
            Spacer(modifier = Modifier.height(12.dp))
            CircularProgressIndicator(color = Color.White)
        }
    }

    // Efeito que realiza a troca e depois faz popBackStack
    LaunchedEffect(targetLanguage) {
        // marca execução
        isRunning.value = true

        val start = System.currentTimeMillis()

        // chama o método suspenso do presenter que faz a troca
        // (use switchLanguageAndShow que já gere isChangingLanguage internamente)

        runCatching {
            appPresenter.switchLanguageAndShow(targetLanguage)
        }.onFailure {
            // opcional: log ou mostrar snackbar
        }

        // garantir mínimo de 3 segundos de tela visível
        val elapsed = System.currentTimeMillis() - start
        val minMs = 3000L
        if (elapsed < minMs) {
            kotlinx.coroutines.delay(minMs - elapsed)
        }

        // retorna para a tela anterior (pop)
        navController.popBackStack()

        // limpar flag
        isRunning.value = false
    }
}

@Composable
fun LoadingNewLanguageScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ){
        Column(modifier = Modifier.padding(it)) {

        }
    }
}