package alexman.chckm.core.designsystem.component

import alexman.chckm.core.designsystem.theme.ChckmTheme
import alexman.chckm.core.designsystem.theme.ChckmTypography
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
private fun ChckmTextPreview() {
    ChckmTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(40.dp),
        ) {
            ChckmTextDM(text = "Display Medium")
            ChckmTextTM(text = "Title Medium")
            ChckmTextL(text = "Body Large")
            ChckmTextM(text = "Body Medium")
            ChckmTextLM(text = "Label Medium")
        }
    }
}

@Composable
fun ChckmTextDM(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center,
) {
    Text(
        text = text,
        style = ChckmTypography.displayMedium,
        modifier = modifier,
        textAlign = textAlign,
    )
}

@Composable
fun ChckmTextTM(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center,
) {
    Text(
        text = text,
        style = ChckmTypography.titleMedium,
        modifier = modifier,
        textAlign = textAlign,
    )
}

@Composable
fun ChckmTextL(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center,
) {
    Text(
        text = text,
        style = ChckmTypography.bodyLarge,
        modifier = modifier,
        textAlign = textAlign,
    )
}

@Composable
fun ChckmTextM(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center,
) {
    Text(
        text = text,
        style = ChckmTypography.bodyMedium,
        modifier = modifier,
        textAlign = textAlign,
    )
}

@Composable
fun ChckmTextLM(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center,
) {
    Text(
        text = text,
        style = ChckmTypography.labelMedium,
        modifier = modifier,
        textAlign = textAlign,
    )
}
