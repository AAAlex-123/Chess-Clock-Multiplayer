package alexman.chckm.designsystem.component

import alexman.chckm.designsystem.theme.ChckmTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
private fun ChckmButtonPreview() {
    ChckmTheme {
        ChckmButton(
            text = "Submit",
            onClick = { },
        )
    }
}

@Composable
fun ChckmButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
    ) {
        ChckmTextL(
            text = text,
            modifier = Modifier.padding(4.dp),
        )
    }
}
