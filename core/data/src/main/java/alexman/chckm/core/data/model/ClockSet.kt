package alexman.chckm.core.data.model

import alexman.chckm.core.data.repository.Identifiable

/**
 * Represents a ClockSet, a collection of clocks used to play a game.
 *
 * Groups together a number of [Clock] objects, one to keep track of the time
 * passed for each player.
 *
 * @property[id] the id of this clock set; its value is handled by the
 * repository responsible for ClockSet objects.
 * @property[name] the name of this clock set; used for display purposes.
 * @property[clocks] the Clock objects which make up this set
 * @property[currentClockIndex] the index of the current active Clock
 *
 * @constructor private, use the [ClockSet.new] and [ClockSet.load] functions
 * instead.
 *
 * @author Alex Mandelias
 */
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

    /**
     * Returns the current clock of this clock set, as specified by
     * [currentClockIndex].
     *
     * @return the current clock
     *
     * @throws[NoSuchElementException] if there are no clocks in this set.
     */
    val currentClock: Clock
        get() = if (clocks.isEmpty()) throw NoSuchElementException()
                else clocks[currentClockIndex]

    /**
     * Returns a copy of this ClockSet where the current clock is now the next
     * one.
     *
     * @param[skipFlagged] if `true`, the current clock will be continuously set
     * to the next one, until a clock with some time left (non-zero
     * `timeLeftMillis`) is found. If there are none, this Clock Set is
     * returned. If `false`, current clock is simply set to the next one.
     *
     * @return the updated ClockSet
     */
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

    /**
     * Returns a copy of this ClockSet where the current clock is now the
     * previous one.
     *
     * @param[skipFlagged] if `true`, the current clock will be continuously set
     * to the previous one, until a clock with some time left (non-zero
     * `timeLeftMillis`) is found. If there are none, this Clock Set is
     * returned. If `false`, current clock is simply set to the previous one.
     *
     * @return the updated ClockSet
     */
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

    /**
     * Returns a copy of this ClockSet where the current clock has been updated.
     *
     * @param[newClock] the Clock to replace the current clock with
     *
     * @return the updated ClockSet
     */
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

    /**
     * Returns a copy of this ClockSet where all of the Clocks have been reset.
     *
     * Resetting a Clock entails simply setting its timeLeftMillis and
     * lastSessionTimeMillis to their original values, as specified by the
     * Clock's time control.
     *
     * @return the updated ClockSet
     */
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

        /** The default empty ClockSet object. */
        val EMPTY: ClockSet = ClockSet(
            id = ID_NOT_SET,
            name = "",
            clocks = listOf(),
            currentClockIndex = 0,
        )

        /**
         * Creates a new ClockSet object with the given parameters.
         *
         * The [id] parameter is set to [id_not_set_constant], and the
         * persistent storage will then set it to an appropriate value.
         * Additionally, the [currentClockIndex] parameter is set to `0`.
         *
         * @param[name] the name of the new Profile
         * @param[clocks] the clocks of the new ClockSet
         *
         * @return the new ClockSet object.
         */
        fun new(name: String, clocks: List<Clock>) =
            ClockSet(ID_NOT_SET, name, clocks, currentClockIndex = 0)

        /**
         * Creates a new ClockSet object with the given parameters.
         *
         * These parameters are (supposed to be) known, since they are read from
         * some sort of persistent storage, therefore they are all known here.
         *
         * @param[id] the value for [ClockSet.id] read
         * @param[name] the value for [ClockSet.name] read
         * @param[clocks] the value for [ClockSet.clocks] read
         * @param[currentClockIndex] the value for
         * [ClockSet.currentClockIndex] read
         *
         * @return the ClockSet object as loaded from persistent storage.
         */
        fun load(id: Int, name: String, clocks: List<Clock>, currentClockIndex: Int) =
            ClockSet(id, name, clocks, currentClockIndex)

        // regex allows: letters, numbers, underscore, dash, space
        // cannot start with dash or space, must be at least 1 character
        /** The regex used to validate a ClockSet name. */
        val NAME_REGEX = Regex(pattern = "\\w[\\w\\- ]*")

        /**
         * Checks if the given name is valid according to the [NAME_REGEX].
         *
         * @param[name] the name to check
         *
         * @return `true` if the name is valid, `false` otherwise.
         */
        fun validateName(name: String) = NAME_REGEX.matches(name)
    }
}
