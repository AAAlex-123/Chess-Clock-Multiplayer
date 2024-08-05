package alexman.chckm.model

import alexman.chckm.repository.Identifiable
import alexman.chckm.ui.Displayable

// TODO: document
enum class TimeControlType {
    FISHER,
    BRONSTEIN,
    DELAY,
}

// TODO: document
data class TimeControl private constructor( // use new() and load() instead of constructor
    // should be ID_NOT_SET when creating new TimeControl
    override var id: Int = ID_NOT_SET,
    val timeSeconds: Int,
    val incrementSeconds: Int,
    val type: TimeControlType,
) : Identifiable, Displayable {

    override val id_not_set_constant: Int = ID_NOT_SET

    override val displayString: String =
        if (incrementSeconds != 0)
            "${Parser.format(timeSeconds)} | ${Parser.format(incrementSeconds)}"
        else
            Parser.format(timeSeconds)

    companion object {
        private const val ID_NOT_SET = -1

        val EMPTY = TimeControl(ID_NOT_SET, 0, 0, TimeControlType.FISHER)

        // use this instead of constructor, it sets id automatically
        fun new(timeSeconds: Int, incrementSeconds: Int, type: TimeControlType) =
            TimeControl(ID_NOT_SET, timeSeconds, incrementSeconds, type)

        fun load(id: Int, timeSeconds: Int, incrementSeconds: Int, type: TimeControlType) =
            TimeControl(id, timeSeconds, incrementSeconds, type)

        object Parser {

            private val regexMin = Regex( // 3 min
                pattern = "(\\d+)\\s*(?:m|min|mins|minute|minutes)"
            )
            private val regexSec = Regex( // 1 sec
                pattern = "(\\d+)\\s*(?:s|sec|secs|second|seconds)"
            )
            private val regexMinSec = Regex( // 3 min, 1 sec
                pattern = "${regexMin.pattern}[\\s,/|+\\-]*?${regexSec.pattern}"
            )

            fun validate(time: String): Boolean =
                time.trim().let {
                    return@let regexMin.matches(it) || regexSec.matches(it)
                            || regexMinSec.matches(it)
                }

            fun parse(time: String): Int {
                if (!validate(time))
                    throw IllegalArgumentException(
                        "\"$time\" does not correspond to a valid time string"
                    )

                val trimmedTime = time.trim()
                regexMin.matchEntire(trimmedTime)?.groupValues?.let { return it[1].toInt() * 60 }
                regexSec.matchEntire(trimmedTime)?.groupValues?.let { return it[1].toInt() }
                regexMinSec.matchEntire(trimmedTime)?.groupValues?.let {
                    return it[1].toInt() * 60 + it[2].toInt()
                }

                // one of the above ?.let calls is guaranteed to return
                // because of `if` statement above, but kotlin doesn't know it
                throw Error("Why are we here? Just to satisfy the compiler?")
            }

            fun format(seconds: Int): String {
                val min = "min"
                val sec = "sec"

                if (seconds == 0)
                    return "0 $sec"

                val m: String = if (seconds > 59) "${seconds / 60} $min" else ""
                val s: String = if (seconds % 60 != 0) "${seconds % 60} $sec" else ""

                return if (listOf(m, s).all { it != "" })
                    "$m $s" else "$m$s"
            }
        }
    }
}
