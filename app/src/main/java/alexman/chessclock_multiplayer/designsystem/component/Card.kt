package alexman.chessclock_multiplayer.designsystem.component

import alexman.chessclock_multiplayer.designsystem.theme.ChckmTheme
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
private fun ChckmCardPreview() {
    ChckmTheme {
        ChckmCard(
            onClick = { },
        ) {
            ChckmTextM(
                text = "Card Contents",
                modifier = Modifier.padding(8.dp),
            )
        }
    }
}

@Composable
fun ChckmCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (ColumnScope.() -> Unit),
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        content = content,
    )
}
