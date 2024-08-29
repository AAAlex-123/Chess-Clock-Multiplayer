package alexman.chckm.core.data.repository

// TODO: document
class Cache<T : Identifiable> : Repository<T> {

    private val cache = mutableMapOf<Int, T>()

    override fun readItem(id: Int): T {
        ensureIdExists(id)
        return cache[id]!!
    }

    override fun readAllItems(): List<T> {
        return cache.values.toList()
    }

    override fun writeItem(item: T) {
        // ID_NOT_SET indicates creation of a new item
        // otherwise indicates update of an existing one
        val newId = if (item.id == item.id_not_set_constant)
            nextAvailableId(item) else item.id

        // don't change the original item's id
        val newItem = item.copySetId(newId)

        // safe cast, since copySetId() should return
        // an Identifiable of the same class as item
        cache[newItem.id] = newItem as T
    }

    override fun deleteItem(id: Int) {
        ensureIdExists(id)
        cache.remove(id)
    }

    private fun nextAvailableId(item: T): Int =
        (cache.keys.maxOrNull() ?: item.id_not_set_constant) + 1

    private fun ensureIdExists(id: Int) {
        if (id !in cache)
            throw IllegalArgumentException("Item with id $id not found")
    }
}
