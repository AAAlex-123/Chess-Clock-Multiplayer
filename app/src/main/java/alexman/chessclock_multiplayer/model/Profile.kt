package alexman.chessclock_multiplayer.model

import alexman.chessclock_multiplayer.repository.Identifiable
import androidx.compose.ui.graphics.Color

// TODO: document
data class Profile private constructor( // use new() and load() instead of constructor
    // should be ID_NOT_SET when creating new Profile
    override var id: Int = ID_NOT_SET,
    val name: String,
    val color: Color,
) : Identifiable {

    override val id_not_set_constant: Int = ID_NOT_SET

    companion object {
        const val ID_NOT_SET = -1

        // use this instead of constructor, it sets id automatically
        fun new(name: String, color: Color) =
            Profile(id = ID_NOT_SET, name, color)

        fun load(id: Int, name: String, color: Color) =
            Profile(id, name, color)
    }
}
