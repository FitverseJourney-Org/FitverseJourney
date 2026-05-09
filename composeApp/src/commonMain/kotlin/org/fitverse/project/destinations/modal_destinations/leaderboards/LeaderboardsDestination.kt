
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.presentation.widgets.LeaderboardScreen

@Composable
fun LeaderboardsDestination(
    navigateBack: () -> Unit
) {
    val cs = MaterialTheme.colorScheme

    LeaderboardScreen(
        onBack = navigateBack
    )
}
