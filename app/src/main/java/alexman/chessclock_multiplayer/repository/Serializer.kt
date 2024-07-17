package alexman.chessclock_multiplayer.repository

import alexman.chessclock_multiplayer.model.TimeControl
import alexman.chessclock_multiplayer.model.TimeControlType

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

        private val regex = "(\\d+)-(\\d+)-(\\d+)-(\\w+)"

        override fun deserialize(serializedItem: String): TimeControl =
            Regex(regex).matchEntire(serializedItem)!!.let {
                TimeControl(
                    it.groupValues[1].toInt(),
                    it.groupValues[2].toInt(),
                    it.groupValues[3].toInt(),
                    TimeControlType.valueOf(it.groupValues[4]),
                )
            }
    }
