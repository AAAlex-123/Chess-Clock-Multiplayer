package alexman.chessclock_multiplayer.designsystem.component

import alexman.chessclock_multiplayer.designsystem.theme.ChckmTheme
import alexman.chessclock_multiplayer.designsystem.theme.ChckmTypography
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
private fun ChckmTextPreview() {
    ChckmTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(40.dp),
        ) {
            ChckmTextL(text = "Large text")
            ChckmTextM(text = "Medium text")
        }
    }
}

@Composable
fun ChckmTextDM(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = ChckmTypography.displayMedium,
        modifier = modifier,
    )
}

@Composable
fun ChckmTextL(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = ChckmTypography.bodyLarge,
        modifier = modifier,
    )
}

@Composable
fun ChckmTextM(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        style = ChckmTypography.bodyMedium,
        modifier = modifier,
    )
}
