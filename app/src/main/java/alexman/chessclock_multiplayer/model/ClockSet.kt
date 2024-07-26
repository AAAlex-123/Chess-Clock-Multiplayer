package alexman.chessclock_multiplayer.model

import alexman.chessclock_multiplayer.repository.Identifiable
import alexman.chessclock_multiplayer.ui.Displayable

// TODO: document
data class ClockSet private constructor( // use new() and load() instead of constructor
    // should be ID_NOT_SET when creating new TimeControl
    override var id: Int = ID_NOT_SET,
    val name: String,
    val clocks: List<Clock>,
    val currentClockIndex: Int,
) : Identifiable, Displayable {

    override val id_not_set_constant: Int = ID_NOT_SET

    override val displayString: String = name

    companion object {
        private const val ID_NOT_SET = -1

        val EMPTY: ClockSet = ClockSet(ID_NOT_SET, "", listOf(), 0)

        // use this instead of constructor, it sets id automatically
        fun new(name: String, clocks: List<Clock>) =
            ClockSet(id = ID_NOT_SET, name, clocks, currentClockIndex = 0)

        fun load(id: Int, name: String, clocks: List<Clock>, currentClockIndex: Int) =
            ClockSet(id, name, clocks, currentClockIndex)
    }
}
