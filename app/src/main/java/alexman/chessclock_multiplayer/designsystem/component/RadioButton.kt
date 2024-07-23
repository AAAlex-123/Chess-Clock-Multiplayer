package alexman.chessclock_multiplayer.designsystem.component

import alexman.chessclock_multiplayer.designsystem.theme.ChckmTheme
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Preview
@Composable
private fun ChckmRadioButtonGroupPreview() {
    ChckmTheme {
        ChckmRadioButtonGroup(
            items = listOf("Item 1", "Item 2", "Item 3"),
            selected = "Item 2",
            onSelectedChanged = { },
        )
    }
}

@Composable
fun <T> ChckmRadioButtonGroup(
    items: List<T>,
    selected: T,
    onSelectedChanged: (T) -> Unit,
    modifier: Modifier = Modifier,
    verticalButtonArrangement: Arrangement.Vertical = Arrangement.spacedBy(16.dp),
    horizontalSpacing: Dp = 12.dp,
    transform: (T) -> String = {
        it.toString().lowercase().replaceFirstChar { c -> c.uppercase() }
    },
) {
    Column(
        modifier = modifier,
        verticalArrangement = verticalButtonArrangement,
    ) {
        items.forEach { item ->
            ChckmRadioButton(
                item = item,
                selected = item == selected,
                onClick = onSelectedChanged,
                transform = transform,
                horizontalSpacing = horizontalSpacing,
            )
        }
    }
}

@Composable
private fun <T> ChckmRadioButton(
    item: T,
    selected: Boolean,
    onClick: (T) -> Unit,
    transform: (T) -> String,
    horizontalSpacing: Dp,
) {
    Row (
        modifier = Modifier
            .clickable { onClick(item) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(horizontalSpacing),
    ) {
        RadioButton(
            selected = selected,
            onClick = null, // handled by Row so that text is also clickable
        )
        ChckmTextM(
            text = transform(item),
        )
    }
}
