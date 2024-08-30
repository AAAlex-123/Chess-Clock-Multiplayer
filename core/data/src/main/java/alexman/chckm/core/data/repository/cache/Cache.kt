package alexman.chckm.core.data.repository.cache

import alexman.chckm.core.data.Identifiable

/**
 * Defines the functionality of a cache which stores [Identifiable] objects.
 *
 * Besides the usual read, write and delete operations, this cache is also
 * responsible for setting the id of new items when they are first written to
 * it. This is described in [writeItem].
 *
 * @param[T] the type of the items this cache will store; it must have an upper
 * bound of [Identifiable].
 *
 * @author Alex Mandelias
 */
interface Cache<T: Identifiable> {

    /**
     * Queries this cache with the given id.
     *
     * @param[id] the id of the item to return
     *
     * @return the item with the given id.
     *
     * @throws[IllegalArgumentException] if no item with the given id exists.
     */
    fun readItem(id: Int): T

    /**
     * Queries this cache for all the items.
     *
     * @return a list of all the items in this cache.
     */
    fun readAllItems(): List<T>

    /**
     * Writes an item to this cache.
     *
     * The item is copied, so any changes to the original item will not be
     * reflected in the item in this cache.
     *
     * If the item's id is equal to [Identifiable.id_not_set_constant], then a
     * new id is assigned to this item when it is stored. Otherwise, the item
     * with the same id as the given item is overwritten to the given item.
     *
     * @param[item] the item to write
     *
     * @return the id of the item actually written, which is different than the
     * given item's id if that is equal to [Identifiable.id_not_set_constant].
     */
    fun writeItem(item: T): Int

    /**
     * Writes all items from a collection to this cache.
     *
     * The default implementation simply calls [writeItem] for each item in the
     * given collection.
     *
     * @param[items] the collection of items to write
     *
     * @return a list of the ids of the items written, in the same order as the
     * items in the given collection. The ids may be different that the original
     * ones, as described in [writeItem].
     */
    fun writeAllItems(items: Collection<T>): List<Int> = items.map(::writeItem)

    /**
     * Deletes an item from this cache with the given id.
     *
     * @param[id] the id of the item to delete
     *
     * @throws[IllegalArgumentException] if no item with the given id exists.
     */
    fun deleteItem(id: Int)

    /**
     * Deletes all items from this cache.
     *
     * The default implementation simply calls [deleteItem] for each item in
     * this cache. More specifically, for each item returned by [readAllItems].
     */
    fun deleteAllItems() = readAllItems().map { it.id }.forEach(::deleteItem)
}
