package org.fitverse.presentation.ui.onboarding

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.fitverse.domain.models.onboarding.LottieAnimation
import org.fitverse.presentation.expect.PlatformLottieAnimation
import org.fitverse.domain.models.onboarding.OnboardingPage
import org.fitverse.presentation.navigationState.OnboardingNavigation
import org.fitverse.presentation.ui.onboarding.viewmodel.OnboardingViewModel
import org.fitverse.presentation.ui.onboarding.state.OnboardingState
import org.fitverse.presentation.widgets.FitverseButton
import fitversejourneyapp.presentation.generated.resources.Res
import fitversejourneyapp.presentation.generated.resources.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import org.jetbrains.compose.resources.stringResource

// ── Palette ───────────────────────────────────────────────────────────────────
private val BackgroundColor   = Color(0xFF0E1119)
private val PrimaryBlue       = Color(0xFF3B6CF6)
private val TextPrimary       = Color(0xFFFFFFFF)
private val TextSecondary     = Color(0xFF8A8FA8)
private val TextMuted         = Color(0xFF55596E)

// ── Screen ────────────────────────────────────────────────────────────────────
@Composable
fun OnboardingScreen(
    state: OnboardingState,
    viewmodel: OnboardingViewModel,
    emitToTrial: () -> Unit,
    emitToNewAccount: () -> Unit,
    emitToLogin: () -> Unit,
    toTrial: () -> Unit,
    toNewAccount: () -> Unit,
    toLogin: () -> Unit
) {
    val onboardingPages = listOf(
        OnboardingPage(
            title       = stringResource(Res.string.onboarding_title_1),
            description = stringResource(Res.string.onboarding_description_1),
            animation   = LottieAnimation.WORKOUT
        ),
        OnboardingPage(
            title       = stringResource(Res.string.onboarding_title_2),
            description = stringResource(Res.string.onboarding_description_2),
            animation   = LottieAnimation.NUTRITION
        ),
        OnboardingPage(
            title       = stringResource(Res.string.onboarding_title_3),
            description = stringResource(Res.string.onboarding_description_3),
            animation   = LottieAnimation.AI
        ),
        OnboardingPage(
            title       = stringResource(Res.string.onboarding_title_4),
            description = stringResource(Res.string.onboarding_description_4),
            animation   = LottieAnimation.COMMUNITY
        )
    )
    val lastIndex = onboardingPages.lastIndex

    val pagerState = rememberPagerState(
        initialPage = state.currentPage,
        pageCount   = { onboardingPages.size }
    )

    LaunchedEffect(true){
        viewmodel.navigationState.collectLatest {
            when(it) {
                is OnboardingNavigation.ToTrial -> {
                    toTrial()
                }
                is OnboardingNavigation.ToNewAccount -> {
                    toTrial()
                }
                is OnboardingNavigation.ToLogin -> {
                    toTrial()
                }
            }
        }
    }

    LaunchedEffect(state.currentPage) {
        if (state.currentPage <= lastIndex) {
            pagerState.animateScrollToPage(state.currentPage)
        }
    }

    // 1. Sincroniza o scroll manual do Pager com o ViewModel
    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage to pagerState.isScrollInProgress }
            .distinctUntilChanged()
            .collect { (pageIndex, isScrolling) ->
                if (!isScrolling) viewmodel.setCurrentPage(pageIndex, lastIndex)
            }
    }

    // 2. NOVO: Timer de 5 segundos para avanço automático
    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        // Só inicia o timer se o usuário não estiver interagindo manualmente
        if (!pagerState.isScrollInProgress) {
            delay(5000L)

            val nextPageIndex = if (pagerState.currentPage < lastIndex) {
                pagerState.currentPage + 1
            } else {
                0 // Volta para a primeira página se estiver na última
            }

            // Atualiza o ViewModel, que por sua vez disparará a animação no OnboardingScreen
            viewmodel.setCurrentPage(nextPageIndex, lastIndex)
        }
    }

    OnboardingScreenContent(
        pagerState      = pagerState,
        onboardingPages = onboardingPages,
        viewmodel       = viewmodel,
        nextPage        = { viewmodel.nextPage(lastIndex) },
        skipToLastPage  = { viewmodel.skipToLastPage(lastIndex) },
        toNewAccount    = emitToNewAccount,
        toLogin         = emitToLogin,
        lastIndex       = lastIndex
    )
}

