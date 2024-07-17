package alexman.chessclock_multiplayer.model

import alexman.chessclock_multiplayer.repository.Identifiable
import androidx.compose.ui.graphics.Color

// TODO: document
data class Profile(
    // should be ID_NOT_SET when creating new Profile
    override var id: Int = ID_NOT_SET,
    val name: String,
    val color: Color,
) : Identifiable {

    override val id_not_set_constant: Int = ID_NOT_SET

    companion object {
        const val ID_NOT_SET = -1

        // use this instead of constructor, it sets id automatically
        fun new(name: String, color: Color)
            = Profile(ID_NOT_SET, name, color)
    }
}
