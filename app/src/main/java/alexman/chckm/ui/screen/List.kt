package alexman.chckm.ui.screen

import alexman.chckm.core.data.model.ClockSet
import alexman.chckm.core.data.model.Displayable
import alexman.chckm.core.designsystem.component.ChckmButton
import alexman.chckm.core.designsystem.component.ChckmCard
import alexman.chckm.core.designsystem.component.ChckmTextM
import alexman.chckm.core.designsystem.component.DeleteIcon
import alexman.chckm.core.designsystem.component.EditIcon
import alexman.chckm.core.designsystem.theme.ChckmTheme
import alexman.chckm.core.data.model.Profile
import alexman.chckm.core.data.model.TimeControl
import alexman.chckm.core.designsystem.component.ChckmScaffold
import alexman.chckm.core.designsystem.component.LocalSizes
import alexman.chckm.core.designsystem.component.Sizes
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
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
private fun ListScreenPreview() {
    ChckmTheme {
        ListScreen(
            data = listOf(),
            listType = ListType.TIME_CONTROL,
            onSelect = { _ -> },
            onSubmit = { _ -> },
            onDelete = { _ -> },
            onNavigateBack = { },
        )
    }
}

enum class ListType {
    PROFILE, TIME_CONTROL, CLOCK_SET,
}

private enum class ListScreenEnum {
    MAIN, EDIT_PROFILE, EDIT_TIME_CONTROL,
}

@Composable
fun <T : Displayable> ListScreen(
    data: List<T>,
    listType: ListType,
    onSelect: (T) -> Unit,
    onSubmit: (T) -> Unit,
    onDelete: (T) -> Unit,
    onNavigateBack: () -> Unit,
) {
    BackHandler { onNavigateBack() }

    var screen by remember { mutableStateOf(ListScreenEnum.MAIN) }
    var editItem: T? by remember { mutableStateOf(null) }
    var straightToCreate: Boolean by remember { mutableStateOf(false) }

    fun switchToEditScreen() {
        screen = when (listType) {
            ListType.PROFILE -> ListScreenEnum.EDIT_PROFILE
            ListType.TIME_CONTROL -> ListScreenEnum.EDIT_TIME_CONTROL
            // can't happen, ListScreenContent is used for ClockSet
            ListType.CLOCK_SET -> ListScreenEnum.MAIN
        }
    }

    fun onEdit(item: T) {
        editItem = item
        switchToEditScreen()
    }

    fun onCreate() {
        val newItem = when (listType) {
            ListType.PROFILE -> Profile.EMPTY as T
            ListType.TIME_CONTROL -> TimeControl.EMPTY as T
            // can't happen, ListScreenContent is used for ClockSet
            ListType.CLOCK_SET -> ClockSet.EMPTY as T
        }

        editItem = newItem
        switchToEditScreen()
    }

    LaunchedEffect(true) {
        // there are no data items to list, go straight to create screen,
        // as if the user pressed the "Create New" button
        if (data.isEmpty()) {
            straightToCreate = true
            onCreate()
        }
    }

    when (screen) {
        ListScreenEnum.MAIN -> ListScreenContent(
            data = data,
            listType = listType,
            onSelect = onSelect,
            onCreate = ::onCreate,
            onEdit = ::onEdit,
            onDelete = onDelete,
            onNavigateBack = onNavigateBack,
        )
        ListScreenEnum.EDIT_PROFILE -> EditProfileScreen(
            profile = (editItem as Profile),
            onSubmitProfile = { updatedProfile ->
                // cast as T is not unchecked because of DataType
                onSubmit(updatedProfile as T)
                onSelect(updatedProfile as T)
            },
            onNavigateBack = {
                if (!straightToCreate) {
                    // cancel Profile edit:
                    // don't submit updatedProfile, just return to MAIN
                    screen = ListScreenEnum.MAIN
                } else {
                    // don't go to MAIN, since we went straight to create
                    // from the previous screen, so return to it directly
                    onNavigateBack()
                }
            },
        )
        ListScreenEnum.EDIT_TIME_CONTROL -> EditTimeControlScreen(
            timeControl = (editItem as TimeControl),
            onSubmitTimeControl = { updatedTimeControl ->
                // cast as T is not unchecked because of DataType
                onSubmit(updatedTimeControl as T)
                onSelect(updatedTimeControl as T)
            },
            onNavigateBack = {
                if (!straightToCreate) {
                    // cancel TimeControl edit:
                    // don't submit updatedTimeControl, just return to MAIN
                    screen = ListScreenEnum.MAIN
                } else {
                    // don't go to MAIN, since we went straight to create
                    // from the previous screen, so return to it directly
                    onNavigateBack()
                }
            },
        )
    }
}

@Composable
fun <T : Displayable> ListScreenContent(
    data: List<T>,
    listType: ListType,
    onSelect: (T) -> Unit,
    onCreate: () -> Unit,
    onEdit: (T) -> Unit,
    onDelete: (T) -> Unit,
    onNavigateBack: (() -> Unit)?,
) {
    ChckmScaffold(
        titleText = when(listType) {
            ListType.PROFILE -> "Pick a Profile"
            ListType.TIME_CONTROL -> "Pick a Time Control"
            ListType.CLOCK_SET -> "Pick a Clock Set"
        },
        onNavigateBack = onNavigateBack,
    ) {
        Column(
            modifier = Modifier.padding(64.dp),
            verticalArrangement = Arrangement.spacedBy(40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                items(data.sorted()) {
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
                CompositionLocalProvider(value = LocalSizes provides Sizes.Secondary) {
                    EditIcon(onClick = onEdit)
                    DeleteIcon(onClick = onDelete)
                }
            }
        }
    }
}
