package alexman.chessclock_multiplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import alexman.chessclock_multiplayer.designsystem.theme.ChckmTheme
import alexman.chessclock_multiplayer.model.Clock
import alexman.chessclock_multiplayer.model.ClockSet
import alexman.chessclock_multiplayer.model.Profile
import alexman.chessclock_multiplayer.model.TimeControl
import alexman.chessclock_multiplayer.model.TimeControlType
import alexman.chessclock_multiplayer.repository.FileRepository
import alexman.chessclock_multiplayer.repository.Serializer
import alexman.chessclock_multiplayer.repository.StringClockSetSerializer
import alexman.chessclock_multiplayer.repository.StringProfileSerializer
import alexman.chessclock_multiplayer.repository.StringTimeControlSerializer
import alexman.chessclock_multiplayer.ui.ChckmViewModel
import alexman.chessclock_multiplayer.ui.ListType
import alexman.chessclock_multiplayer.ui.EditClockSetScreen
import alexman.chessclock_multiplayer.ui.EditProfileScreen
import alexman.chessclock_multiplayer.ui.EditTimeControlScreen
import alexman.chessclock_multiplayer.ui.ListClockSetScreen
import alexman.chessclock_multiplayer.ui.ListScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChckmTheme {
                TestScreen()
            }
        }
    }
}

private enum class Screen {
    EDIT_TIMECONTROL, EDIT_PROFILE, EDIT_CLOCKSET,
    LIST, LIST_CLOCKEST, NONE,
}

@Composable
private fun TestScreen() {

    val screen = Screen.LIST_CLOCKEST

    val timeControlFileName = "timecontrol.txt"
    val profileFileName = "profile.txt"
    val clockSetFileName = "clockset.txt"

    val timeControlRepository = FileRepository(
        context = LocalContext.current,
        fileName = timeControlFileName,
        serializer = Serializer.StringTimeControlSerializer,
    )

    val profileRepository = FileRepository(
        context = LocalContext.current,
        fileName = profileFileName,
        serializer = Serializer.StringProfileSerializer,
    )

    val clockSetRepository = FileRepository(
        context = LocalContext.current,
        fileName = clockSetFileName,
        serializer = Serializer.StringClockSetSerializer,
    )

    val viewModel = ChckmViewModel(
        profileRepository = profileRepository,
        timeControlRepository = timeControlRepository,
        clockSetRepository = clockSetRepository,
    )

    val timeControlTestData = listOf(
        TimeControl.load(0, 180, 1, TimeControlType.FISHER),
        TimeControl.load(1, 60, 1, TimeControlType.BRONSTEIN),
        TimeControl.load(2, 300, 5, TimeControlType.DELAY),
    )

    val profileTestData = listOf(
        Profile.load(0, "Alice", Color.Red),
        Profile.load(1, "Bob", Color.Green),
        Profile.load(2, "Charlie", Color.Blue),
    )

    val clockSetTestData = listOf(
        ClockSet.load(0, "Scrabble", listOf(
            Clock.load(profileTestData[0], timeControlTestData[0], 1000, 5000),
            Clock.load(profileTestData[1], timeControlTestData[1], 2000, 4000),
        ), 0),
    )

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            when (screen) {
                Screen.NONE -> {}

                Screen.EDIT_TIMECONTROL -> EditTimeControlScreen(
                    timeControl = timeControlTestData[0],
                    onSubmitTimeControl = { println("TimeControl data: $it") }
                )

                Screen.EDIT_PROFILE -> EditProfileScreen(
                    profile = profileTestData[0],
                    onSubmitProfile = { println("Profile data: $it") },
                )

                Screen.EDIT_CLOCKSET -> EditClockSetScreen(
                    profileData = profileTestData,
                    timeControlData = timeControlTestData,
                    clockSet = clockSetTestData[0],
                    onSubmitClockSet = { println("ClockSet data: $it") },
                    onSubmitProfile = { _ -> },
                    onDeleteProfile = { _ -> },
                    onSubmitTimeControl = { _ -> },
                    onDeleteTimeControl = { _ -> },
                )

                Screen.LIST -> ListScreen(
                    data = timeControlTestData,
                    listType = ListType.TIME_CONTROL,
                    onSelect = { println("Selected: $it") },
                    onSubmit = { println("Submit: $it") },
                    onDelete = { println("Delete: $it") }
                )

                Screen.LIST_CLOCKEST -> ListClockSetScreen(
                    viewModel = viewModel,
                    onSelect = { println("Select: $it") },
                )
            }
        }
    }
}
