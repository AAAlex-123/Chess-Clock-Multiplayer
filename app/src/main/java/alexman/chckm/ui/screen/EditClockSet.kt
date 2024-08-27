package alexman.chckm.ui.screen

import alexman.chckm.core.data.model.Clock
import alexman.chckm.core.data.model.ClockSet
import alexman.chckm.core.data.model.Profile
import alexman.chckm.core.data.model.TimeControl
import alexman.chckm.core.designsystem.component.ChckmButton
import alexman.chckm.core.designsystem.component.ChckmCard
import alexman.chckm.core.designsystem.component.ChckmScaffold
import alexman.chckm.core.designsystem.component.ChckmTextField
import alexman.chckm.core.designsystem.component.ChckmTextM
import alexman.chckm.core.designsystem.component.DeleteIcon
import alexman.chckm.core.designsystem.theme.ChckmTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
private fun EditClockSetScreenPreview() {

    val timeControl = TimeControl.new(
        timeSeconds = 180, incrementSeconds = 1,
    )
    val profile = Profile.new(
        name = "Alice", color = Color.Red,
    )

    val clockSet = ClockSet.new(
        name = "Scrabble",
        clocks = listOf(
            Clock.new(profile = profile, timeControl = timeControl),
        ),
    )

    ChckmTheme {
        EditClockSetScreen(
            profileData = listOf(),
            timeControlData = listOf(),
            clockSet = clockSet,
            onNavigateBack = { },
            onSubmitClockSet = { _ -> },
            onSubmitProfile = { _ -> },
            onDeleteProfile = { _ -> },
            onSubmitTimeControl = { _ -> },
            onDeleteTimeControl = { _ -> },
        )
    }
}

private data class ClockData(
    var profile: Profile = Profile.EMPTY,
    var timeControl: TimeControl = TimeControl.EMPTY,
) {
    // computed property since it depends on other var properties
    val isInitialized: Boolean
        get() = (profile != Profile.EMPTY) and (timeControl != TimeControl.EMPTY)
}

@Composable
fun EditClockSetScreen(
    profileData: List<Profile>,
    timeControlData: List<TimeControl>,
    clockSet: ClockSet,
    onNavigateBack: () -> Unit,
    onSubmitClockSet: (ClockSet) -> Unit,
    onSubmitProfile: (Profile) -> Unit,
    onDeleteProfile: (Profile) -> Unit,
    onSubmitTimeControl: (TimeControl) -> Unit,
    onDeleteTimeControl: (TimeControl) -> Unit,
) {

    fun onSubmit(name: String, clockDataList: List<ClockData>) {
        val clocks = clockDataList.map {
            Clock.new(it.profile, it.timeControl)
        }

        onSubmitClockSet(
            clockSet.copy(name = name, clocks = clocks)
        )
    }

    val validateName = ClockSet.Companion::validateName

    EditClockSetScreenContent(
        profileData = profileData,
        timeControlData = timeControlData,
        initialName = clockSet.name,
        initialClockDataList = clockSet.clocks.map {
            ClockData(
                it.profile,
                it.timeControl,
            )
        },
        onNavigateBack = onNavigateBack,
        onSubmit = ::onSubmit,
        validateName = validateName,
        onSubmitProfile = onSubmitProfile,
        onDeleteProfile = onDeleteProfile,
        onSubmitTimeControl = onSubmitTimeControl,
        onDeleteTimeControl = onDeleteTimeControl,
    )
}

private enum class EditClockSetScreenEnum {
    MAIN, SELECT_PROFILE, SELECT_TIME_CONTROL,
}

