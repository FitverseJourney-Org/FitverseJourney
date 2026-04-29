package com.example.expect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Variável global que guarda o idioma atual.
 * É atualizada em [AppEnvironment] sempre que o StateFlow do ViewModel emite.
 * O [key()] dentro de [AppEnvironment] garante recomposição total da árvore.
 */
var currentLocale by mutableStateOf<String?>(null)
    private set

expect object LocalAppLocale {
    val current: String @Composable get
    @Composable infix fun provides(value: String?): ProvidedValue<*>
}

/**
 * Wrapper raiz da UI.
 *
 * Recebe [language] diretamente do ViewModel (via collectAsState),
 * atualiza [currentLocale] e força recomposição completa via [key].
 *
 * @param language Código ISO do idioma atual (ex: "pt", "en").
 */
@Composable
fun AppEnvironment(
    language: String,
    content : @Composable () -> Unit,
) {
    // Sincroniza o estado global com o valor vindo do ViewModel
    currentLocale = language

    CompositionLocalProvider(
        LocalAppLocale provides language
    ) {
        // key() destrói e recria a árvore inteira ao trocar o idioma,
        // garantindo que stringResource use o locale correto
        androidx.compose.runtime.key(language) {
            content()
        }
    }
}