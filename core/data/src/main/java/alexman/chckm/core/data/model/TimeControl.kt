package alexman.chckm.core.data.model

import alexman.chckm.core.data.repository.Identifiable

/**
 * Represents a TimeControl, a time and increment for a clock.
 *
 * @property[id] the id of this time control; its value is handled by the
 * repository responsible for Time Control objects.
 * @property[timeSeconds] the total time in seconds.
 * @property[incrementSeconds] the increment in seconds.
 *
 * @constructor private, use the [TimeControl.new] and [TimeControl.load]
 * functions instead.
 *
 * @author Alex Mandelias
 */
data class TimeControl private constructor(
    // should be ID_NOT_SET when creating new TimeControl
    override var id: Int = ID_NOT_SET,
    val timeSeconds: Int,
    val incrementSeconds: Int,
) : Displayable(), Identifiable {

    override val id_not_set_constant: Int = ID_NOT_SET

    override fun copySetId(newId: Int): TimeControl = copy(id = newId)

    override val displayString: String =
        if (incrementSeconds != 0)
            "${Parser.format(timeSeconds)} | ${Parser.format(incrementSeconds)}"
        else
            Parser.format(timeSeconds)

    // cache the comparator used in compareToImpl()
    private val comparator = compareBy<TimeControl>(
        { it.timeSeconds }, { it.incrementSeconds }
    )

    override fun compareToImpl(other: Displayable): Int =
        comparator.compare(this, other as TimeControl)

    companion object {
        // required since member val cannot be used in companion object
        private const val ID_NOT_SET = -1

        /** The default empty TimeControl object. */
        val EMPTY = TimeControl(
            id = ID_NOT_SET,
            timeSeconds = 0,
            incrementSeconds = 0,
        )

        /**
         * Creates a new TimeControl object with the given parameters.
         *
         * The [id] parameter is set to [id_not_set_constant], and the
         * persistent storage will then set it to an appropriate value.
         *
         * @param[timeSeconds] the time of the new TimeControl
         * @param[incrementSeconds] the increment of the new TimeControl
         *
         * @return the new TimeControl object.
         */
        fun new(timeSeconds: Int, incrementSeconds: Int) =
            TimeControl(ID_NOT_SET, timeSeconds, incrementSeconds)

        /**
         * Creates a new TimeControl object with the given parameters.
         *
         * These parameters are (supposed to be) known, since they are read from
         * some sort of persistent storage, therefore they are all known here.
         *
         * @param[id] the value for [TimeControl.id] read
         * @param[timeSeconds] the value for [TimeControl.timeSeconds] read
         * @param[incrementSeconds] the value for [TimeControl.incrementSeconds] read
         *
         * @return the TimeControl object as loaded from persistent storage.
         */
        fun load(id: Int, timeSeconds: Int, incrementSeconds: Int) =
            TimeControl(id, timeSeconds, incrementSeconds)

        object Parser {

            private val regexMin = Regex( // 3 min
                pattern = "(\\d+)\\s*(?:m|min|mins|minute|minutes)"
            )
            private val regexSec = Regex( // 1 sec
                pattern = "(\\d+)\\s*(?:s|sec|secs|second|seconds)"
            )
            private val regexMinSec = Regex( // 3 min, 1 sec
                pattern = "${regexMin.pattern}[\\s,/|+\\-]+?${regexSec.pattern}"
            )

            /**
             * Checks if the given string is a valid time string.
             *
             * Valid time strings consist of:
             * - a (positive) number
             * - some whitespace (optional)
             * - a unit
             *
             * If the unit corresponds to minutes, then a second time string may
             * follow, after any number of separator characters (at least one),
             * this time with a seconds unit.
             *
             * Possible separator characters are any whitespace characters and
             * also: `,`, `/`, `|`, `+`, `-`
             *
             * Possible units (either singular or plural will work):
             * - Minutes: `m`, `min`, `mins`, `minute`, `minutes`
             * - Seconds: `s`, `sec`, `secs`, `second`, `seconds`
             *
             * Examples:
             * - `"0 sec"`
             * - `"15s"`
             * - `"1minutes"`
             * - `"1 min 20 sec"`
             *
             * @param[time] the time string to check
             *
             * @return `true` if the time string is valid, `false` otherwise.
             */
            fun validate(time: String): Boolean =
                time.trim().let {
                    return@let (regexMin.matches(it) || regexSec.matches(it)
                            || regexMinSec.matches(it))
                }

            /**
             * Parses the given time string as a number of seconds, while
             * checking whether it is valid.
             *
             * Examples:
             * - `"0 sec"` -> `0`
             * - `"15 sec"` -> `15`
             * - `"1 min"` -> `60`
             * - `"1 min 20 sec"` -> `80`
             *
             * @param[time] the time string to parse
             *
             * @return the number of seconds this time string represents.
             *
             * @throws[IllegalArgumentException] if the time string is invalid,
             * as determined by calling [validate] with the given time string as
             * the argument.
             */
            fun parse(time: String): Int {
                if (!validate(time))
                    throw IllegalArgumentException(
                        "\"$time\" does not correspond to a valid time string"
                    )

                time.trim().let { t ->
                    regexMin.matchEntire(t)?.groupValues?.let { return it[1].toInt() * 60 }
                    regexSec.matchEntire(t)?.groupValues?.let { return it[1].toInt() }
                    regexMinSec.matchEntire(t)?.groupValues?.let {
                        return it[1].toInt() * 60 + it[2].toInt()
                    }
                }

                // one of the above ?.let calls is guaranteed to return
                // because of `if` statement above, but kotlin doesn't know it
                // "Just to satisfy the compiler?" was autocompleted by Gemini
                throw Error("Why are we here? Just to satisfy the compiler?")
            }

            /**
             * Formats the given number of seconds as a valid time string.
             *
             * Examples:
             * - `0` -> `"0 sec"`
             * - `15` -> `"15 sec"`
             * - `60` -> `"1 min"`
             * - `80` -> `"1 min 20 sec"`
             *
             * @param[seconds] the number of seconds to format
             *
             * @return the formatted time string.
             *
             * @throws[IllegalArgumentException] if `seconds` is negative.
             */
            fun format(seconds: Int): String {
                if (seconds < 0)
                    throw IllegalArgumentException("`seconds` cannot be negative")

                val min = "min"
                val sec = "sec"

                if (seconds == 0)
                    return "0 $sec"

                val m: String = if (seconds > 59) "${seconds / 60} $min" else ""
                val s: String = if (seconds % 60 != 0) "${seconds % 60} $sec" else ""

                // if only minutes or seconds, don't add extra space
                return if (listOf(m, s).all { it != "" })
                    "$m $s" else "$m$s"
            }
        }
    }
}
