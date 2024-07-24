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
import alexman.chessclock_multiplayer.model.TimeControl
import alexman.chessclock_multiplayer.model.TimeControlType
import alexman.chessclock_multiplayer.ui.EditTimeControlScreen

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
    EDIT_TIMECONTROL,
    NONE,
}

@Composable
private fun TestScreen() {

    val screen = Screen.EDIT_TIMECONTROL

    val timeControlTestData = listOf(
        TimeControl.load(0, 180, 1, TimeControlType.FISHER),
        TimeControl.load(1, 60, 1, TimeControlType.BRONSTEIN),
        TimeControl.load(2, 300, 5, TimeControlType.DELAY),
    )

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        when (screen) {
            Screen.NONE -> { }
            Screen.EDIT_TIMECONTROL -> EditTimeControlScreen(
                timeControl = timeControlTestData[0],
                onSubmitTimeControl = { println("TimeControl data: $it") }
            )
        }
    }
}
