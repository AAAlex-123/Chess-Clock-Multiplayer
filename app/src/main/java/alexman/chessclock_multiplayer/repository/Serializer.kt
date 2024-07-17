package alexman.chessclock_multiplayer.repository

// TODO: document
interface Serializer<T, U> {

    fun serialize(item: T): U

    fun deserialize(serialized: U): T

    companion object
}

// TODO: document
interface StringSerializer<T>: Serializer<T, String>
