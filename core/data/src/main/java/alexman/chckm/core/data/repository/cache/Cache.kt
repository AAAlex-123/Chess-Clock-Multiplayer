package alexman.chckm.core.data.repository.cache

import alexman.chckm.core.data.Identifiable

// TODO: document
interface Cache<T: Identifiable> {

    fun readItem(id: Int): T

    fun readAllItems(): List<T>

    fun writeItem(item: T): Int

    fun writeAllItems(items: Collection<T>): List<Int> = items.map(::writeItem)

    fun deleteItem(id: Int)

    fun deleteAllItems() = readAllItems().map { it.id }.forEach(::deleteItem)
}