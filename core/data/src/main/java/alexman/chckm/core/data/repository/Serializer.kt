package alexman.chckm.core.data.repository

import alexman.chckm.core.data.model.Clock
import alexman.chckm.core.data.model.ClockSet
import alexman.chckm.core.data.model.Profile
import alexman.chckm.core.data.model.TimeControl
import android.graphics.Color.parseColor
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb

// TODO: document
interface Serializer<T, U> {

    fun serialize(item: T): U

    fun deserialize(serializedItem: U): T

    companion object
}

// TODO: document
interface StringSerializer<T>: Serializer<T, String>

// TODO: document
val Serializer.Companion.StringTimeControlSerializer
    get() = object : StringSerializer<TimeControl> {

        private val SEP = "~"

        override fun serialize(item: TimeControl): String =
            with(item) {
                "$id$SEP$timeSeconds$SEP$incrementSeconds"
            }

        private val regex = Regex(
            pattern = "(\\d+)$SEP(\\d+)$SEP(\\d+)"
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

// TODO: document
val Serializer.Companion.StringProfileSerializer
    get() = object : StringSerializer<Profile> {

        private val SEP = "~"

        override fun serialize(item: Profile): String =
            with(item) {
                val argb = Integer.toHexString(color.toArgb()).uppercase()
                "$id$SEP$name$SEP#${argb}"
            }

        private val regex = Regex(
            pattern = "(\\d+)$SEP(${Profile.NAME_REGEX.pattern})$SEP(#\\w+)"
        )

        override fun deserialize(serializedItem: String): Profile =
            regex.matchEntire(serializedItem)!!.let { m ->
                Profile.load(
                    m.groupValues[1].toInt(),
                    m.groupValues[2],
                    Color(parseColor(m.groupValues[3])),
                )
            }
    }

// TODO: document
val Serializer.Companion.StringClockSetSerializer
    get() = object : StringSerializer<ClockSet> {

        private val SEP = "~"

        override fun serialize(item: ClockSet): String {

            val clocksString = item.clocks.joinToString(separator=",") {
                "(${it.profile.id}$SEP${it.timeControl.id}$SEP" +
                        "${it.timeLeftMillis}$SEP${it.lastSessionTimeMillis})"
            }

            return with(item) {
                "$id$SEP$name$SEP$currentClockIndex$SEP[$clocksString]"
            }
        }

        private val regex = Regex(
            pattern = "(\\d+)$SEP(${ClockSet.NAME_REGEX.pattern})$SEP(\\d+)$SEP\\[(.*)]"
        )
        private val clockRegex = Regex(
            pattern = "\\((\\d+)$SEP(\\d+)$SEP(\\d+)$SEP(\\d+)\\)"
        )

        override fun deserialize(serializedItem: String): ClockSet =
            regex.matchEntire(serializedItem)!!.let { m ->
                val id = m.groupValues[1].toInt()
                val name = m.groupValues[2]
                val currentClockIndex = m.groupValues[3].toInt()
                val clocksString = m.groupValues[4]

                val clocks = clocksString.split(",").map { clockString ->
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
