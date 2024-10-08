package alexman.chckm.core.data.serializer

import alexman.chckm.core.data.model.Clock
import alexman.chckm.core.data.model.ClockSet
import alexman.chckm.core.data.model.Profile
import alexman.chckm.core.data.model.TimeControl
import android.graphics.Color.parseColor
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

/**
 * Specification of the [Serializer] interface which converts to string.
 *
 * @param[T] the type from which to convert to string
 *
 * @author Alex Mandelias
 */
interface StringSerializer<T> : Serializer<T, String>

/**
 * Serializer which converts [TimeControl] objects to string and back.
 *
 * Example:
 *
 * ```
 * // time control:
 * TimeControl(id=1, timeSeconds=180, incrementSeconds=3)
 * // serialized:
 * "1~180~3"
 * ````
 */
val Serializer.Companion.StringTimeControlSerializer
    get() = object : StringSerializer<TimeControl> {

        private val SEP = "~"

        override fun serialize(item: TimeControl): String =
            with(item) {
                listOf(id, timeSeconds, incrementSeconds)
                    .joinToString(separator = SEP)
            }

        private val regex = Regex(
            pattern = listOf("\\d+", "\\d+", "\\d+") // regex parts
                .joinToString(separator = SEP) { "($it)" } // make each a group
        )

        override fun deserialize(serializedItem: String): TimeControl =
            regex.matchEntire(serializedItem)!!.let { m ->
                TimeControl.load(
                    m.groupValues[1].toInt(),
                    m.groupValues[2].toInt(),
                    m.groupValues[3].toInt(),
                )
            }
    }

/**
 * Serializer which converts [Profile] objects to string and back.
 *
 * Example:
 *
 * ```
 * // profile:
 * Profile(id=1, name="Alice", color=Color.Red)
 * // serialized:
 * 1~Alice~#FF0000
 * ```
 */
val Serializer.Companion.StringProfileSerializer
    get() = object : StringSerializer<Profile> {

        private val SEP = "~"

        override fun serialize(item: Profile): String =
            with(item) {
                val argb = Integer.toHexString(color.toArgb())
                    // prettier
                    .uppercase()
                    // remove alpha, keep RRGGBB
                    .substring(startIndex = 2)
                    // cover some weird edge case where alpha is 00 (it shouldn't be),
                    // and also RRGGBB has leading zeros; this would lead to an incomplete
                    // RRGGBB string, which the `parseColor()` function cannot parse.
                    .padStart(length = 6, padChar = '0')

                listOf(id, name, "#$argb")
                    .joinToString(separator = SEP)
            }

        private val regex = Regex(
            pattern = listOf("\\d+", Profile.NAME_REGEX.pattern, "#\\w+") // regex parts
                .joinToString(separator = SEP) { "($it)" } // make each a group
        )

        override fun deserialize(serializedItem: String): Profile =
            regex.matchEntire(serializedItem)!!.let { m ->
                Profile.load(
                    m.groupValues[1].toInt(),
                    m.groupValues[2],
                    Color(parseColor(m.groupValues[3])), // parses #RRGGBB
                )
            }
    }

/**
 * Serializer which converts [ClockSet] objects to string and back.
 *
 * Example:
 *
 * ```
 * // clock set data:
 * val profile1 = Profile(id=2, name="Alice", color=Color.Red)
 * val profile2 = Profile(id=3, name="Bob", color=Color.Green)
 * val timeControl = TimeControl(id=4, timeSeconds=180, incrementSeconds=3)
 *
 * val clock1 = Clock(profile=profile1, timeControl=timeControl,
 *                   timeLeftMillis=2000, lastSessionTimeMillis=2500)
 * val clock2 = Clock(profile=profile2, timeControl=timeControl,
 *                   timeLeftMillis=5000, lastSessionTimeMillis=6000)
 *
 * // clock set:
 * ClockSet(id=1, name="Scrabble", clocks=[clock1, clock2], currentClockIndex=0)
 * // serialized:
 * 1~Scrabble~0~[(2~4~2000~2500),(3~4~5000~6000)]
 * ```
 */
val Serializer.Companion.StringClockSetSerializer
    get() = object : StringSerializer<ClockSet> {

        private val SEP = "~"
        private val CLOCK_LIST_SEP = ","

        override fun serialize(item: ClockSet): String {

            val clockListString = item.clocks.joinToString(
                separator = CLOCK_LIST_SEP
            ) { clock ->
                with(clock) {
                    listOf(profile.id, timeControl.id, timeLeftMillis, lastSessionTimeMillis)
                        .joinToString(
                            separator = SEP,
                            prefix = "(", postfix = ")", // surround clock string with '(', ')'
                        )
                }
            }

            return with(item) {
                listOf(id, name, currentClockIndex, "[$clockListString]")
                    .joinToString(separator = SEP)
            }
        }

        private val regex = Regex(
            pattern = listOf(
                "\\d+", ClockSet.NAME_REGEX.pattern, "\\d+", "\\[.*\\]" // regex parts
            ).joinToString(separator = SEP) { "($it)" } // make each a group
        )

        private val clockRegex = Regex(
            pattern = listOf("\\d+", "\\d+", "\\d+", "\\d+") // regex parts
                .joinToString(
                    separator = SEP,
                    prefix = "\\(", postfix = "\\)", // clock string is surrounded by '(', ')'
                    transform = { "($it)" }, // make each a group
                )
        )

        override fun deserialize(serializedItem: String): ClockSet =
            regex.matchEntire(serializedItem)!!.let { m ->
                val id = m.groupValues[1].toInt()
                val name = m.groupValues[2]
                val currentClockIndex = m.groupValues[3].toInt()
                val clockListString = m.groupValues[4].let {
                    it.substring(1, it.length - 1) // remove '[', ']' around string
                }

                val clocks = clockListString.split(",").map { clockString ->
                    clockRegex.matchEntire(clockString)!!.let { m2 ->
                        val profileId = m2.groupValues[1].toInt()
                        val timeControlId = m2.groupValues[2].toInt()
                        val timeLeftMillis = m2.groupValues[3].toLong()
                        val lastSessionTimeMillis = m2.groupValues[4].toLong()

                        val partialProfile = Profile.EMPTY.copy(id = profileId)
                        val partialTimeControl = TimeControl.EMPTY.copy(id = timeControlId)

                        Clock.load(
                            partialProfile,
                            partialTimeControl,
                            timeLeftMillis,
                            lastSessionTimeMillis,
                        )
                    }
                }

                ClockSet.load(id, name, clocks, currentClockIndex)
            }
    }
