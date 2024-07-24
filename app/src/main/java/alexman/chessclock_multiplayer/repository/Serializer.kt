package alexman.chessclock_multiplayer.repository

import alexman.chessclock_multiplayer.model.Clock
import alexman.chessclock_multiplayer.model.ClockSet
import alexman.chessclock_multiplayer.model.Profile
import alexman.chessclock_multiplayer.model.TimeControl
import alexman.chessclock_multiplayer.model.TimeControlType
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

        override fun serialize(item: TimeControl): String =
            with(item) {
                "$id-$timeSeconds-$incrementSeconds-$type"
            }

        private val regex = Regex("(\\d+)-(\\d+)-(\\d+)-(\\w+)")

        override fun deserialize(serializedItem: String): TimeControl =
            regex.matchEntire(serializedItem)!!.let { m ->
                TimeControl.load(
                    m.groupValues[1].toInt(),
                    m.groupValues[2].toInt(),
                    m.groupValues[3].toInt(),
                    TimeControlType.valueOf(m.groupValues[4]),
                )
            }
    }

// TODO: document
val Serializer.Companion.StringProfileSerializer
    get() = object : StringSerializer<Profile> {

        override fun serialize(item: Profile): String =
            with(item) {
                val argb = Integer.toHexString(color.toArgb()).uppercase()
                "$id-$name-#${argb}"
            }

        private val regex = Regex("(\\d+)-(\\w+)-(#\\w+)")

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

        override fun serialize(item: ClockSet): String {

            val clocksString = item.clocks.joinToString(separator=",") {
                "(${it.profile.id}-${it.timeControl.id}-" +
                        "${it.timeLeftMillis}-${it.lastSessionTimeMillis})"
            }

            return with(item) {
                "$id-$name-$currentClockIndex-[$clocksString]"
            }
        }

        private val regex = Regex("(\\d+)-(\\w+)-(\\d+)-\\[(.*)]")
        private val clockRegex = Regex("\\((\\d+)-(\\d+)-(\\d+)-(\\d+)\\)")

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
                        val timeLeftMillis = m2.groupValues[3].toInt()
                        val lastSessionTimeMillis = m2.groupValues[4].toInt()

                        val partialProfile = Profile.load(
                            id = profileId,
                            name = "",
                            color = Color.Unspecified,
                        )

                        val partialTimeControl = TimeControl.load(
                            id = timeControlId,
                            timeSeconds = 0,
                            incrementSeconds = 0,
                            type = TimeControlType.FISHER,
                        )

                        Clock.load(
                            partialProfile,
                            partialTimeControl,
                            timeLeftMillis,
                            lastSessionTimeMillis,
                        )
                    }
                }

                ClockSet.load(id,name, clocks, currentClockIndex)
            }
    }