// ── Content ───────────────────────────────────────────────────────────────────
@Composable
fun OnboardingScreenContent(
    viewmodel: OnboardingViewModel,
    lastIndex: Int,
    pagerState: PagerState,
    nextPage: () -> Unit,
    skipToLastPage: () -> Unit,
    onboardingPages: List<OnboardingPage>,
    toNewAccount: () -> Unit,
    toLogin: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    Scaffold(
        containerColor = Color.Transparent
    ){
        Column(
            modifier= Modifier.fillMaxSize().padding(horizontal = 28.dp).padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(32.dp))

            // ── Logo ──────────────────────────────────────────────────────────────
            AppLogo()

            Spacer(Modifier.height(6.dp))

            Text(
                text      = "Intelligent fitness companion",
                fontSize  = 13.sp,
                color     = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(28.dp))

            // ── Pager ─────────────────────────────────────────────────────────────
            HorizontalPager(
                modifier = Modifier.weight(1f),
                state    = pagerState
            ) { pageIndex ->
                OnboardingPageContent(page = onboardingPages[pageIndex])
            }

            // ── Dots ──────────────────────────────────────────────────────────────
            Spacer(Modifier.height(20.dp))
            PagerDots(
                currentPage = pagerState.currentPage,
                totalPages  = onboardingPages.size
            )

            Spacer(Modifier.height(20.dp))

            // ── CTA Button ────────────────────────────────────────────────────────
            FitverseButton(
                text = if (pagerState.currentPage == lastIndex) "Create Free Account" else "Continue",
                onClick = {
                    if (pagerState.currentPage == lastIndex) {
                        toNewAccount()
                    } else {
                        nextPage()
                    }
                }
            )

            Spacer(Modifier.height(14.dp))

            // ── Already have account ──────────────────────────────────────────────
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = TextSecondary, fontSize = 13.sp)) {
                        append("Already have an account? ")
                    }
                    withStyle(SpanStyle(color = cs.primary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)) {
                        append("Sign In")
                    }
                },
                modifier = Modifier.clickable {
                    toLogin()
                }
            )

            Spacer(Modifier.height(12.dp))

            // ── Terms ─────────────────────────────────────────────────────────────
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = TextMuted, fontSize = 11.sp)) {
                        append("By continuing, you agree to our ")
                    }
                    withStyle(SpanStyle(color = cs.primary, fontSize = 11.sp)) {
                        append("Terms")
                    }
                    withStyle(SpanStyle(color = TextMuted, fontSize = 11.sp)) {
                        append(" and ")
                    }
                    withStyle(SpanStyle(color = cs.primary, fontSize = 11.sp)) {
                        append("Privacy Policy")
                    }
                },
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )

            Spacer(Modifier.height(24.dp))
        }

    }
}

// ── Page content ──────────────────────────────────────────────────────────────
@Composable
fun OnboardingPageContent(page: OnboardingPage) {
    val cs = MaterialTheme.colorScheme
    Column(
        modifier            = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        // Card azul sólido
        Box(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(28.dp)),
            contentAlignment = Alignment.Center
        ) {
            PlatformLottieAnimation(
                modifier  = Modifier.fillMaxSize(0.85f),
                animation = page.animation
            )
        }

        Spacer(Modifier.height(32.dp))

        Text(
            text       = page.title,
            fontSize   = 22.sp,
            fontWeight = FontWeight.Bold,
            color      = TextPrimary,
            textAlign  = TextAlign.Center
        )

        Spacer(Modifier.height(10.dp))

        Text(
            text       = page.description,
            fontSize   = 14.sp,
            color      = TextSecondary,
            textAlign  = TextAlign.Center,
            lineHeight = 22.sp,
            modifier   = Modifier.padding(horizontal = 12.dp)
        )
    }
}

// ── Dots ──────────────────────────────────────────────────────────────────────
@Composable
private fun PagerDots(currentPage: Int, totalPages: Int) {
    val cs = MaterialTheme.colorScheme

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        repeat(totalPages) { index ->
            val isSelected = index == currentPage
            val color by animateColorAsState(
                targetValue = if (isSelected) cs.primary else TextSecondary.copy(alpha = 0.3f),
                label       = "DotColor"
            )
            val width by animateDpAsState(
                targetValue = if (isSelected) 22.dp else 8.dp,
                label       = "DotWidth"
            )
            Box(
                modifier = Modifier
                    .padding(horizontal = 3.dp)
                    .height(8.dp)
                    .width(width)
                    .clip(RoundedCornerShape(4.dp))
                    .background(color)
            )
        }
    }
}

// ── Logo ──────────────────────────────────────────────────────────────────────
@Composable
private fun AppLogo() {
    val cs = MaterialTheme.colorScheme

    Row(
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text       = "FitVerse",
            fontSize   = 22.sp,
            fontWeight = FontWeight.Bold,
            color      = TextPrimary
        )
        Spacer(Modifier.width(6.dp))
        Box(
            modifier         = Modifier
                .clip(RoundedCornerShape(7.dp))
                .background(cs.primary)
                .padding(horizontal = 9.dp, vertical = 3.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text       = "Journey",
                fontSize   = 13.sp,
                fontWeight = FontWeight.Bold,
                color      = Color.Black
            )
        }
    }
}