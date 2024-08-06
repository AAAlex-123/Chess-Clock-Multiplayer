package alexman.chckm.ui.screen

import alexman.chckm.core.designsystem.component.ChckmButton
import alexman.chckm.core.designsystem.component.ChckmTextField
import alexman.chckm.core.designsystem.theme.ChckmTheme
import alexman.chckm.core.data.model.TimeControl
import alexman.chckm.core.data.model.TimeControlType
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

@Composable
@Preview
private fun EditTimeControlScreenPreview() {
    ChckmTheme {
        EditTimeControlScreen(
            timeControl = TimeControl.new(
                timeSeconds = 180,
                incrementSeconds = 1,
                type = TimeControlType.FISHER,
            ),
            onSubmitTimeControl = { _ -> },
        )
    }
}

@Composable
fun EditTimeControlScreen(
    timeControl: TimeControl,
    onSubmitTimeControl: (TimeControl) -> Unit,
) {

    val parser = TimeControl.Companion.Parser

    fun onSubmit(time: String, increment: String, type: TimeControlType) {
        // these should never throw; validate should be called before submit
        val timeSeconds: Int = parser.parse(time)
        val incrementSeconds: Int = parser.parse(increment)
        onSubmitTimeControl(
            timeControl.copy(
                timeSeconds = timeSeconds,
                incrementSeconds = incrementSeconds,
                type = type,
            )
        )
    }

    timeControl.let {

        val (initialTime, initialIncrement) =
            if (it == TimeControl.EMPTY) ("" to "")
            else parser.run { format(it.timeSeconds) to format(it.incrementSeconds) }

        EditTimeControlScreenContent(
            initialTime = initialTime,
            initialIncrement = initialIncrement,
            initialType = it.type,
            onSubmit = ::onSubmit,
            validateTimeString = parser::validate,
        )
    }
}

@Composable
private fun EditTimeControlScreenContent(
    initialTime: String,
    initialIncrement: String,
    initialType: TimeControlType,
    onSubmit: (String, String, TimeControlType) -> Unit,
    validateTimeString: (String) -> Boolean,
) {
    var time by remember { mutableStateOf(initialTime) }
    var increment by remember { mutableStateOf(initialIncrement) }
    var type by remember { mutableStateOf(initialType) }

    var timeIsError by remember { mutableStateOf(false) }
    var incrementIsError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(64.dp),
        verticalArrangement = Arrangement.spacedBy(40.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        ChckmTextField(
            title = "Time",
            value = time,
            onValueChanged = {
                time = it
                timeIsError = !validateTimeString(it)
            },
            placeholderText = "15min 30s",
            isError = timeIsError,
        )
        ChckmTextField(
            title = "Increment",
            value = increment,
            onValueChanged = {
                increment = it
                incrementIsError = !validateTimeString(it)
            },
            placeholderText = "1m 15sec",
            isError = incrementIsError,
        )
        ChckmButton(
            text = "OK",
            onClick = {
                if (!timeIsError && !incrementIsError) {
                    onSubmit(time, increment, type)
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
    }
}
