package alexman.chckm.core.data.repository

// TODO: document
interface Repository<T: Identifiable> {

    fun readItem(id: Int): T

    fun readAllItems(): List<T>

    fun writeItem(item: T)

    fun writeAllItems(items: Collection<T>) {
        items.forEach(::writeItem)
    }

    fun deleteItem(id: Int)

    fun deleteAllItems() {
        readAllItems().map { it.id }.forEach(::deleteItem)
    }
}

// TODO: document
interface Persistent {

    fun load()

    fun store()
}

// TODO: document
abstract class PersistentRepository<T: Identifiable>
    : Repository<T> by Cache(), Persistent
