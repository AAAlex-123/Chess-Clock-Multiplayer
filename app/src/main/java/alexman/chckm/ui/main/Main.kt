package alexman.chckm.ui.main

import alexman.chckm.core.designsystem.component.PreviousIcon
import alexman.chckm.core.designsystem.component.ChckmButton
import alexman.chckm.core.designsystem.component.ChckmCard
import alexman.chckm.core.designsystem.component.ChckmTextDM
import alexman.chckm.core.designsystem.component.ChckmTextM
import alexman.chckm.core.designsystem.component.EditIcon
import alexman.chckm.core.designsystem.component.FlagIcon
import alexman.chckm.core.designsystem.component.NextIcon
import alexman.chckm.core.designsystem.component.PauseIcon
import alexman.chckm.core.designsystem.component.ResetIcon
import alexman.chckm.core.designsystem.component.ResumeIcon
import alexman.chckm.core.data.model.Clock
import alexman.chckm.core.data.model.ClockSet
import alexman.chckm.core.data.model.Profile
import alexman.chckm.core.designsystem.component.LocalSizes
import alexman.chckm.core.designsystem.component.Sizes
import alexman.chckm.ui.screen.ChckmViewModel
import alexman.chckm.ui.screen.EditTimeControlScreen
import alexman.chckm.ui.screen.ListClockSetScreen
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.ceil

private enum class ClockState {
    STOPPED, RUNNING,
}

private enum class MainScreenEnum {
    MAIN, LOAD_CLOCKSET, EDIT_TIMECONTROL
}

@Composable
fun MainScreen(
    chckmViewModel: ChckmViewModel,
    countDownViewModel: CountDownViewModel,
) {
    var screen by remember { mutableStateOf(MainScreenEnum.LOAD_CLOCKSET) }

    var clockSet by remember { mutableStateOf(ClockSet.EMPTY) }
    var clockState by remember { mutableStateOf(ClockState.STOPPED) }
    val countDownState = countDownViewModel.countDownUiState.collectAsState()

    // update timer each time the screen changes to MAIN
    LaunchedEffect(screen) {
        if (screen == MainScreenEnum.MAIN) {
            countDownViewModel.setNewTimeMillis(clockSet.currentClock.timeLeftMillis)
        }
    }

    fun updateClockTimeLeftMillisFromCountDownAndIncrement() {

        val currentTimeLeftMillis = countDownState.value.timeLeftMillis

        val newClock = clockSet.currentClock.let {

            val incrMillis = run {
                // no time has passed, clock hasn't run, just clicked next, no increment
                if (it.lastSessionTimeMillis - currentTimeLeftMillis == 0L)
                    return@run 0L

                // flag, no increment
                if (currentTimeLeftMillis == 0L)
                    return@run 0L

                it.timeControl.incrementSeconds * 1000L
            }

            return@let it.copy(
                timeLeftMillis = currentTimeLeftMillis + incrMillis,
            )
        }

        clockSet = clockSet.updateCurrentClock(newClock)
    }

    fun resetClockLastSessionTimeMillis() {
        val newClock = clockSet.currentClock.let {
            it.copy(lastSessionTimeMillis = it.timeLeftMillis)
        }

        clockSet = clockSet.updateCurrentClock(newClock)
    }

    fun setTimerToCurrentClock() {
        countDownViewModel.setNewTimeMillis(clockSet.currentClock.timeLeftMillis)
    }

    fun onPause() {
        countDownViewModel.pause()
        clockState = ClockState.STOPPED
    }

    fun onResume() {
        countDownViewModel.resume()
        clockState = ClockState.RUNNING
    }

    fun onNext(skipFlagged: Boolean) {
        updateClockTimeLeftMillisFromCountDownAndIncrement()
        clockSet = clockSet.nextIndex(skipFlagged)
        chckmViewModel.writeClockSet(clockSet)
        resetClockLastSessionTimeMillis()
        setTimerToCurrentClock()
    }

    fun onPrev(skipFlagged: Boolean) {
        updateClockTimeLeftMillisFromCountDownAndIncrement()
        clockSet = clockSet.prevIndex(skipFlagged)
        chckmViewModel.writeClockSet(clockSet)
        resetClockLastSessionTimeMillis()
        setTimerToCurrentClock()
    }

    fun onFlag() {
        countDownViewModel.setNewTimeMillis(0L)
        onPause()
    }

    fun onReset() {
        countDownViewModel.setNewTimeMillis(clockSet.currentClock.lastSessionTimeMillis)
        onPause()
    }

    fun onEdit() {
        onPause()
        screen = MainScreenEnum.EDIT_TIMECONTROL
    }

    fun onResetGame() {
        onPause()
        clockSet = clockSet.resetAll()
        chckmViewModel.writeClockSet(clockSet)
        setTimerToCurrentClock()
    }

    fun onLoad() {
        onPause()
        screen = MainScreenEnum.LOAD_CLOCKSET
    }

    val currentTimeLeftMillis = countDownState.value.timeLeftMillis

    // going "back" from the game means loading another clock set
    BackHandler { onLoad() }

    when (screen) {
        MainScreenEnum.MAIN -> MainScreenContent(
            clockSet = clockSet,
            currentTimeLeftMillis = currentTimeLeftMillis,
            clockState = clockState,
            onPause = ::onPause,
            onResume = ::onResume,
            onNext = ::onNext,
            onPrev = ::onPrev,
            onFlag = ::onFlag,
            onReset = ::onReset,
            onEdit = ::onEdit,
            onResetGame = ::onResetGame,
            onLoad = ::onLoad,
        )
        MainScreenEnum.LOAD_CLOCKSET -> ListClockSetScreen(
            viewModel = chckmViewModel,
            onSelect = {
                clockSet = it
                screen = MainScreenEnum.MAIN
            },
        )
        MainScreenEnum.EDIT_TIMECONTROL -> EditTimeControlScreen(
            timeControl = clockSet.currentClock.let {
                it.timeControl.copy(timeSeconds = (it.timeLeftMillis / 1000).toInt())
            },
            onSubmitTimeControl = { tc ->
                val newClock = clockSet.currentClock.let { clock ->

                    // change only increment, timeSeconds is needed to reset game
                    // note that this change in increment will be lost when the
                    // game is stored and reloaded from persistent storage
                    val newTimeControl = clock.timeControl.copy(
                        incrementSeconds = tc.incrementSeconds,
                    )

                    // update the increment, and only change timeLeftMillis and
                    // lastSessionTimeMillis. the actual time control will not
                    // be updated, since we need the original one to reset game
                    clock.copy(
                        timeControl = newTimeControl,
                        timeLeftMillis = tc.timeSeconds * 1000L,
                        lastSessionTimeMillis = tc.timeSeconds * 1000L,
                    )
                }
                clockSet = clockSet.updateCurrentClock(newClock)
                chckmViewModel.writeClockSet(clockSet)
                countDownViewModel.setNewTimeMillis(newClock.timeLeftMillis)
                screen = MainScreenEnum.MAIN
            },
            onNavigateBack = {
                // cancel TimeControl edit:
                // don't submit updated time control, just return to MAIN
                screen = MainScreenEnum.MAIN
            },
        )
    }
}

