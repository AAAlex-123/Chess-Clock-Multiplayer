package alexman.chessclock_multiplayer.repository

// TODO: document
class FakePersistentRepository<T: Identifiable>
    : PersistentRepository<T>() {

    override fun load() { }

    override fun store() { }
}
