package alexman.chckm.core.designsystem.component

import alexman.chckm.core.designsystem.theme.ChckmTheme
import alexman.chckm.core.designsystem.theme.ChckmTypography
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Preview
@Composable
private fun ChckmTextFieldPreview() {
    ChckmTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(40.dp),
        ) {
            ChckmTextField(
                title = "Time",
                value = "3 min",
                onValueChanged = { },
                placeholderText = "Placeholder",
                isError = false,
            )
            ChckmTextField(
                title = "Time",
                value = "",
                onValueChanged = { },
                placeholderText = "3 min",
                isError = true,
            )
        }
    }
}

@Composable
fun ChckmTextField(
    title: String,
    value: String,
    onValueChanged: (String) -> Unit,
    placeholderText: String,
    isError: Boolean,
    modifier: Modifier = Modifier,
    horizontalSpacing: Dp = 16.dp,
) {
    Row (
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(horizontalSpacing),
    ) {
        ChckmTextM(
            text = "$title:",
        )
        TextField(
            value = value,
            onValueChange = onValueChanged,
            modifier = Modifier.fillMaxWidth(),
            textStyle = ChckmTypography.bodyMedium,
            placeholder = { ChckmTextM(
                text = placeholderText,
            ) },
            keyboardOptions = KeyboardOptions(autoCorrect = false),
            isError = isError,
            singleLine = true,
        )
    }
}