@Composable
private fun MainScreenContent(
    clockSet: ClockSet,
    currentTimeLeftMillis: Long,
    clockState: ClockState,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onNext: (Boolean) -> Unit,
    onPrev: (Boolean) -> Unit,
    onFlag: () -> Unit,
    onReset: () -> Unit,
    onEdit: () -> Unit,
    onResetGame: () -> Unit,
    onLoad: () -> Unit,
) {
    val flagged = currentTimeLeftMillis == 0L
    val resetEnabled = clockSet.currentClock.lastSessionTimeMillis != currentTimeLeftMillis

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        OtherPlayersRow(clockSet = clockSet)
        BigPlayerClock(
            profile = clockSet.currentClock.profile,
            currentTimeLeftMillis = currentTimeLeftMillis,
            onClick = { onNext(true) },
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CompositionLocalProvider(value = LocalSizes provides Sizes.Primary) {
                PreviousIcon(
                    onClick = { onPrev(false) },
                    enabled = clockState == ClockState.STOPPED,
                )
                when (clockState) {
                    ClockState.STOPPED -> ResumeIcon(
                        onClick = { onResume() },
                        enabled = !flagged,
                    )
                    ClockState.RUNNING -> PauseIcon(
                        onClick = { onPause() },
                    )
                }
                NextIcon(
                    onClick = { onNext(false) },
                    enabled = clockState == ClockState.STOPPED,
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            CompositionLocalProvider(value = LocalSizes provides Sizes.Primary) {
                ResetIcon(
                    onClick = { onReset() },
                    enabled = resetEnabled,
                )
                EditIcon(
                    onClick = { onEdit() },
                )
                FlagIcon(
                    onClick = { onFlag() },
                    enabled = !flagged,
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(modifier = Modifier.weight(1f)) {
                ChckmButton(
                    text = "Reset All",
                    onClick = { onResetGame() },
                    modifier = Modifier.align(Alignment.Center),
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                ChckmButton(
                    text = "Load",
                    onClick = { onLoad() },
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        }
    }
}

private fun msToTimeString(ms: Long, longFormat: Boolean): String {
    val tenths = (ms / 100) % 10
    val seconds = (ms / 1000) % 60
    val minutes = (ms / (1000)) / 60

    val t = "$tenths"
    val s = "$seconds".padStart(length = 2, padChar = '0')
    val m = "$minutes"

    return if (longFormat) "$m:$s.$t" else "$m:$s"
}

@Composable
private fun OtherPlayersRow(
    clockSet: ClockSet,
) {
    val absoluteMaxClocksPerRow = 3
    val clockCount = clockSet.clocks.size

    var rows = 0
    var clocksPerRow: Int

    // add rows while clocksPerRow exceeds maximum
    do {
        rows += 1
        clocksPerRow = ceil(clockCount / rows.toDouble()).toInt()
    } while (clocksPerRow > absoluteMaxClocksPerRow)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        var clockList = clockSet.clocks

        do {
            val (head, rest) = clockList.take(clocksPerRow) to clockList.drop(clocksPerRow)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                head.forEach {
                    SmallPlayerClock(clock = it)
                }
            }

            clockList = rest

        } while (clockList.isNotEmpty())
    }
}

@Composable
private fun RowScope.SmallPlayerClock(
    clock: Clock,
) {
    ChckmCard(
        onClick = { },
        modifier = Modifier.weight(1f),
        backgroundColor = clock.profile.color.copy(alpha = 0.5f),
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ChckmTextM(text = clock.profile.name)
            ChckmTextM(
                text = msToTimeString(
                    clock.timeLeftMillis,
                    longFormat = false
                )
            )
        }
    }
}

@Composable
private fun ColumnScope.BigPlayerClock(
    profile: Profile,
    currentTimeLeftMillis: Long,
    onClick: () -> Unit,
) {
    ChckmCard(
        onClick = onClick,
        modifier = Modifier
            .aspectRatio(ratio = 1f)
            .weight(1f),
        backgroundColor = profile.color.copy(alpha = 0.5f),
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ChckmTextDM(text = profile.name)
                ChckmTextDM(
                    text = msToTimeString(
                        currentTimeLeftMillis,
                        longFormat = true
                    )
                )
            }
        }
    }
}
