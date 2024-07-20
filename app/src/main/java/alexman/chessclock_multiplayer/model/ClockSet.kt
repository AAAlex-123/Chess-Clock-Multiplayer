package alexman.chessclock_multiplayer.model

import alexman.chessclock_multiplayer.repository.Identifiable

// TODO: document
data class ClockSet(
    // should be ID_NOT_SET when creating new TimeControl
    override var id: Int = ID_NOT_SET,
    val name: String,
    val clocks: List<Clock>,
    var currentClockIndex: Int,
) : Identifiable {

    override val id_not_set_constant: Int = ID_NOT_SET

    companion object {
        private const val ID_NOT_SET = -1

        // use this instead of constructor, it sets id automatically
        fun new(
            name: String,
            clocks: List<Clock>,
            currentClockIndex: Int,
        ) = ClockSet(
            ID_NOT_SET, name, clocks, currentClockIndex
        )
    }
}
