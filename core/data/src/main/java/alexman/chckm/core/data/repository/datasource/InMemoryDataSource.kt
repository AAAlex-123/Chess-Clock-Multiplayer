package alexman.chckm.core.data.repository.datasource

// TODO: document
class InMemoryDataSource<T> : DataSource<T> {

    private lateinit var items: List<T>

    override fun recreate() {
        items = listOf()
    }

    override fun load(): List<T> =
        try {
            items
        } catch (e: UninitializedPropertyAccessException) {
            throw LoadException("Data source not initialized", e)
        }

    override fun store(items: List<T>) {
        this.items = items
    }
}
