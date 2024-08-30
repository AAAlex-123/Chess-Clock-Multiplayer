package alexman.chckm.core.data.repository.cache

import alexman.chckm.core.data.Identifiable

/**
 * An implementation of the [Cache] interface, which stores data in memory.
 *
 * @param[T] the type of the items this in-memory cache will store; it must have
 * an upper bound of [Identifiable].
 *
 * @author Alex Mandelias
 */
class InMemoryCache<T : Identifiable> : Cache<T> {

    private val cache = mutableMapOf<Int, T>()

    override fun readItem(id: Int): T {
        requireIdInCache(id)
        return cache[id]!!
    }

    override fun readAllItems(): List<T> = cache.values.toList()

    override fun writeItem(item: T): Int {
        // an item with id `id_not_set_constant` indicates creation of a new item,
        // otherwise indicates update of an existing one with that id.
        // when creating a new item, we need to assign an id to it
        val newItem = item.takeIf { it.id != item.id_not_set_constant }
            ?: item.copySetId(newId = nextAvailableId(item))

        // safe cast, since copySetId() should return
        // an Identifiable of the same class as item
        cache.writeIdentifiable(newItem as T)

        return newItem.id
    }

    override fun deleteItem(id: Int) {
        requireIdInCache(id)
        cache.remove(id)
    }

    private fun nextAvailableId(item: T): Int =
        (cache.keys.maxOrNull() ?: item.id_not_set_constant) + 1

    private fun requireIdInCache(id: Int) =
        require(id in cache) {
            "Item with id $id not found"
        }

    private fun MutableMap<Int, T>.writeIdentifiable(item: T) {
        this[item.id] = item
    }
}
