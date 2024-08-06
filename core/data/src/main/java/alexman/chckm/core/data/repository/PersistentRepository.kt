package alexman.chckm.core.data.repository

// TODO: document
interface Identifiable {
    var id: Int
    // this goes against existing naming conventions
    // however, this val functions as a companion object
    // constant, it should not be treated as a member,
    // and the different name indicates exactly that
    // (it's not possible to declare an abstract and
    // overridable companion object constant in an Interface)
    val id_not_set_constant: Int
}

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
