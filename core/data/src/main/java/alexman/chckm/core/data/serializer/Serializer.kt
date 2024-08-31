package alexman.chckm.core.data.serializer

/**
 * Defines a Serializer, responsible for converting objects between two types.
 *
 * @param[T] the type from which to convert
 * @param[U] the type to which to convert
 *
 * @author Alex Mandelias
 */
interface Serializer<T, U> {

    /**
     * Serializes an item of type [T] to type [U].
     *
     * @param[item] the item to serialize
     *
     * @return the serialized item.
     */
    fun serialize(item: T): U

    /**
     * Deserializes an item of type [U] back to type [T].
     *
     * @param[serializedItem] the item to deserialize
     *
     * @return the deserialized item.
     */
    fun deserialize(serializedItem: U): T

    /** Contains all the serializers used in the app. */
    companion object
}
