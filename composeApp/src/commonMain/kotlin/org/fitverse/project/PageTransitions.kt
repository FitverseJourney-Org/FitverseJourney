package org.fitverse.project

import androidx.compose.animation.*
import androidx.compose.animation.core.EaseInOutCubic
import androidx.compose.animation.core.tween
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.scene.Scene
import kotlin.time.Clock
import kotlin.time.TimeSource

// ─────────────────────────────────────────────────────────────
// PAGE TRANSITIONS
// Extension functions sobre AnimatedContentTransitionScope<Scene<NavKey>>.
//
// Uso — encadeie com ?: no transitionSpec / popTransitionSpec:
//
//   transitionSpec = {
//       slideFromRight<Notification>()
//           ?: slideFromBottom<UserLevelUp>()
//           ?: noTransition()
//   }
//
// Cada função retorna null se a rota T não estiver envolvida,
// passando para o próximo ?: até encontrar um match.
// ─────────────────────────────────────────────────────────────

const val DURATION = 300

inline fun <reified T : NavKey> AnimatedContentTransitionScope<Scene<NavKey>>.slideFromRight(): ContentTransform? {
    if (initialState.key.toString() != T::class.simpleName &&
        targetState.key.toString()  != T::class.simpleName) return null
    return slideInHorizontally(
        animationSpec  = tween(DURATION, easing = EaseInOutCubic),
        initialOffsetX = { it },
    ) + fadeIn(tween(DURATION)) togetherWith
            slideOutHorizontally(
                animationSpec = tween(DURATION, easing = EaseInOutCubic),
                targetOffsetX = { -it },
            ) + fadeOut(tween(DURATION))
}

inline fun <reified T : NavKey> AnimatedContentTransitionScope<Scene<NavKey>>.slideFromLeft(): ContentTransform? {
    if (initialState.key.toString() != T::class.simpleName &&
        targetState.key.toString()  != T::class.simpleName) return null
    return slideInHorizontally(
        animationSpec  = tween(DURATION, easing = EaseInOutCubic),
        initialOffsetX = { -it },
    ) + fadeIn(tween(DURATION)) togetherWith
            slideOutHorizontally(
                animationSpec = tween(DURATION, easing = EaseInOutCubic),
                targetOffsetX = { it },
            ) + fadeOut(tween(DURATION))
}

inline fun <reified T : NavKey> AnimatedContentTransitionScope<Scene<NavKey>>.slideFromBottom(): ContentTransform? {
    if (initialState.key.toString() != T::class.simpleName &&
        targetState.key.toString()  != T::class.simpleName) return null
    return slideInVertically(
        animationSpec  = tween(DURATION, easing = EaseInOutCubic),
        initialOffsetY = { it },
    ) + fadeIn(tween(DURATION)) togetherWith
            slideOutVertically(
                animationSpec = tween(DURATION, easing = EaseInOutCubic),
                targetOffsetY = { it },
            ) + fadeOut(tween(DURATION))
}

inline fun <reified T : NavKey> AnimatedContentTransitionScope<Scene<NavKey>>.slideFromTop(): ContentTransform? {
    val mark = TimeSource.Monotonic.markNow()

    val match = initialState.key.toString() != T::class.simpleName && targetState.key.toString()  != T::class.simpleName
//    println(
//        "PageTransitions | slideFromTop<${T::class.simpleName}> | " +
//                "initial=${initialState.key} | " +
//                "target=${targetState.key} | " +
//                "match=$match"
//    )

    if (match) return null

    val result = slideInVertically(
        animationSpec  = tween(DURATION, easing = EaseInOutCubic),
        initialOffsetY = { -it },
    ) + fadeIn(tween(DURATION)) togetherWith
            slideOutVertically(
                animationSpec = tween(DURATION, easing = EaseInOutCubic),
                targetOffsetY = { -it },
            ) + fadeOut(tween(DURATION))

    println("slideFromTop | animated | ${mark.elapsedNow().inWholeMicroseconds}µs")

    return result
}

inline fun <reified T : NavKey> AnimatedContentTransitionScope<Scene<NavKey>>.fadeOnly(): ContentTransform? {
    if (initialState.key.toString() != T::class.simpleName &&
        targetState.key.toString()  != T::class.simpleName) return null
    return fadeIn(tween(DURATION)) togetherWith fadeOut(tween(DURATION))
}

fun noTransition(): ContentTransform =
    EnterTransition.None togetherWith ExitTransition.None