@Composable
private fun EditClockSetScreenContent(
    profileData: List<Profile>,
    timeControlData: List<TimeControl>,
    initialName: String,
    initialClockDataList: List<ClockData>,
    onNavigateBack: () -> Unit,
    onSubmit: (String, List<ClockData>) -> Unit,
    validateName: (String) -> Boolean,
    onSubmitProfile: (Profile) -> Unit,
    onDeleteProfile: (Profile) -> Unit,
    onSubmitTimeControl: (TimeControl) -> Unit,
    onDeleteTimeControl: (TimeControl) -> Unit,
) {
    var screen by remember { mutableStateOf(EditClockSetScreenEnum.MAIN) }
    // keeps track of the item between screens
    var editItemIndex by remember { mutableIntStateOf(-1) }

    var name by remember { mutableStateOf(initialName) }
    var nameIsError by remember { mutableStateOf(!validateName(initialName)) }

    // start with at least two clocks
    val minClockCount = 2
    val additionalEmptyClockCount = minClockCount - initialClockDataList.size
    val initialEmptyClocks = (0..< additionalEmptyClockCount).map { ClockData() }

    // call toTypedArray() to use spread operator on resulting array
    val clockDataList = remember { mutableStateListOf(
        *initialClockDataList.toTypedArray(),
        *initialEmptyClocks.toTypedArray()
    ) }

    fun validateClockDataList(clockDataList: List<ClockData>) =
        with (clockDataList) {
            (isNotEmpty()) and (all { it.isInitialized })
        }

    when (screen) {
        EditClockSetScreenEnum.MAIN -> EditClockSetScreenContentMain(
            onEditProfile = { index ->
                editItemIndex = index
                screen = EditClockSetScreenEnum.SELECT_PROFILE
            },
            onEditTimeControl = { index ->
                editItemIndex = index
                screen = EditClockSetScreenEnum.SELECT_TIME_CONTROL
            },
            name = name,
            nameIsError = nameIsError,
            onNavigateBack = onNavigateBack,
            onNameChanged = {
                name = it
                nameIsError = !validateName(it)
            },
            minClockCount = minClockCount,
            clockDataList = clockDataList,
            onAddClock = { clockDataList.add(ClockData()) },
            onRemoveClock = clockDataList::removeAt,
            onCreate = {
                clockDataList.let {
                    if (!nameIsError && validateClockDataList(it))
                        onSubmit(name, it)
                }
            },
        )
        EditClockSetScreenEnum.SELECT_PROFILE -> ListScreen(
            data = profileData,
            listType = ListType.PROFILE,
            onNavigateBack = { }, // TODO: this
            onSelect = { item ->
                with (clockDataList[editItemIndex]) {
                    profile = item
                }
                screen = EditClockSetScreenEnum.MAIN
            },
            onSubmit = onSubmitProfile,
            onDelete = onDeleteProfile,
        )
        EditClockSetScreenEnum.SELECT_TIME_CONTROL -> ListScreen(
            data = timeControlData,
            listType = ListType.TIME_CONTROL,
            onNavigateBack = { }, // TODO: this
            onSelect = { item ->
                with (clockDataList[editItemIndex]) {
                    timeControl = item
                }
                screen = EditClockSetScreenEnum.MAIN
            },
            onSubmit = onSubmitTimeControl,
            onDelete = onDeleteTimeControl,
        )
    }
}

@Composable
private fun EditClockSetScreenContentMain(
    onEditProfile: (Int) -> Unit,
    onEditTimeControl: (Int) -> Unit,
    name: String,
    nameIsError: Boolean,
    onNavigateBack: () -> Unit,
    onNameChanged: (String) -> Unit,
    minClockCount: Int,
    clockDataList: List<ClockData>,
    onAddClock: () -> Unit,
    onRemoveClock: (Int) -> Unit,
    onCreate: () -> Unit,
) {
    ChckmScaffold(
        titleText = "Edit Clock Set",
        onNavigateBack = onNavigateBack,
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(40.dp),
        ) {
            ChckmTextField(
                title = "Name",
                value = name,
                onValueChanged = onNameChanged,
                placeholderText = "Your Game",
                isError = nameIsError,
            )
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            ) {
                itemsIndexed(clockDataList) { i, clockData ->
                    EditClockRow(
                        onEditProfile = { onEditProfile(i) },
                        onEditTimeControl = { onEditTimeControl(i) },
                        index = i,
                        minClockCount = minClockCount,
                        clockData = clockData,
                        onDelete = { onRemoveClock(i) }
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                ChckmButton(
                    text = "+",
                    onClick = onAddClock,
                )
                ChckmButton(
                    text = "OK",
                    onClick = onCreate,
                )
            }
        }
    }
}

@Composable
private fun EditClockRow(
    onEditProfile: () -> Unit,
    onEditTimeControl: () -> Unit,
    index: Int,
    minClockCount: Int,
    clockData: ClockData,
    onDelete: () -> Unit,
) {

    val emptyFieldText = "---"

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            // https://stackoverflow.com/questions/67677125/fill-height-for-child-in-row#67678041
            .height(IntrinsicSize.Min)
    ) {
        ChckmCard(
            onClick = onEditProfile,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            backgroundColor = clockData.profile.color.copy(alpha = 0.5f),
        ) {
            ChckmTextM(
                text = clockData.profile.displayString
                    .takeIf { it.isNotEmpty() } ?: emptyFieldText,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
        }
        ChckmCard(
            onClick = onEditTimeControl,
            modifier = Modifier
                .weight(1.5f)
                .fillMaxHeight(),
            backgroundColor = clockData.profile.color.copy(alpha = 0.5f),
        ) {
            ChckmTextM(
                text = clockData.timeControl.displayString
                    .takeIf { it != "0 sec" } ?: emptyFieldText,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
        }
        // don't allow deletion of first `minClocks` clocks
        if (index >= minClockCount)
            ChckmCard(
                onClick = onDelete,
                modifier = Modifier.aspectRatio(ratio = 1f),
            ) {
                DeleteIcon(onClick = onDelete)
            }
    }
}
