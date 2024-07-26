package alexman.chessclock_multiplayer.ui

import alexman.chessclock_multiplayer.designsystem.component.ChckmButton
import alexman.chessclock_multiplayer.designsystem.component.ChckmCard
import alexman.chessclock_multiplayer.designsystem.component.ChckmTextM
import alexman.chessclock_multiplayer.designsystem.component.DeleteIcon
import alexman.chessclock_multiplayer.designsystem.component.EditIcon
import alexman.chessclock_multiplayer.designsystem.theme.ChckmTheme
import alexman.chessclock_multiplayer.model.ClockSet
import alexman.chessclock_multiplayer.model.Profile
import alexman.chessclock_multiplayer.model.TimeControl
import alexman.chessclock_multiplayer.model.TimeControlType
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
private fun ListScreenPreview() {
    ChckmTheme {
        ListScreen(
            data = listOf(
                TimeControl.new(180, 1, TimeControlType.FISHER),
                TimeControl.new(60, 0, TimeControlType.BRONSTEIN),
                TimeControl.new(900, 30, TimeControlType.DELAY),
            ),
            dataType = DataType.TIME_CONTROL,
            onSelect = { _ -> }
        )
    }
}

enum class ListType {
    PROFILE, TIME_CONTROL, CLOCK_SET,
}

private enum class ListScreenEnum {
    MAIN, EDIT_PROFILE, EDIT_TIME_CONTROL, EDIT_CLOCK_SET,
}

@Composable
fun <T : Displayable> ListScreen(
    data: List<T>,
    listType: ListType,
    onSelect: (T) -> Unit,
    onSubmit: (T) -> Unit,
    onDelete: (T) -> Unit,
) {
    var screen by remember { mutableStateOf(ListScreenEnum.MAIN) }
    var editItem: T? by remember { mutableStateOf(null) }

    fun onEdit(item: T) {
        editItem = item
        screen = when (listType) {
            ListType.PROFILE -> ListScreenEnum.EDIT_PROFILE
            ListType.TIME_CONTROL -> ListScreenEnum.EDIT_TIME_CONTROL
            ListType.CLOCK_SET -> ListScreenEnum.EDIT_CLOCK_SET
        }
    }

    fun onCreate() {
        val newItem = when (listType) {
            ListType.PROFILE -> Profile.EMPTY as T
            ListType.TIME_CONTROL -> TimeControl.EMPTY as T
            ListType.CLOCK_SET -> ClockSet.EMPTY as T
        }

        onEdit(newItem)
    }

    when (screen) {
        ListScreenEnum.MAIN -> ListScreenContent(
            data = data,
            onSelect = onSelect,
            onCreate = ::onCreate,
            onEdit = ::onEdit,
            onDelete = onDelete,
        )
        ListScreenEnum.EDIT_PROFILE -> EditProfileScreen(
            profile = (editItem as Profile),
            onSubmitProfile = { updatedProfile ->
                // cast as T is not unchecked because of DataType
                onSubmit(updatedProfile as T)
                screen = ListScreenEnum.MAIN
            },
        )
        ListScreenEnum.EDIT_TIME_CONTROL -> EditTimeControlScreen(
            timeControl = (editItem as TimeControl),
            onSubmitTimeControl = { updatedTimeControl ->
                // cast as T is not unchecked because of DataType
                onSubmit(updatedTimeControl as T)
                screen = ListScreenEnum.MAIN
            }
        )
        ListDataScreen.EDIT_CLOCK_SET -> EditClockSetScreen(
            // make sure these match test data of TestScreen
            // TODO: replace these with EditClockSet viewModel data
            profileData = listOf(
                Profile.load(0, "Alice", Color.Red),
                Profile.load(1, "Bob", Color.Green)
            ),
            // make sure these match test data of TestScreen
            // TODO: replace these with EditClockSet viewModel data
            timeControlData = listOf(
                TimeControl.load(0, 180, 1, TimeControlType.FISHER),
                TimeControl.load(1, 60, 0, TimeControlType.BRONSTEIN),
            ),
            clockSet = (editItem as ClockSet),
            onSubmitClockSet = { updatedClockSet ->
                // cast as T is not unchecked because of DataType
                onSubmit(updatedClockSet as T)
                screen = ListScreenEnum.MAIN
            }
        )
    }
}

@Composable
private fun <T : Displayable> ListScreenContent(
    data: List<T>,
    onSelect: (T) -> Unit,
    onCreate: () -> Unit,
    onEdit: (T) -> Unit,
    onDelete: (T) -> Unit,
) {
    Column(
        modifier = Modifier.padding(64.dp),
        verticalArrangement = Arrangement.spacedBy(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            items(data) {
                ListItem(
                    dataItem = it,
                    onSelect = { onSelect(it) },
                    onEdit = { onEdit(it) },
                    onDelete = { onDelete(it) },
                )
            }
        }
        ChckmButton(
            text = "Create New",
            onClick = onCreate,
        )
    }
}

@Composable
private fun <T : Displayable> ListItem(
    dataItem: T,
    onSelect: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    ChckmCard(
        onClick = onSelect,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
        ) {
            ChckmTextM(
                text = dataItem.displayString,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                EditIcon(onClick = onEdit)
                DeleteIcon(onClick = onDelete)
            }
        }
    }
}
