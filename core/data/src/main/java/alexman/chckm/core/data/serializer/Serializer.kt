package alexman.chckm.core.data.serializer

// TODO: document
interface Serializer<T, U> {

    fun serialize(item: T): U

    fun deserialize(serializedItem: U): T

    companion object
}
