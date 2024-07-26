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
import alexman.chessclock_multiplayer.ui.ListType
import alexman.chessclock_multiplayer.ui.EditClockSetScreen
import alexman.chessclock_multiplayer.ui.EditProfileScreen
import alexman.chessclock_multiplayer.ui.EditTimeControlScreen
import alexman.chessclock_multiplayer.ui.ListScreen
import androidx.compose.ui.graphics.Color

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
    LIST, NONE,
}

@Composable
private fun TestScreen() {

    val screen = Screen.LIST

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
        when (screen) {
            Screen.NONE -> { }
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
            )
            Screen.LIST -> ListScreen(
                data = clockSetTestData,
                dataType = DataType.CLOCK_SET,
                onSelect = { println("Selected: $it") }
            )
        }
    }
}
