package alexman.chckm.core.data.repository

import alexman.chckm.core.data.Identifiable
import alexman.chckm.core.data.repository.cache.Cache
import alexman.chckm.core.data.repository.cache.InMemoryCache
import alexman.chckm.core.data.repository.datasource.DataSource
import alexman.chckm.core.data.repository.datasource.InMemoryDataSource

/**
 * Aggregates a [DataSource] and a [Cache].
 *
 * After loading data from persistent storage, the data is manipulated using the
 * cache, and then stored back to persistent storage at a later time.
 *
 * @param[T] the type of the items this repository will handle; it must have an
 * upper bound of [Identifiable].
 *
 * @constructor Calls [DataSource.initialLoad] to ensure that the data source is
 * properly initialized, and stores its result to the [cache]. This deletes
 * existing items from the cache.
 *
 * @author Alex Mandelias
 */
class Repository<T: Identifiable>(
    private val dataSource: DataSource<T>,
    private val cache: Cache<T>,
) : Cache<T> by cache {

    private fun Cache<T>.replaceItems(items: List<T>) {
        deleteAllItems()
        writeAllItems(items)
    }

    init {
        dataSource.initialLoad().let {
            cache.replaceItems(it)
        }
    }

    /**
     * Loads the items from the [dataSource] to the [cache].
     *
     * This deletes existing items from the [cache].
     */
    fun load() {
        dataSource.load().let {
            cache.replaceItems(it)
        }
    }

    /** Stores the items from the [cache] to the [dataSource]. */
    fun store() {
        cache.readAllItems().let {
            dataSource.store(it)
        }
    }

    companion object {

        /**
         * Returns a dummy Repository which does not depend on an external data
         * source.
         *
         * Everything is stored in memory; it uses an [InMemoryDataSource] in
         * combination with an [InMemoryCache].
         */
        fun <T : Identifiable> fake() = Repository(
            InMemoryDataSource<T>(), InMemoryCache<T>(),
        )
    }
}
