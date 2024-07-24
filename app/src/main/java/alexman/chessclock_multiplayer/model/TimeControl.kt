package alexman.chessclock_multiplayer.model

import alexman.chessclock_multiplayer.repository.Identifiable

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
) : Identifiable {

    override val id_not_set_constant: Int = ID_NOT_SET

    companion object {
        private const val ID_NOT_SET = -1

        // use this instead of constructor, it sets id automatically
        fun new(timeSeconds: Int, incrementSeconds: Int, type: TimeControlType) =
            TimeControl(ID_NOT_SET, timeSeconds, incrementSeconds, type)

        fun load(id: Int, timeSeconds: Int, incrementSeconds: Int, type: TimeControlType) =
            TimeControl(id, timeSeconds, incrementSeconds, type)
    }
}
