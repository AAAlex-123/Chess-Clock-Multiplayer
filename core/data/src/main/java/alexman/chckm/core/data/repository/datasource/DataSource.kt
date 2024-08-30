package alexman.chckm.core.data.repository.datasource

// TODO: document
interface DataSource<T> {

    fun initialLoad(): List<T> {
        try {
            return load()
        } catch (e: LoadException) {
            // recreate data source from scratch
            recreate()
            // data source is now empty
            return listOf()
        }
    }

    fun recreate()

    fun load(): List<T>

    fun store(items: List<T>)
}
