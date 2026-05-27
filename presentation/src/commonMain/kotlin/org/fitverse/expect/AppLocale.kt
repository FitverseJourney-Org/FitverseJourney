package org.fitverse.presentation.expect

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

// ─────────────────────────────────────────────────────────────
// APP LOCALE
// Gerenciamento de locale para suporte a múltiplos idiomas.
// actual: Android → LocalConfiguration / AppCompatDelegate
//         iOS     → NSLocale / Bundle
// ─────────────────────────────────────────────────────────────

/**
 * Estado global do idioma atual.
 *
 * Atualizado por [AppEnvironment] sempre que o ViewModel emite um novo locale.
 * O [androidx.compose.runtime.key] dentro de [AppEnvironment] garante
 * recomposição total da árvore ao trocar de idioma.
 */
var currentLocale: String? by mutableStateOf(null)
    private set

/**
 * CompositionLocal para o idioma ativo.
 *
 * ```
 * // Leitura:
 * val locale = LocalAppLocale.current
 *
 * // Provisão (feita automaticamente por AppEnvironment):
 * LocalAppLocale provides "pt"
 * ```
 */
expect object LocalAppLocale {

    /** Idioma ativo no contexto Compose atual (ex: `"pt"`, `"en"`). */
    val current: String @Composable get

    /** Provê [value] como idioma ativo para a árvore de composição filha. */
    @Composable
    infix fun provides(value: String?): ProvidedValue<*>
}

// ─────────────────────────────────────────────────────────────
// APP ENVIRONMENT
// Wrapper raiz que aplica e propaga o locale na árvore de UI.
// ─────────────────────────────────────────────────────────────

/**
 * Wrapper raiz da UI que propaga o [language] para toda a árvore Compose.
 *
 * - Sincroniza [currentLocale] com o valor emitido pelo ViewModel.
 * - Usa [CompositionLocalProvider] para expor [LocalAppLocale].
 * - Força recomposição completa via [androidx.compose.runtime.key]
 *   ao trocar de idioma, garantindo que `stringResource` use o locale correto.
 *
 * ```kotlin
 * val language by viewModel.language.collectAsState()
 *
 * AppEnvironment(language = language) {
 *     AppNavGraph()
 * }
 * ```
 *
 * @param language Código ISO do idioma atual (ex: `"pt"`, `"en"`).
 * @param content  Conteúdo da aplicação.
 */
@Composable
fun AppEnvironment(
    language: String,
    content: @Composable () -> Unit,
) {
    currentLocale = language

    CompositionLocalProvider(LocalAppLocale provides language) {
        key(language) {
            content()
        }
    }
}