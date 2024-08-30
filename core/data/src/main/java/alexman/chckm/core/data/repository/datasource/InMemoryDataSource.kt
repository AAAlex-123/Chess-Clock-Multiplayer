package alexman.chckm.core.data.repository.datasource

/**
 * An implementation of the [DataSource] interface, which just stores data in
 * memory.
 *
 * @param[T] the type of the items this in-memory data source will handle
 *
 * @author Alex Mandelias
 */
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
