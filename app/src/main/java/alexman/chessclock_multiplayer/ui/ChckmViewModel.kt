package alexman.chessclock_multiplayer.ui

import alexman.chessclock_multiplayer.model.ClockSet
import alexman.chessclock_multiplayer.model.Profile
import alexman.chessclock_multiplayer.model.TimeControl
import alexman.chessclock_multiplayer.repository.PersistentRepository
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ListDataState<T>(
    val data: List<T> = listOf(),
)

class ChckmViewModel(
    private val profileRepository: PersistentRepository<Profile>,
    private val timeControlRepository: PersistentRepository<TimeControl>,
    private val clockSetRepository: PersistentRepository<ClockSet>,
): ViewModel() {

    private val _profileListDataUiState = MutableStateFlow(ListDataState<Profile>())
    val profileListDataUiState = _profileListDataUiState.asStateFlow()

    private val _timeControlListDataUiState = MutableStateFlow(ListDataState<TimeControl>())
    val timeControlListDataUiState = _timeControlListDataUiState.asStateFlow()

    private val _clockSetListDataUiState = MutableStateFlow(ListDataState<ClockSet>())
    val clockSetListDataUiState = _clockSetListDataUiState.asStateFlow()

    init {
        refreshProfileState()
        refreshTimeControlState()
        refreshClockSetState()
    }

    fun writeProfile(item: Profile) {
        profileRepository.writeItem(item)
        profileRepository.store()
        refreshProfileState()
    }

    fun deleteProfile(item: Profile) {
        profileRepository.deleteItem(item.id)
        profileRepository.store()
        refreshProfileState()
    }

    fun writeTimeControl(item: TimeControl) {
        timeControlRepository.writeItem(item)
        timeControlRepository.store()
        refreshTimeControlState()
    }

    fun deleteTimeControl(item: TimeControl) {
        timeControlRepository.deleteItem(item.id)
        timeControlRepository.store()
        refreshTimeControlState()
    }

    fun writeClockSet(item: ClockSet) {
        clockSetRepository.writeItem(item)
        clockSetRepository.store()
        refreshClockSetState()
    }

    fun deleteClockSet(item: ClockSet) {
        clockSetRepository.deleteItem(item.id)
        clockSetRepository.store()
        refreshClockSetState()
    }

    private fun refreshProfileState() {
        _profileListDataUiState.update { currentData ->
            currentData.copy(data = profileRepository.readAllItems())
        }
    }

    private fun refreshTimeControlState() {
        _timeControlListDataUiState.update { currentData ->
            currentData.copy(data = timeControlRepository.readAllItems())
        }
    }

    private fun refreshClockSetState() {
        _clockSetListDataUiState.update { currentData ->
            currentData.copy(data = clockSetRepository.readAllItems())
        }
    }
}
