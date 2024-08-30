package alexman.chckm.core.data.repository.datasource

// TODO: document
internal class LoadException : Exception {

    constructor(message: String): super(message)

    constructor(message: String, cause: Throwable): super(message, cause)
}
