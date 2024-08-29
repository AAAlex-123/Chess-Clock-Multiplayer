package alexman.chckm.core.data.repository

import alexman.chckm.core.data.repository.cache.Cache
import alexman.chckm.core.data.repository.cache.InMemoryCache

// TODO: document
interface Persistent {

    fun load()

    fun store()
}

// TODO: document
abstract class PersistentRepository<T: Identifiable>
    : Cache<T> by InMemoryCache(), Persistent
