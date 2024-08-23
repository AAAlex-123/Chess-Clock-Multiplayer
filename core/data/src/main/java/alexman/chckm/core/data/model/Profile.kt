package alexman.chckm.core.data.model

import alexman.chckm.core.data.repository.Identifiable
import androidx.compose.ui.graphics.Color

// TODO: document
data class Profile private constructor(
    // should be ID_NOT_SET when creating new Profile
    override var id: Int = ID_NOT_SET,
    val name: String,
    val color: Color,
) : Displayable(), Identifiable {

    override val id_not_set_constant: Int = ID_NOT_SET

    override val displayString: String = name

    // cache the comparator used in compareToImpl()
    private val comparator = compareBy<Profile> { it.displayString.lowercase() }

    override fun compareToImpl(other: Displayable): Int =
        comparator.compare(this, other as Profile)

    companion object {
        // required since member val cannot be used in companion object
        private const val ID_NOT_SET = -1

        val EMPTY: Profile = Profile(
            id = ID_NOT_SET,
            name = "",
            color = Color.White,
        )

        // use this instead of constructor, it sets id automatically
        fun new(name: String, color: Color) =
            Profile(ID_NOT_SET, name, color)

        fun load(id: Int, name: String, color: Color) =
            Profile(id, name, color)

        // regex allows: letters, numbers, underscore, dash, space
        // cannot start with dash or space, must be at least 1 character
        val NAME_REGEX = Regex(pattern = "\\w[\\w\\- ]*")
        fun validateName(name: String) = NAME_REGEX.matches(name)
    }
}
