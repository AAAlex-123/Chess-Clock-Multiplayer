package alexman.chckm.core.data.repository.datasource

import alexman.chckm.core.data.serializer.StringSerializer
import android.content.Context
import java.io.FileNotFoundException

/**
 * An implementation of the [DataSource] interface, which uses a file as
 * persistent storage for data.
 *
 * @param[T] the type of the items this file data source will handle
 * @param[context] the [Context] object of the app
 * @param[fileName] the name of the file to store the data to
 * @param[serializer] the serializer to use to convert objects to strings
 */
class FileDataSource<T>(
    private val context: Context,
    private val fileName: String,
    private val serializer: StringSerializer<T>,
) : DataSource<T> {

    private companion object {
        // separator for each item in the file
        const val SEPARATOR = "\n"
    }

    override fun recreate() {
        // openFileOutput() creates file if not exists
        context.openFileOutput(fileName, Context.MODE_PRIVATE)
            .also { it.close() } // close the stream
    }

    override fun load(): List<T> {
        val fileContents: String
        try {
            context.openFileInput(fileName).bufferedReader()
                .also { fileContents = it.readText() } // read
                .also { it.close() } // close the stream
        } catch (e: FileNotFoundException) {
            throw LoadException("File $fileName does not exist", e)
        }

        return fileContents.split(SEPARATOR)
            // .filter(String::isNotEmpty)
            .map(serializer::deserialize)
    }

    override fun store(items: List<T>) {
        val fileContents = items
            .joinToString(
                separator = SEPARATOR,
                transform = serializer::serialize,
            )

        context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(fileContents.toByteArray())
        } // .use closes the stream automatically
    }
}
