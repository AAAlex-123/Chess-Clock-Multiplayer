package alexman.chckm.core.data.model

import alexman.chckm.core.data.repository.Identifiable

// TODO: document
data class ClockSet private constructor( // use new() and load() instead of constructor
    // should be ID_NOT_SET when creating new TimeControl
    override var id: Int = ID_NOT_SET,
    val name: String,
    val clocks: List<Clock>,
    val currentClockIndex: Int,
) : Displayable(), Identifiable {

    override val id_not_set_constant: Int = ID_NOT_SET

    override val displayString: String = name

    private val comparator = compareBy<ClockSet> { it.displayString.lowercase() }

    override fun compareToImpl(other: Displayable): Int =
        comparator.compare(this, other as ClockSet)

    val currentClock: Clock
        get() = if (clocks.isEmpty()) throw NoSuchElementException()
                else clocks[currentClockIndex]

    fun nextIndex(skipFlagged: Boolean): ClockSet = this.let {
        val initialIndex = it.currentClockIndex

        var index = initialIndex
        do {
            index = (index + 1) % it.clocks.size

            // index circled all the way back to initialIndex,
            // all clocks are flagged, there is no 'next' clock
            if (index == initialIndex)
                return it

        } while (skipFlagged && clocks[index].timeLeftMillis == 0L)

        it.copy(currentClockIndex = index)
    }

    fun prevIndex(skipFlagged: Boolean): ClockSet = this.let {
        val initialIndex = it.currentClockIndex

        var index = initialIndex
        do {
            index = (index + it.clocks.size - 1) % it.clocks.size

            // index circled all the way back to initialIndex,
            // all clocks are flagged, there is no 'next' clock
            if (index == initialIndex)
                return it

        } while (skipFlagged && clocks[index].timeLeftMillis == 0L)

        it.copy(currentClockIndex = index)
    }

    fun updateCurrentClock(newClock: Clock): ClockSet = this.let {
        val newClockList = it.clocks.mapIndexed { index, clock ->
            // replace clock at currentClockIndex with newClock
            if (index == it.currentClockIndex)
                newClock
            // keep other clocks the same
            else clock
        }

        it.copy(clocks = newClockList)
    }

    fun resetAll(): ClockSet = this.let {
        val newClockList = it.clocks.map { clock ->
            // this effectively resets timeLeftMillis and
            // lastSessionTimeMillis of each Clock
            Clock.new(clock.profile, clock.timeControl)
        }

        it.copy(clocks = newClockList)
    }

    companion object {
        private const val ID_NOT_SET = -1

        val EMPTY: ClockSet = ClockSet(ID_NOT_SET, "", listOf(), 0)

        // use this instead of constructor, it sets id automatically
        fun new(name: String, clocks: List<Clock>) =
            ClockSet(id = ID_NOT_SET, name, clocks, currentClockIndex = 0)

        fun load(id: Int, name: String, clocks: List<Clock>, currentClockIndex: Int) =
            ClockSet(id, name, clocks, currentClockIndex)

        // regex allows: letters, numbers, underscore, dash, space
        // cannot start with dash or space, must be at least 1 character
        val NAME_REGEX = Regex(pattern = "\\w[\\w\\- ]*")
        fun validateName(name: String) = NAME_REGEX.matches(name)
    }
}
