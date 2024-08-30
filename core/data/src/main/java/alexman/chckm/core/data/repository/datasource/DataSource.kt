package alexman.chckm.core.data.repository.datasource

/**
 * Represents a Data Source, usually a persistent storage medium from where
 * data is loaded and stored to.
 *
 * @param[T] the type of the items this data source will handle
 *
 * @author Alex Mandelias
 */
interface DataSource<T> {

    /**
     * Performs the initial load of this data source.
     *
     * This should be the first call to this data source, since it initializes
     * it properly, if necessary, so that subsequent calls to [load] work as
     * expected. For example, it creates the file from which data is read if it
     * does not exist.
     *
     * The default implementation attempts to call [load], and, if it throws,
     * [recreate] is called to ensure a clean state for this data source.
     *
     * @return a list of items in this data source, or an empty list if the
     * first call to [load] throws.
     */
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

    /** Recreates this data source, meaning that existing data is erased. */
    fun recreate()

    /**
     * Loads the data from this data source.
     *
     * @return a list of the items in this data source.
     *
     * @throws[LoadException] if the data could not be loaded for any reason,
     * usually because this data source has not been initialized. If this
     * exception is thrown, [recreate] may be called as an attempt to start anew
     * with a clear data source.
     */
    fun load(): List<T>

    /**
     * Stores data to this data source.
     *
     * @param[items] the data to store
     */
    fun store(items: List<T>)
}
