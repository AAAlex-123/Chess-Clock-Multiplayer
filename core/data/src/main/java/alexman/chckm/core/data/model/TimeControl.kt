package alexman.chckm.core.data.model

import alexman.chckm.core.data.repository.Identifiable

// TODO: document
data class TimeControl private constructor(
    // should be ID_NOT_SET when creating new TimeControl
    override var id: Int = ID_NOT_SET,
    val timeSeconds: Int,
    val incrementSeconds: Int,
) : Displayable(), Identifiable {

    override val id_not_set_constant: Int = ID_NOT_SET

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

        val EMPTY = TimeControl(
            id = ID_NOT_SET,
            timeSeconds = 0,
            incrementSeconds = 0,
        )

        // use this instead of constructor, it sets id automatically
        fun new(timeSeconds: Int, incrementSeconds: Int) =
            TimeControl(ID_NOT_SET, timeSeconds, incrementSeconds)

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

            fun validate(time: String): Boolean =
                time.trim().let {
                    return@let (regexMin.matches(it) || regexSec.matches(it)
                            || regexMinSec.matches(it))
                }

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
