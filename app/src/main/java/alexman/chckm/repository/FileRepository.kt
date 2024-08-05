package alexman.chckm.repository

import android.content.Context
import java.io.FileNotFoundException

// TODO: document
class FileRepository<T : Identifiable>(
    private val context: Context,
    private val fileName: String,
    private val serializer: StringSerializer<T>,
) : PersistentRepository<T>() {

    // TODO: document
    init {
        try {
            load()
        } catch (e: FileNotFoundException) {
            // openFileOutput() creates file if not exists
            context.openFileOutput(fileName, Context.MODE_PRIVATE)
                .also { it.close() }
        }
    }

    companion object {
        // TODO: document
        const val SEPARATOR = "\n"
    }

    override fun load() {

        val fileContents: String
        context.openFileInput(fileName).bufferedReader()
            .also { fileContents = it.readText() }
            .close()

        val newItemList = fileContents.split(SEPARATOR)
            .filter(String::isNotEmpty)
            .map(serializer::deserialize)

        deleteAllItems()
        writeAllItems(newItemList)
    }

    override fun store() {

        val fileContents = readAllItems()
            .joinToString(
                separator = SEPARATOR,
                transform = serializer::serialize,
            )

        // .use closes stream
        context.openFileOutput(fileName, Context.MODE_PRIVATE).use {
            it.write(fileContents.toByteArray())
        }
    }
}
