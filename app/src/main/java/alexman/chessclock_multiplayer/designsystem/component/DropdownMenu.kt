package alexman.chessclock_multiplayer.designsystem.component

import alexman.chessclock_multiplayer.designsystem.theme.ChckmTheme
import alexman.chessclock_multiplayer.designsystem.theme.ChckmTypography
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Preview
@Composable
private fun ChckmDropdownMenuPreview() {
    ChckmTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(40.dp),
        ) {
            ChckmDropdownMenu(
                title = "Items",
                options = listOf("Item 1", "Item 2", "Item 3"),
                initialSelected = "Item 2",
                type = DropdownType.OTHER,
                onSelectedChanged = { },
            )
            ChckmDropdownMenu(
                title = "Colors",
                options = listOf(Color.Red, Color.Green, Color.Blue),
                initialSelected = Color.Green,
                type = DropdownType.COLOR,
                onSelectedChanged = { },
            )
        }
    }
}

enum class DropdownType {
    OTHER, COLOR,
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> ChckmDropdownMenu(
    title: String,
    options: List<T>,
    initialSelected: T,
    type: DropdownType,
    onSelectedChanged: (T) -> Unit,
    horizontalSpacing: Dp = 16.dp,
    transform: (T) -> String = {
        it.toString().lowercase().replaceFirstChar { c -> c.uppercase() }
    },
) {
    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(initialSelected) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(horizontalSpacing),
        ) {
            ChckmTextM(
                text = "$title:",
            )
            TextField(
                value = when (type) {
                    DropdownType.OTHER -> transform(selected)
                    DropdownType.COLOR -> (selected as Color).getNameOrHex()
                },
                onValueChange = { }, // read-only, change handled by DropdownMenuItem
                modifier = Modifier.fillMaxWidth().menuAnchor(),
                readOnly = true,
                textStyle = ChckmTypography.bodyMedium,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
            )
        }
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { option ->
                ChckmDropdownMenuItem(
                    item = option,
                    type = type,
                    onClick = {
                        selected = option
                        expanded = false
                        onSelectedChanged(option)
                    },
                    transform = transform,
                )
            }
        }
    }
}

@Composable
private fun <T> ChckmDropdownMenuItem(
    item: T,
    type: DropdownType,
    onClick: () -> Unit,
    transform: (T) -> String,
) {
    DropdownMenuItem(
        text = {
            ChckmTextM(
                text = when (type) {
                    DropdownType.OTHER -> transform(item)
                    DropdownType.COLOR -> (item as Color).getNameOrHex()
                },
            )
        },
        onClick = onClick,
        trailingIcon = {
            when (type) {
                DropdownType.OTHER -> { }
                DropdownType.COLOR -> {
                    Canvas(
                        modifier = Modifier, // not an optional parameter
                        contentDescription = "",
                    ) {
                        drawCircle(
                            color = item as Color,
                            radius = 50f,
                        )
                    }
                }
            }
        }
    )
}

// copied from Color companion object constants
private val colorToStringMap = mapOf(
    Color(0xFF000000) to "Black",
    Color(0xFF444444) to "DarkGray",
    Color(0xFF888888) to "Gray",
    Color(0xFFCCCCCC) to "LightGray",
    Color(0xFFFFFFFF) to "White",
    Color(0xFFFF0000) to "Red",
    Color(0xFF00FF00) to "Green",
    Color(0xFF0000FF) to "Blue",
    Color(0xFFFFFF00) to "Yellow",
    Color(0xFF00FFFF) to "Cyan",
    Color(0xFFFF00FF) to "Magenta",
)

private fun Color.getNameOrHex() =
    colorToStringMap.getOrElse(key = this) {
        Integer.toHexString(this.toArgb()).uppercase().let { "#$it" }
    }
