package alexman.chckm.core.data.repository

// TODO: document
interface Identifiable {
    val id: Int
    // this goes against existing naming conventions
    // however, this val functions as a companion object
    // constant, it should not be treated as a member,
    // and the different name indicates exactly that
    // (it's not possible to declare an abstract and
    // overridable companion object constant in an Interface)
    val id_not_set_constant: Int

    fun copySetId(newId: Int): Identifiable
}
