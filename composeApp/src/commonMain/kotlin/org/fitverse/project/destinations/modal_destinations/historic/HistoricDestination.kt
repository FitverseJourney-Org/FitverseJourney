
import androidx.compose.runtime.Composable
import com.example.presentation.ui.historic.HistoricScreen

@Composable
fun HistoricDestination(
    navigateBack: () -> Unit
) {
    HistoricScreen(
        onBack = navigateBack
    )
}