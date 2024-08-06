package alexman.chckm.ui.screen

import alexman.chckm.core.data.model.Clock
import alexman.chckm.core.data.model.ClockSet
import alexman.chckm.core.data.model.Profile
import alexman.chckm.core.data.model.TimeControl
import alexman.chckm.core.data.model.TimeControlType
import alexman.chckm.core.designsystem.component.ChckmButton
import alexman.chckm.core.designsystem.component.ChckmCard
import alexman.chckm.core.designsystem.component.ChckmTextField
import alexman.chckm.core.designsystem.component.ChckmTextM
import alexman.chckm.core.designsystem.component.DeleteIcon
import alexman.chckm.core.designsystem.theme.ChckmTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
        timeSeconds = 180, incrementSeconds = 1, type = TimeControlType.FISHER,
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
            onSubmitClockSet = { _ -> },
            onSubmitProfile = { _ -> },
            onDeleteProfile = { _ -> },
            onSubmitTimeControl = { _ -> },
            onDeleteTimeControl = { _ -> },
        )
    }
}

private data class ClockData(
    var profileId: Int = -1,
    var profileDisplayString: String? = null,
    var timeControlId: Int = -1,
    var timeControlDisplayString: String? = null,
) {
    // computed property since it depends on other var properties
    val isInitialized: Boolean
        get() = (profileId != -1) and (timeControlId != -1)
}

@Composable
fun EditClockSetScreen(
    profileData: List<Profile>,
    timeControlData: List<TimeControl>,
    clockSet: ClockSet,
    onSubmitClockSet: (ClockSet) -> Unit,
    onSubmitProfile: (Profile) -> Unit,
    onDeleteProfile: (Profile) -> Unit,
    onSubmitTimeControl: (TimeControl) -> Unit,
    onDeleteTimeControl: (TimeControl) -> Unit,
) {

    fun onSubmit(name: String, clockDataList: List<ClockData>) {
        val clocks = clockDataList.map { clockData ->
            Clock.new(
                profileData.first { it.id == clockData.profileId },
                timeControlData.first { it.id == clockData.timeControlId },
            )
        }

        onSubmitClockSet(
            clockSet.copy(name = name, clocks = clocks)
        )
    }

    // regex allows: letters, numbers, underscore, dash, space
    // cannot start with dash or space, must be at least 1 character
    val nameRegex = Regex(pattern = "^\\w[\\w\\- ]*$")

    fun validateName(name: String): Boolean =
        nameRegex.matches(name)

    EditClockSetScreenContent(
        profileData = profileData,
        timeControlData = timeControlData,
        initialName = clockSet.name,
        initialClockDataList = clockSet.clocks.map {
            ClockData(
                it.profile.id,
                it.profile.displayString,
                it.timeControl.id,
                it.timeControl.displayString
            )
        },
        onSubmit = ::onSubmit,
        validateName = ::validateName,
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
            onSelect = { item ->
                with (clockDataList[editItemIndex]) {
                    profileId = item.id
                    profileDisplayString = item.displayString
                }
                screen = EditClockSetScreenEnum.MAIN
            },
            onSubmit = onSubmitProfile,
            onDelete = onDeleteProfile,
        )
        EditClockSetScreenEnum.SELECT_TIME_CONTROL -> ListScreen(
            data = timeControlData,
            listType = ListType.TIME_CONTROL,
            onSelect = { item ->
                with (clockDataList[editItemIndex]) {
                    timeControlId = item.id
                    timeControlDisplayString = item.displayString
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
    onNameChanged: (String) -> Unit,
    minClockCount: Int,
    clockDataList: List<ClockData>,
    onAddClock: () -> Unit,
    onRemoveClock: (Int) -> Unit,
    onCreate: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(32.dp),
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
        Row (
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
        ChckmTextM(
            text = "${index + 1}:",
        )
        ChckmCard(
            onClick = onEditProfile,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            ChckmTextM(
                text = clockData.profileDisplayString ?: emptyFieldText,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
        }
        ChckmCard(
            onClick = onEditTimeControl,
            modifier = Modifier
                .weight(2f)
                .fillMaxHeight()
        ) {
            ChckmTextM(
                text = clockData.timeControlDisplayString ?: emptyFieldText,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            )
        }
        // don't allow deletion of first `minClocks` clocks
        if (index >= minClockCount)
            ChckmCard(
                onClick = onDelete,
                modifier = Modifier.fillMaxHeight(),
            ) {
                // icon card has smaller height than other cards with text,
                // add a box to center icon in card
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(ratio = 1f),
                    contentAlignment = Alignment.Center,
                ) {
                    DeleteIcon(onClick = onDelete)
                }
            }
    }
}
