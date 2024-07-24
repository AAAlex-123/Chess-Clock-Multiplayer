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
    NONE,
}

@Composable
private fun TestScreen() {

    val screen = Screen.NONE

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        when (screen) {
            Screen.NONE -> { }
        }
    }
}
