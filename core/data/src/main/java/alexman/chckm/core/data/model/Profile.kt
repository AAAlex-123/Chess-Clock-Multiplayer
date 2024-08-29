package alexman.chckm.core.data.model

import alexman.chckm.core.data.repository.Identifiable
import androidx.compose.ui.graphics.Color

/**
 * Represents a Profile of a player.
 *
 * @property[id] the id of this profile; its value is handled by the repository
 * responsible for Profile objects.
 * @property[name] the name of this profile; used for display purposes.
 * @property[color] the color of this profile; used for display purposes.
 *
 * @constructor private, use the [Profile.new] and [Profile.load] functions
 * instead.
 *
 * @author Alex Mandelias
 */
data class Profile private constructor(
    // should be ID_NOT_SET when creating new Profile
    override var id: Int = ID_NOT_SET,
    val name: String,
    val color: Color,
) : Displayable(), Identifiable {

    override val id_not_set_constant: Int = ID_NOT_SET

    override fun copySetId(newId: Int): Profile = copy(id = newId)

    override val displayString: String = name

    // cache the comparator used in compareToImpl()
    private val comparator = compareBy<Profile> { it.displayString.lowercase() }

    override fun compareToImpl(other: Displayable): Int =
        comparator.compare(this, other as Profile)

    companion object {
        // required since member val cannot be used in companion object
        private const val ID_NOT_SET = -1

        /** The default empty Profile object. */
        val EMPTY: Profile = Profile(
            id = ID_NOT_SET,
            name = "",
            color = Color.White,
        )

        /**
         * Creates a new Profile object with the given parameters.
         *
         * The [id] parameter is set to [id_not_set_constant], and the
         * persistent storage will then set it to an appropriate value.
         *
         * @param[name] the name of the new Profile
         * @param[color] the color of the new Profile
         *
         * @return the new Profile object.
         */
        fun new(name: String, color: Color) =
            Profile(ID_NOT_SET, name, color)

        /**
         * Creates a new Profile object with the given parameters.
         *
         * These parameters are (supposed to be) known, since they are read from
         * some sort of persistent storage, therefore they are all known here.
         *
         * @param[id] the value for [Profile.id] read
         * @param[name] the value for [Profile.name] read
         * @param[color] the value for [Profile.color] read
         *
         * @return the Profile object as loaded from persistent storage.
         */
        fun load(id: Int, name: String, color: Color) =
            Profile(id, name, color)

        // regex allows: letters, numbers, underscore, dash, space
        // cannot start with dash or space, must be at least 1 character
        /** The regex used to validate a Profile name. */
        val NAME_REGEX = Regex(pattern = "\\w[\\w\\- ]*")

        /**
         * Checks if the given name is valid according to the [NAME_REGEX].
         *
         * @param[name] the name to check
         *
         * @return `true` if the name is valid, `false` otherwise.
         */
        fun validateName(name: String) = NAME_REGEX.matches(name)
    }
}
