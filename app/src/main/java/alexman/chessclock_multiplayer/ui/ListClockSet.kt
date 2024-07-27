package alexman.chessclock_multiplayer.ui

import alexman.chessclock_multiplayer.designsystem.theme.ChckmTheme
import alexman.chessclock_multiplayer.model.ClockSet
import alexman.chessclock_multiplayer.model.Profile
import alexman.chessclock_multiplayer.model.TimeControl
import alexman.chessclock_multiplayer.repository.FakePersistentRepository
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview

@Preview
@Composable
private fun ListClockSetDataScreenPreview() {
    ChckmTheme {
        ListClockSetScreen(
            viewModel = ChckmViewModel(
                clockSetRepository = FakePersistentRepository(),
                profileRepository = FakePersistentRepository(),
                timeControlRepository = FakePersistentRepository(),
            ),
            onSelect = { _ -> }
        )
    }
}

private enum class ListClockSetScreenEnum {
    MAIN, EDIT_CLOCK_SET,
}

@Composable
fun ListClockSetScreen(
    viewModel: ChckmViewModel,
    onSelect: (ClockSet) -> Unit,
) {
    val profileData = viewModel.profileListDataUiState.collectAsState()
    val timeControlData = viewModel.timeControlListDataUiState.collectAsState()
    val clockSetData = viewModel.clockSetListDataUiState.collectAsState()

    var screen by remember { mutableStateOf(ListClockSetScreenEnum.MAIN) }
    var editItem: ClockSet? by remember { mutableStateOf(null) }

    // 3 callbacks for ListScreenContent (ClockSet)
    fun onEditClockSet(item: ClockSet) {
        editItem = item
        screen = ListClockSetScreenEnum.EDIT_CLOCK_SET
    }
    fun onCreateClockSet() = onEditClockSet(ClockSet.EMPTY)
    fun onDeleteClockSet(item: ClockSet) = viewModel.deleteClockSet(item)

    // 1 callback for EditClockSet
    fun onSubmitClockSet(item: ClockSet) = viewModel.writeClockSet(item)

    // 4 callbacks for EditClockSet -> List (Profile/TimeControl) -> EditProfile/TimeControl
    fun onSubmitProfile(item: Profile) = viewModel.writeProfile(item)
    fun onDeleteProfile(item: Profile) = viewModel.deleteProfile(item)

    fun onSubmitTimeControl(item: TimeControl) = viewModel.writeTimeControl(item)
    fun onDeleteTimeControl(item: TimeControl) = viewModel.deleteTimeControl(item)

    when (screen) {
        // yes, content, not screen, since we handle create/edit actions differently
        // the default of ListScreen is not suitable for this, we have our own Edit Screen
        ListClockSetScreenEnum.MAIN -> ListScreenContent(
            data = clockSetData.value.data,
            onSelect = onSelect,
            onCreate = ::onCreateClockSet,
            onEdit = ::onEditClockSet,
            onDelete = ::onDeleteClockSet,
        )
        ListClockSetScreenEnum.EDIT_CLOCK_SET -> EditClockSetScreen(
            profileData = profileData.value.data,
            timeControlData = timeControlData.value.data,
            clockSet = (editItem as ClockSet),
            onSubmitClockSet = { updatedClockSet ->
                onSubmitClockSet(updatedClockSet)
                screen = ListClockSetScreenEnum.MAIN
            },
            onSubmitProfile = ::onSubmitProfile,
            onDeleteProfile = ::onDeleteProfile,
            onSubmitTimeControl = ::onSubmitTimeControl,
            onDeleteTimeControl = ::onDeleteTimeControl,
        )
    }
}
