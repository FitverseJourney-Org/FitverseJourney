package com.example.presentation.screens.ui.onboarding

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
import com.example.domain.model.onboarding.OnboardingAnimationTopics
import com.example.domain.model.onboarding.OnboardingPage
import com.example.expect.FitnessLottieAnimation
import com.example.presentation.components.background.ModernFitverseBackground
import com.example.presentation.screens.ui.authentication.login.components.AnimatedLoginBackground
import com.example.presentation.screens.ui.onboarding.viewmodel.OnboardingViewModel
import com.example.presentation.screens.ui.onboarding.state.OnboardingState
import fitversejourneyapp.presentation.generated.resources.Res
import fitversejourneyapp.presentation.generated.resources.*
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
    emitToLogin: () -> Unit
) {
    val onboardingPages = listOf(
        OnboardingPage(
            title       = stringResource(Res.string.onboarding_title_1),
            description = stringResource(Res.string.onboarding_description_1),
            animation   = OnboardingAnimationTopics.WORKOUT
        ),
        OnboardingPage(
            title       = stringResource(Res.string.onboarding_title_2),
            description = stringResource(Res.string.onboarding_description_2),
            animation   = OnboardingAnimationTopics.NUTRITION
        ),
        OnboardingPage(
            title       = stringResource(Res.string.onboarding_title_3),
            description = stringResource(Res.string.onboarding_description_3),
            animation   = OnboardingAnimationTopics.AI
        ),
        OnboardingPage(
            title       = stringResource(Res.string.onboarding_title_4),
            description = stringResource(Res.string.onboarding_description_4),
            animation   = OnboardingAnimationTopics.COMMUNITY
        )
    )
    val lastIndex = onboardingPages.lastIndex

    val pagerState = rememberPagerState(
        initialPage = state.currentPage,
        pageCount   = { onboardingPages.size }
    )

    LaunchedEffect(state.currentPage) {
        if (state.currentPage <= lastIndex) {
            pagerState.animateScrollToPage(state.currentPage)
        }
    }

    Scaffold(containerColor = BackgroundColor) { innerPadding ->
        ModernFitverseBackground()
        OnboardingContent(
            modifier        = Modifier.padding(innerPadding),
            pagerState      = pagerState,
            onboardingPages = onboardingPages,
            viewmodel       = viewmodel,
            nextPage        = { viewmodel.nextPage(lastIndex) },
            skipToLastPage  = { viewmodel.skipToLastPage(lastIndex) },
            toNewAccount    = emitToNewAccount,
            toLogin         = emitToLogin
        )
    }
}

// ── Content ───────────────────────────────────────────────────────────────────
@Composable
fun OnboardingContent(
    modifier: Modifier,
    pagerState: PagerState,
    nextPage: () -> Unit,
    skipToLastPage: () -> Unit,
    onboardingPages: List<OnboardingPage>,
    viewmodel: OnboardingViewModel,
    toNewAccount: () -> Unit,
    toLogin: () -> Unit
) {
    val lastIndex = onboardingPages.lastIndex

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage to pagerState.isScrollInProgress }
            .distinctUntilChanged()
            .collect { (pageIndex, isScrolling) ->
                if (!isScrolling) viewmodel.setCurrentPage(pageIndex, lastIndex)
            }
    }

    Column(
        modifier= modifier
            .fillMaxSize()
            .padding(horizontal = 28.dp),
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
        Button(
            onClick  = {
                if (pagerState.currentPage == lastIndex) {
                    toNewAccount()
                }
                else {
                    nextPage()
                }
           },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape  = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
        ) {
            Text(
                text       = if (pagerState.currentPage == lastIndex) "Create Free Account" else "Continue",
                fontSize   = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color      = TextPrimary
            )
        }

        Spacer(Modifier.height(14.dp))

        // ── Already have account ──────────────────────────────────────────────
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(color = TextSecondary, fontSize = 13.sp)) {
                    append("Already have an account? ")
                }
                withStyle(SpanStyle(color = PrimaryBlue, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)) {
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
                withStyle(SpanStyle(color = PrimaryBlue, fontSize = 11.sp)) {
                    append("Terms")
                }
                withStyle(SpanStyle(color = TextMuted, fontSize = 11.sp)) {
                    append(" and ")
                }
                withStyle(SpanStyle(color = PrimaryBlue, fontSize = 11.sp)) {
                    append("Privacy Policy")
                }
            },
            textAlign = TextAlign.Center,
            lineHeight = 16.sp
        )

        Spacer(Modifier.height(24.dp))
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
                .clip(RoundedCornerShape(28.dp))
                .background(cs.surface.copy(alpha = 0.6f)),
            contentAlignment = Alignment.Center
        ) {
            FitnessLottieAnimation(
                modifier  = Modifier.fillMaxSize(0.72f),
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
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        repeat(totalPages) { index ->
            val isSelected = index == currentPage
            val color by animateColorAsState(
                targetValue = if (isSelected) PrimaryBlue else TextSecondary.copy(alpha = 0.3f),
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
                .background(PrimaryBlue)
                .padding(horizontal = 9.dp, vertical = 3.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text       = "Journey",
                fontSize   = 13.sp,
                fontWeight = FontWeight.Bold,
                color      = TextPrimary
            )
        }
    }
}