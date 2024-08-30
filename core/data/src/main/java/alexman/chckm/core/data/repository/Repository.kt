package alexman.chckm.core.data.repository

import alexman.chckm.core.data.Identifiable
import alexman.chckm.core.data.repository.cache.Cache
import alexman.chckm.core.data.repository.cache.InMemoryCache
import alexman.chckm.core.data.repository.datasource.DataSource
import alexman.chckm.core.data.repository.datasource.InMemoryDataSource

// TODO: document
class Repository<T: Identifiable>(
    private val dataSource: DataSource<T>,
    private val cache: Cache<T>,
) : Cache<T> by cache {

    // TODO: document
    init {
        val initialItems = dataSource.initialLoad()
        cache.writeAllItems(initialItems)
    }

    fun load() {
        val loadedItems = dataSource.load()

        cache.deleteAllItems()
        cache.writeAllItems(loadedItems)
    }

    fun store() {
        dataSource.store(cache.readAllItems())
    }

    companion object {
        fun <T : Identifiable> fake() = Repository(
            InMemoryDataSource<T>(), InMemoryCache<T>(),
        )
    }
}
