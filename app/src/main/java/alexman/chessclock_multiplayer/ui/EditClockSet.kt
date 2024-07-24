package alexman.chessclock_multiplayer.ui

import alexman.chessclock_multiplayer.designsystem.component.ChckmButton
import alexman.chessclock_multiplayer.designsystem.component.ChckmTextField
import alexman.chessclock_multiplayer.designsystem.theme.ChckmTheme
import alexman.chessclock_multiplayer.model.ClockSet
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
private fun EditClockSetScreenPreview() {
    ChckmTheme {
        EditClockSetScreen(
            clockSet = ClockSet.new(
                name = "Scrabble",
                clocks = listOf(),
            ),
            onSubmitClockSet = { _ -> },
        )
    }
}

@Composable
fun EditClockSetScreen(
    clockSet: ClockSet,
    onSubmitClockSet: (ClockSet) -> Unit,
) {

    fun onSubmit(name: String) {
        onSubmitClockSet(
            clockSet.copy(
                name = name,
            )
        )
    }

    // regex allows: letters, numbers, dash, space
    val nameRegex = Regex(pattern = "^[\\w\\- ]+$")

    fun validateName(name: String): Boolean =
        nameRegex.matches(name)

    EditClockSetScreenContent(
        initialName = clockSet.name,
        onSubmit = ::onSubmit,
        validateName = ::validateName,
    )
}

@Composable
private fun EditClockSetScreenContent(
    initialName: String,
    onSubmit: (String) -> Unit,
    validateName: (String) -> Boolean,
) {
    var name by remember { mutableStateOf(initialName) }

    var nameIsError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(64.dp),
        verticalArrangement = Arrangement.spacedBy(40.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        ChckmTextField(
            title = "Name",
            value = name,
            onValueChanged = {
                name = it
                nameIsError = !validateName(it)
            },
            placeholderText = "Scrabble",
            isError = nameIsError,
        )
        ChckmButton(
            text = "OK",
            onClick = {
                if (!nameIsError) {
                    onSubmit(name)
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
    }
}
