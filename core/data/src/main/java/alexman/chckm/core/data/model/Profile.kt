package alexman.chckm.core.data.model

import alexman.chckm.core.data.repository.Identifiable
import androidx.compose.ui.graphics.Color

// TODO: document
data class Profile private constructor( // use new() and load() instead of constructor
    // should be ID_NOT_SET when creating new Profile
    override var id: Int = ID_NOT_SET,
    val name: String,
    val color: Color,
) : Identifiable, Displayable {

    override val id_not_set_constant: Int = ID_NOT_SET

    override val displayString: String = name

    override fun compareTo(other: Displayable): Int =
        if (other is Profile)
            compareBy<Profile> { it.displayString.lowercase() }.compare(this, other)
        else
            throw IllegalArgumentException("Cannot compare Profile to ${other::class.simpleName}")

    companion object {
        private const val ID_NOT_SET = -1

        val EMPTY: Profile = Profile(ID_NOT_SET, "", Color.White)

        // use this instead of constructor, it sets id automatically
        fun new(name: String, color: Color) =
            Profile(id = ID_NOT_SET, name, color)

        fun load(id: Int, name: String, color: Color) =
            Profile(id, name, color)

        // regex allows: letters, numbers, underscore, dash, space
        // cannot start with dash or space, must be at least 1 character
        val NAME_REGEX = Regex(pattern = "\\w[\\w\\- ]*")
        fun validateName(name: String) = NAME_REGEX.matches(name)
    }
}
