package alexman.chckm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import alexman.chckm.core.designsystem.theme.ChckmTheme
import alexman.chckm.core.data.repository.Repository
import alexman.chckm.core.data.repository.cache.InMemoryCache
import alexman.chckm.core.data.repository.datasource.FileDataSource
import alexman.chckm.core.data.serializer.Serializer
import alexman.chckm.core.data.serializer.StringClockSetSerializer
import alexman.chckm.core.data.serializer.StringProfileSerializer
import alexman.chckm.core.data.serializer.StringTimeControlSerializer
import alexman.chckm.ui.screen.ChckmViewModel
import alexman.chckm.ui.main.CountDownViewModel
import alexman.chckm.ui.main.MainScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext

private const val TIME_CONTROL_FILE_NAME = "timecontrol_string_data.txt"
private const val PROFILE_FILE_NAME = "profile_string_data.txt"
private const val CLOCK_SET_FILE_NAME = "clockset_string_data.txt"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChckmTheme {
                ChckmScreen()
            }
        }
    }
}

@Composable
private fun ChckmScreen() {
    // initialize data sources
    val timeControlDataSource = FileDataSource(
        LocalContext.current, TIME_CONTROL_FILE_NAME, Serializer.StringTimeControlSerializer
    )
    val profileDataSource = FileDataSource(
        LocalContext.current, PROFILE_FILE_NAME, Serializer.StringProfileSerializer
    )
    val clockSetDataSource = FileDataSource(
        LocalContext.current, CLOCK_SET_FILE_NAME, Serializer.StringClockSetSerializer
    )

    // initialize repositories with data sources and a new InMemoryCache object for each
    val timeControlRepository = Repository(timeControlDataSource, InMemoryCache())
    val profileRepository = Repository(profileDataSource, InMemoryCache())
    val clockSetRepository = Repository(clockSetDataSource, InMemoryCache())

    // update the partial clockSet objects with the full profile and timeControl data,
    // since the serialized version of clockSet only stores the id of profile and timeControl data
    val newClockSets = clockSetRepository.readAllItems().map {
        it.copy(
            clocks = it.clocks.map { clock ->
                clock.copy(
                    profile = profileRepository.readItem(clock.profile.id),
                    timeControl = timeControlRepository.readItem(clock.timeControl.id),
                )
            }
        )
    }

    // overwrites old ones, since the id doesn't change
    clockSetRepository.writeAllItems(newClockSets)

    // initialize view models
    val chckmViewModel = ChckmViewModel(
        timeControlRepository = timeControlRepository,
        profileRepository = profileRepository,
        clockSetRepository = clockSetRepository,
    )

    val countDownViewModel = CountDownViewModel()

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            MainScreen(
                chckmViewModel,
                countDownViewModel,
            )
        }
    }
}
