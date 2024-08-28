package alexman.chckm.ui.screen

import alexman.chckm.core.designsystem.component.ChckmButton
import alexman.chckm.core.designsystem.component.ChckmTextField
import alexman.chckm.core.designsystem.theme.ChckmTheme
import alexman.chckm.core.data.model.TimeControl
import alexman.chckm.core.designsystem.component.ChckmScaffold
import androidx.activity.compose.BackHandler
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
            ),
            onSubmitTimeControl = { _ -> },
            onNavigateBack = { },
        )
    }
}

@Composable
fun EditTimeControlScreen(
    timeControl: TimeControl,
    onSubmitTimeControl: (TimeControl) -> Unit,
    onNavigateBack: () -> Unit,
) {
    BackHandler { onNavigateBack() }

    val parser = TimeControl.Companion.Parser

    fun onSubmit(time: String, increment: String) {
        // these should never throw; validate should be called before submit
        val timeSeconds: Int = parser.parse(time)
        val incrementSeconds: Int = parser.parse(increment)
        onSubmitTimeControl(
            timeControl.copy(
                timeSeconds = timeSeconds,
                incrementSeconds = incrementSeconds,
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
            isCreate = it == TimeControl.EMPTY,
            onSubmit = ::onSubmit,
            onNavigateBack = onNavigateBack,
            validateTimeString = parser::validate,
        )
    }
}

@Composable
private fun EditTimeControlScreenContent(
    initialTime: String,
    initialIncrement: String,
    isCreate: Boolean,
    onSubmit: (String, String) -> Unit,
    onNavigateBack: () -> Unit,
    validateTimeString: (String) -> Boolean,
) {
    var time by remember { mutableStateOf(initialTime) }
    var increment by remember { mutableStateOf(initialIncrement) }

    var timeIsError by remember { mutableStateOf(!validateTimeString(initialTime)) }
    var incrementIsError by remember { mutableStateOf(!validateTimeString(initialIncrement)) }

    ChckmScaffold(
        titleText = when (isCreate) {
            true -> "Create Time Control"
            false -> "Edit Time Control"
        },
        onNavigateBack = onNavigateBack,
    ) {
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
                        onSubmit(time, increment)
                    }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
        }
    }
}
