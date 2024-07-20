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
            regex.matchEntire(serializedItem)!!.let {
                TimeControl(
                    it.groupValues[1].toInt(),
                    it.groupValues[2].toInt(),
                    it.groupValues[3].toInt(),
                    TimeControlType.valueOf(it.groupValues[4]),
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
            regex.matchEntire(serializedItem)!!.let {
                Profile(
                    it.groupValues[1].toInt(),
                    it.groupValues[2],
                    Color(parseColor(it.groupValues[3])),
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
            regex.matchEntire(serializedItem)!!.let {
                val id = it.groupValues[1].toInt()
                val name = it.groupValues[2]
                val currentClockIndex = it.groupValues[3].toInt()
                val clocksString = it.groupValues[4]

                val clocks = clocksString.split(",").map { clockString ->
                    clockRegex.matchEntire(clockString)!!.let { it2 ->
                        val profileId = it2.groupValues[1].toInt()
                        val timeControlId = it2.groupValues[2].toInt()
                        val timeLeftMillis = it2.groupValues[3].toInt()
                        val lastSessionTimeMillis = it2.groupValues[4].toInt()

                        val partialProfile = Profile(
                            id = profileId,
                            name = "",
                            color = Color.Unspecified,
                        )

                        val partialTimeControl = TimeControl(
                            id = timeControlId,
                            timeSeconds = 0,
                            incrementSeconds = 0,
                            type = TimeControlType.FISHER,
                        )

                        Clock.new(
                            partialProfile,
                            partialTimeControl,
                            timeLeftMillis,
                            lastSessionTimeMillis,
                        )
                    }
                }

                ClockSet(id,name, clocks, currentClockIndex)
            }
    }
