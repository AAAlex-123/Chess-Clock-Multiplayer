package alexman.chckm.core.designsystem.component

import alexman.chckm.core.designsystem.theme.ChckmTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
private fun ChckmSwitchPreview() {
    ChckmTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(40.dp),
        ) {
            ChckmSwitch(
                text = "Switch text",
                checked = false,
                onCheckedChanged = { },
            )
            ChckmSwitch(
                text = "Switch text",
                checked = true,
                onCheckedChanged = { },
            )
        }
    }
}

@Composable
fun ChckmSwitch(
    text: String,
    checked: Boolean,
    onCheckedChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.then(
            // make both switch and text clickable
            Modifier.clickable { onCheckedChanged(!checked) }
        ),
        horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Switch(
            checked = checked,
            onCheckedChange = { onCheckedChanged(it) }, // also allow the switch to be clicked
        )
        ChckmTextM(text = text)
    }
}
