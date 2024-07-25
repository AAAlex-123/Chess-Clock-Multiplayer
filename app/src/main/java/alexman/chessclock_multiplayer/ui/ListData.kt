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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
private fun ListDataScreenPreview() {
    ChckmTheme {
        ListDataScreen(
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

@Composable
fun <T : Displayable> ListDataScreen(
    data: List<T>, // TODO: use viewModel instead
    dataType: DataType,
    onSelect: (T) -> Unit,
) {
    fun onSubmit(item: T) {
        // TODO: store to viewModel
        when (dataType) {
            DataType.PROFILE -> {
                println("Storing profile: ${(item as Profile)}")
            }
            DataType.TIME_CONTROL -> {
                println("Storing time control: ${(item as TimeControl)}")
            }
            DataType.CLOCK_SET -> {
                println("Storing clock set: ${(item as ClockSet)}")
            }
        }
    }

    fun onDelete(item: T) {
        println("Deleting item: $item")
        // TODO: delete from viewModel
    }

    ListDataScreenContent(
        data = data,
        dataType = dataType,
        onSelect = onSelect,
        onSubmit = ::onSubmit,
        onDelete = ::onDelete,
    )
}

enum class DataType {
    PROFILE, TIME_CONTROL, CLOCK_SET,
}

private enum class ListDataScreen {
    MAIN, EDIT_PROFILE, EDIT_TIME_CONTROL, EDIT_CLOCK_SET,
}

@Composable
private fun <T : Displayable> ListDataScreenContent(
    data: List<T>,
    dataType: DataType,
    onSelect: (T) -> Unit,
    onSubmit: (T) -> Unit,
    onDelete: (T) -> Unit,
) {
    var screen by remember { mutableStateOf(ListDataScreen.MAIN) }
    var editItem: T? by remember { mutableStateOf(null) }

    fun getScreenByDataType(dataType: DataType) =
        when (dataType) {
            DataType.PROFILE -> ListDataScreen.EDIT_PROFILE
            DataType.TIME_CONTROL -> ListDataScreen.EDIT_TIME_CONTROL
            DataType.CLOCK_SET -> ListDataScreen.EDIT_CLOCK_SET
        }

    fun onEdit(item: T) {
        editItem = item
        screen = getScreenByDataType(dataType)
    }

    when (screen) {
        ListDataScreen.MAIN -> ListDataScreenContentMain(
            data = data,
            dataType = dataType,
            onSelect = onSelect,
            onCreate = {
                val newEmptyItem = when (dataType) {
                    // cast as T is not unchecked because of DataType
                    DataType.PROFILE -> Profile.EMPTY as T
                    DataType.TIME_CONTROL -> TimeControl.EMPTY as T

                    // create() can't be called from clock set list screen,
                    // create button does not appear in clock set list screen
                    DataType.CLOCK_SET -> null
                        // screen also doesn't change
                }
                // can't be null, can't create from clock set list screen
                onEdit(newEmptyItem!!)
            },
            onEdit = ::onEdit,
            onDelete = onDelete,
        )
        ListDataScreen.EDIT_PROFILE -> EditProfileScreen(
            profile = (editItem as Profile),
            onSubmitProfile = { updatedProfile ->
                // cast as T is not unchecked because of DataType
                onSubmit(updatedProfile as T)
                screen = ListDataScreen.MAIN
            },
        )
        ListDataScreen.EDIT_TIME_CONTROL -> EditTimeControlScreen(
            timeControl = (editItem as TimeControl),
            onSubmitTimeControl = { updatedTimeControl ->
                // cast as T is not unchecked because of DataType
                onSubmit(updatedTimeControl as T)
                screen = ListDataScreen.MAIN
            }
        )
        ListDataScreen.EDIT_CLOCK_SET -> EditClockSetScreen(
            clockSet = (editItem as ClockSet),
            onSubmitClockSet = { updatedClockSet ->
                // cast as T is not unchecked because of DataType
                onSubmit(updatedClockSet as T)
                screen = ListDataScreen.MAIN
            }
        )
    }
}

@Composable
private fun <T : Displayable> ListDataScreenContentMain(
    data: List<T>,
    dataType: DataType,
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
                ListDataItem(
                    dataItem = it,
                    onSelect = { onSelect(it) },
                    onEdit = { onEdit(it) },
                    onDelete = { onDelete(it) },
                )
            }
        }
        if ((dataType == DataType.PROFILE) || (dataType == DataType.TIME_CONTROL))
            ChckmButton(
                text = "Create New",
                onClick = onCreate,
            )
    }
}

@Composable
private fun <T : Displayable> ListDataItem(
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
