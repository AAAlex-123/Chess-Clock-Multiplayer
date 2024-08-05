package alexman.chckm.ui

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CountDownState(
    val timeLeftMillis: Long,
)

class CountDownViewModel: ViewModel() {

    private val _countDownUiState = MutableStateFlow(
        CountDownState(timeLeftMillis = 0L)
    )
    val countDownUiState = _countDownUiState.asStateFlow()

    private val countdown = ChckmCountDownTimer(
        onUpdate = ::updateFlow,
        updateIntervalMillis = 100L,
    )

    fun pause() = countdown.pause()

    fun resume() = countdown.resume()

    fun setNewTimeMillis(timeMillis: Long) {
        countdown.setTime(timeMillis)

        // send update manually because countdown might be stopped,
        // however we want an update immediately
        updateFlow(timeMillis)
    }

    private fun updateFlow(timeLeftMillis: Long) {
        _countDownUiState.update { currentState ->
            currentState.copy(timeLeftMillis = timeLeftMillis)
        }
    }
}

private class ChckmCountDownTimer(
    private val onUpdate: (Long) -> Unit,
    private val updateIntervalMillis: Long,
) {

    companion object {
        private const val LAST_TICK_NOT_SET: Long = -1L
    }

    private lateinit var timer: CountDownTimer
    private var running = false
    private var lastTick: Long = LAST_TICK_NOT_SET

    fun pause() {
        timer.cancel()
        running = false
    }

    fun resume() {
        // built-in CountDownTimer doesn't have pause/resume
        // start() restarts it, so we have to set the time manually
        // after every pause() call (which cancels the timer)
        if (lastTick != LAST_TICK_NOT_SET)
            setTime(lastTick)

        timer.start()
        running = true
    }

    fun setTime(timeMillis: Long) {
        // stop current, otherwise multiple timers running
        if (this::timer.isInitialized)
            timer.cancel()

        // clear lastTick
        lastTick = LAST_TICK_NOT_SET

        // create new CountDownTimer with the given time
        timer = object : CountDownTimer(timeMillis, updateIntervalMillis) {

            // run every `updateIntervalMillis` ms
            override fun onTick(timeLeft: Long) {
                // set lastTick, used on resume() to reset time
                lastTick = timeLeft
                onUpdate(timeLeft)
            }

            override fun onFinish() {
                // set to actual 0
                onTick(timeLeft = 0L)
            }
        }

        // start it immediately if timer was already running
        if (running)
            timer.start()
    }
}
