package com.example.presentation.screens.ui.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.domain.model.onboarding.OnboardingAnimationTopics
import com.example.domain.model.onboarding.OnboardingPage
import com.example.expect.FitnessLottieAnimation
import com.example.presentation.screens.ui.onboarding.components.OnboardingControls
import com.example.presentation.screens.ui.onboarding.components.UltraFitnessOnboardingBackground
import com.example.presentation.states.onboarding.OnboardingState
import fitversejourneyapp.presentation.generated.resources.Res
import fitversejourneyapp.presentation.generated.resources.onboarding_description_1
import fitversejourneyapp.presentation.generated.resources.onboarding_description_2
import fitversejourneyapp.presentation.generated.resources.onboarding_description_3
import fitversejourneyapp.presentation.generated.resources.onboarding_description_4
import fitversejourneyapp.presentation.generated.resources.onboarding_title_1
import fitversejourneyapp.presentation.generated.resources.onboarding_title_2
import fitversejourneyapp.presentation.generated.resources.onboarding_title_3
import fitversejourneyapp.presentation.generated.resources.onboarding_title_4
import kotlinx.coroutines.flow.distinctUntilChanged
import org.jetbrains.compose.resources.stringResource

@Composable
fun OnboardingScreen(
    state: OnboardingState,
    viewmodel: OnboardingViewModel,
    onFinish: () -> Unit,
) {
    val onboardingPages = listOf(
        OnboardingPage(
            title = stringResource(Res.string.onboarding_title_1),
            description = stringResource(Res.string.onboarding_description_1),
            animation = OnboardingAnimationTopics.WORKOUT
        ),
        OnboardingPage(
            title = stringResource(Res.string.onboarding_title_2),
            description = stringResource(Res.string.onboarding_description_2),
            animation = OnboardingAnimationTopics.NUTRITION
        ),
        OnboardingPage(
            title = stringResource(Res.string.onboarding_title_3),
            description = stringResource(Res.string.onboarding_description_3),
            animation = OnboardingAnimationTopics.AI
        ),
        OnboardingPage(
            title = stringResource(Res.string.onboarding_title_4),
            description = stringResource(Res.string.onboarding_description_4),
            animation = OnboardingAnimationTopics.COMMUNITY
        )
    )
    val lastIndex = onboardingPages.lastIndex

    val pagerState = rememberPagerState(
        initialPage = state.currentPage,
        pageCount = { onboardingPages.size },
    )

    // anima quando viewmodel.state muda; se exceder lastIndex -> finaliza
    LaunchedEffect(state.currentPage) {
        if (state.currentPage <= lastIndex) {
            pagerState.animateScrollToPage(state.currentPage)
        }
    }

    Scaffold(
        modifier = Modifier,
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                UltraFitnessOnboardingBackground(
                    step = pagerState.currentPage
                )
                OnboardingContent(
                    modifier = Modifier.padding(it),
                    pagerState = pagerState,
                    onFinish = onFinish,
                    nextPage = {
                        viewmodel.nextPage(lastIndex)
                    },
                    skipToLastPage = {
                        viewmodel.skipToLastPage(lastIndex)
                    },
                    onboardingPages = onboardingPages,
                    viewmodel = viewmodel
                )
            }
        }
    )
}
@Composable
fun OnboardingContent(
    modifier: Modifier,
    pagerState: PagerState,
    onFinish: () -> Unit = {},
    nextPage: () -> Unit = {},
    skipToLastPage: () -> Unit = {},
    onboardingPages: List<OnboardingPage>,
    viewmodel: OnboardingViewModel
) {
    val cs = MaterialTheme.colorScheme
    val lastIndex = onboardingPages.lastIndex

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage to pagerState.isScrollInProgress }
            .distinctUntilChanged()
            .collect { (pageIndex, isScrolling) ->
                if (!isScrolling) {
                    viewmodel.setCurrentPage(pageIndex, lastIndex)
                }
            }
    }

    Column(modifier = modifier.fillMaxSize()) {

        HorizontalPager(
            modifier = Modifier.weight(1f),
            state = pagerState
        ) { pageIndex ->

            val pageItem = onboardingPages[pageIndex]

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {

                FitnessLottieAnimation(
                    modifier = Modifier
                        .height(320.dp)
                        .fillMaxWidth(),
                    animation = pageItem.animation
                )

                Spacer(Modifier.height(36.dp))

                Text(
                    text = pageItem.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = cs.onBackground,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(12.dp))

                Text(
                    text = pageItem.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = cs.onBackground.copy(alpha = 0.75f),
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )
            }
        }

        OnboardingControls(
            currentPage = pagerState.currentPage,
            totalPages = onboardingPages.size,
            onNext = nextPage,
            onSkip = skipToLastPage,
            onFinish = onFinish
        )
    }
}
