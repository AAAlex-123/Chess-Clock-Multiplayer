package alexman.chckm.core.data.repository.datasource

/**
 * Thrown when a call to [DataSource.load] fails.
 *
 * @author Alex Mandelias
 */
internal class LoadException : Exception {

    /**
     * Constructs a new LoadException with the given message.
     *
     * @param[message] a short message explaining the cause of this exception
     */
    constructor(message: String): super(message)

    /**
     * Constructs a new LoadException with the given message and cause.
     *
     * @param[message] a short message explaining the cause of this exception
     * @param[cause] the throwable which caused this LoadException
     */
    constructor(message: String, cause: Throwable): super(message, cause)
}
