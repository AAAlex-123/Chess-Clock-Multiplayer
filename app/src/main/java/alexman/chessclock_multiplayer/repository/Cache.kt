package alexman.chessclock_multiplayer.repository

// TODO: document
class Cache<T : Identifiable> : Repository<T> {

    private val cache = mutableMapOf<Int, T>()

    override fun readItem(id: Int): T {
        println("Cache: reading item: $id")
        ensureIdExists(id)
        return cache[id]!!
    }

    override fun readAllItems(): List<T> {
        println("Cache: reading all items")
        return cache.values.toList()
    }

    override fun writeItem(item: T) {
        // ID_NOT_SET indicates creation of a new item
        // otherwise indicates update of an existing one
        val newId = if (item.id == item.id_not_set_constant)
            nextAvailableId(item) else item.id

        // lol
        // this also changes original item's id
        // since we don't make a copy
        // since item is not a dataclass

        // maybe Cloneable interface?
        // maybe add setId method to Identifiable?
        // maybe just make id var? (current solution)
        item.id = newId

        println("Cache: writing item: ${item.id}")
        cache[item.id] = item
    }

    override fun deleteItem(id: Int) {
        println("Cache: deleting item: $id")
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
