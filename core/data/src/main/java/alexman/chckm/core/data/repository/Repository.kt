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

    private fun Cache<T>.replaceItems(items: List<T>) {
        deleteAllItems()
        writeAllItems(items)
    }

    // TODO: document
    init {
        dataSource.initialLoad().let {
            cache.replaceItems(it)
        }
    }

    fun load() {
        dataSource.load().let {
            cache.replaceItems(it)
        }
    }

    fun store() {
        cache.readAllItems().let {
            dataSource.store(it)
        }
    }

    companion object {
        fun <T : Identifiable> fake() = Repository(
            InMemoryDataSource<T>(), InMemoryCache<T>(),
        )
    }
}
