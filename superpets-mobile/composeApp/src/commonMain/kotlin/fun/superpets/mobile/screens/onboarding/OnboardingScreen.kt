package fun.superpets.mobile.screens.onboarding

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fun.superpets.mobile.screens.components.PageIndicator
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

private data class OnboardingPage(
    val title: String,
    val description: String,
    val image: ImageVector,
)

private val onboardingPages = listOf(
    OnboardingPage(
        title = "Welcome to KMP App",
        description = "This is a KMP template that you can use to start your new project.",
        image = Icons.Default.Info
    ),
    OnboardingPage(
        title = "Offline First",
        description = "This app is offline-first. It will work even if you don't have an internet connection.",
        image = Icons.Default.List
    ),
    OnboardingPage(
        title = "Ready to go!",
        description = "You are all set to go. Enjoy the app!",
        image = Icons.Default.Check
    )
)

@Composable
fun OnboardingScreen(
    navigateToHome: () -> Unit,
    viewModel: OnboardingViewModel = koinInject()
) {
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) { page ->
            OnboardingPageContent(onboardingPages[page])
        }

        OnboardingFooter(
            pagerState = pagerState,
            onNextClick = {
                scope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            },
            onFinishClick = {
                viewModel.onOnboardingCompleted()
                navigateToHome()
            }
        )
    }
}

@Composable
private fun OnboardingPageContent(page: OnboardingPage) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            imageVector = page.image,
            contentDescription = null,
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = page.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = page.description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun OnboardingFooter(
    pagerState: PagerState,
    onNextClick: () -> Unit,
    onFinishClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val isLastPage = pagerState.currentPage == onboardingPages.size - 1

            if (isLastPage) {
                Spacer(modifier = Modifier.weight(1f))
            } else {
                TextButton(onClick = onFinishClick) {
                    Text("Skip")
                }
            }

            PageIndicator(
                numberOfPages = onboardingPages.size,
                currentPage = pagerState.currentPage,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
            )

            AnimatedContent(
                targetState = isLastPage,
                label = "finish-button"
            ) {
                if (it) {
                    Button(onClick = onFinishClick) {
                        Text("Let's Get Started!")
                    }
                } else {
                    Button(onClick = onNextClick) {
                        Text("Next")
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                    }
                }
            }
        }
    }
} 