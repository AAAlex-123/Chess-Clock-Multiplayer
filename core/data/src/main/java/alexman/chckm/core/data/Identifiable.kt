package alexman.chckm.core.data

/**
 * Interface for all objects which have an [id] property.
 *
 * To use a [Repository][alexman.chckm.core.data.repository.Repository] with a
 * custom class, it must implement this interface.
 *
 * @author Alex Mandelias
 */
interface Identifiable {

    /**
     * The id of this object, its unique identifier.
     *
     * This property should not be set manually, it is handled by the
     * [Repository][alexman.chckm.core.data.repository.Repository] class.
     *
     * @see [id_not_set_constant]
     */
    val id: Int

    /**
     * Constant which indicates that if this object has that value as its
     * [id], then it is not yet set. Object implementing this interface should
     * initialize their [id] field to this value when creating a new object.
     */
    val id_not_set_constant: Int

    /**
     * Returns a copy of this object but with a new id.
     *
     * The copy should have the same class as this object, even though this
     * method is annotated as returning an Identifiable object.
     *
     * @param[newId] the new id of this object
     *
     * @return a copy of this object
     */
    fun copySetId(newId: Int): Identifiable
}
