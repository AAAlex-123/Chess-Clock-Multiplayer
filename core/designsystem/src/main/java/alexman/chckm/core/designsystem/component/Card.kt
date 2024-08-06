package alexman.chckm.core.designsystem.component

import alexman.chckm.core.designsystem.theme.ChckmTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable (BoxScope.() -> Unit),
) {
    Card(
        onClick = onClick,
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = contentAlignment,
            content = content,
        )
    }
}
