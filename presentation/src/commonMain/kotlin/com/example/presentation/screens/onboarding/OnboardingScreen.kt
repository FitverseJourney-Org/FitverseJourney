package com.example.presentation.screens.onboarding

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
import com.example.presentation.screens.onboarding.components.OnboardingControls
import com.example.presentation.screens.onboarding.components.UltraFitnessOnboardingBackground
import com.example.presentation.states.onboarding.OnboardingState
import fitversejorneyapp.presentation.generated.resources.Res
import fitversejorneyapp.presentation.generated.resources.onboarding_description_1
import fitversejorneyapp.presentation.generated.resources.onboarding_description_2
import fitversejorneyapp.presentation.generated.resources.onboarding_description_3
import fitversejorneyapp.presentation.generated.resources.onboarding_description_4
import fitversejorneyapp.presentation.generated.resources.onboarding_title_1
import fitversejorneyapp.presentation.generated.resources.onboarding_title_2
import fitversejorneyapp.presentation.generated.resources.onboarding_title_3
import fitversejorneyapp.presentation.generated.resources.onboarding_title_4
import org.jetbrains.compose.resources.stringResource

@Composable
fun OnboardingScreen(
    state: OnboardingState,
    onFinish: () -> Unit,
    nextPage: () -> Unit,
    skipToLastPage: () -> Unit,
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

    val pagerState = rememberPagerState(
        initialPage = state.currentPage,
        pageCount = { onboardingPages.size },
    )

    LaunchedEffect(state.currentPage) {
        if(pagerState.currentPage != state.totalPages){
            pagerState.animateScrollToPage(state.currentPage)
        }else{
            onFinish()
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
                    page = onboardingPages[pagerState.currentPage],
                    onFinish = onFinish,
                    nextPage = nextPage,
                    skipToLastPage = skipToLastPage,
                    onboardingPages = onboardingPages
                )
            }
        }
    )
}
@Composable
fun OnboardingContent(
    modifier: Modifier,
    pagerState: PagerState,
    page: OnboardingPage,
    onFinish: () -> Unit = {},
    nextPage: () -> Unit = {},
    skipToLastPage: () -> Unit = {},
    onboardingPages: List<OnboardingPage>
) {
    Column(modifier = modifier.fillMaxSize()){
        HorizontalPager(
            modifier = Modifier.weight(1f),
            state = pagerState,
            userScrollEnabled = true
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Bottom
                ){
                    val tilePerPage1 = stringResource(Res.string.onboarding_title_1)
                    val tilePerPage2 = stringResource(Res.string.onboarding_title_2)
                    val tilePerPage3 = stringResource(Res.string.onboarding_title_3)

                    FitnessLottieAnimation(
                        modifier = Modifier.height(320.dp).fillMaxWidth(),
                        animation = when (page.title) {
                            tilePerPage1 -> OnboardingAnimationTopics.WORKOUT
                            tilePerPage2 -> OnboardingAnimationTopics.NUTRITION
                            tilePerPage3 -> OnboardingAnimationTopics.AI
                            else -> OnboardingAnimationTopics.COMMUNITY
                        }
                    )
                    Spacer(Modifier.height(32.dp))
                    Text(
                        text = page.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = page.description,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.85f),
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )
                }
            }
        }
        OnboardingControls(
            modifier = Modifier,
            currentPage = pagerState.currentPage,
            totalPages = onboardingPages.size,
            onNext = nextPage,
            onSkip = skipToLastPage,
            onFinish = onFinish
        )
    }



}

