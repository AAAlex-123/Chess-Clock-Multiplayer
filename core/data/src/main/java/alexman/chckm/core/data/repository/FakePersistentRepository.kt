package alexman.chckm.core.data.repository

import alexman.chckm.core.data.Identifiable

// TODO: document
class FakePersistentRepository<T: Identifiable>
    : PersistentRepository<T>() {

    override fun load() { }

    override fun store() { }
}
