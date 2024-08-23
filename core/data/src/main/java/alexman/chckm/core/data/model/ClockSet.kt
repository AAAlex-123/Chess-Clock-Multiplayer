package alexman.chckm.core.data.model

import alexman.chckm.core.data.repository.Identifiable

// TODO: document
data class ClockSet private constructor(
    // should be ID_NOT_SET when creating new TimeControl
    override var id: Int = ID_NOT_SET,
    val name: String,
    val clocks: List<Clock>,
    internal val currentClockIndex: Int, // only used in Serializer
) : Displayable(), Identifiable {

    override val id_not_set_constant: Int = ID_NOT_SET

    override val displayString: String = name

    // cache the comparator used in compareToImpl()
    private val comparator = compareBy<ClockSet> { it.displayString.lowercase() }

    override fun compareToImpl(other: Displayable): Int =
        comparator.compare(this, other as ClockSet)

    val currentClock: Clock
        get() = if (clocks.isEmpty()) throw NoSuchElementException()
                else clocks[currentClockIndex]

    fun nextIndex(skipFlagged: Boolean): ClockSet {

        var newIndex = (currentClockIndex + 1) % clocks.size

        if (skipFlagged) {
            // keep going until a clock with some time left is found
            while (clocks[newIndex].timeLeftMillis == 0L) {
                newIndex = (newIndex + 1) % clocks.size

                // newIndex circled all the way back to currentClockIndex,
                // all clocks are flagged, there is no 'next' clock
                if (newIndex == currentClockIndex)
                    return this
            }
        }

        return copy(currentClockIndex = newIndex)
    }

    fun prevIndex(skipFlagged: Boolean): ClockSet {

        var newIndex = (currentClockIndex - 1 + clocks.size) % clocks.size

        if (skipFlagged) {
            // keep going until a clock with some time left is found
            while (clocks[newIndex].timeLeftMillis == 0L) {
                newIndex = (newIndex - 1 + clocks.size) % clocks.size

                // newIndex circled all the way back to currentClockIndex,
                // all clocks are flagged, there is no 'previous' clock
                if (newIndex == currentClockIndex)
                    return this
            }
        }

        return copy(currentClockIndex = newIndex)
    }

    fun updateCurrentClock(newClock: Clock): ClockSet {
        val newClockList = clocks.mapIndexed { index, clock ->
            // replace clock at currentClockIndex with newClock
            if (index == currentClockIndex)
                newClock
            // keep other clocks the same
            else clock
        }

        return copy(clocks = newClockList)
    }

    fun resetAll(): ClockSet {
        // this effectively resets timeLeftMillis and
        // lastSessionTimeMillis for each Clock
        val newClockList = clocks.map {
            Clock.new(it.profile, it.timeControl)
        }

        return copy(clocks = newClockList)
    }

    companion object {
        // required since member val cannot be used in companion object
        private const val ID_NOT_SET = -1

        val EMPTY: ClockSet = ClockSet(
            id = ID_NOT_SET,
            name = "",
            clocks = listOf(),
            currentClockIndex = 0,
        )

        // use this instead of constructor, it sets id automatically
        fun new(name: String, clocks: List<Clock>) =
            ClockSet(ID_NOT_SET, name, clocks, currentClockIndex = 0)

        fun load(id: Int, name: String, clocks: List<Clock>, currentClockIndex: Int) =
            ClockSet(id, name, clocks, currentClockIndex)

        // regex allows: letters, numbers, underscore, dash, space
        // cannot start with dash or space, must be at least 1 character
        val NAME_REGEX = Regex(pattern = "\\w[\\w\\- ]*")
        fun validateName(name: String) = NAME_REGEX.matches(name)
    }
}
