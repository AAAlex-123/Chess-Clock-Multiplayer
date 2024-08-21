package alexman.chckm.core.data.model

abstract class Displayable : Comparable<Displayable> {

    abstract val displayString: String

    final override fun compareTo(other: Displayable): Int {
        if (this::class != other::class) {
            throw IllegalArgumentException(
                "Cannot compare ${this::class.simpleName} to ${other::class.simpleName}"
            )
        }

        return compareToImpl(other)
    }

    protected abstract fun compareToImpl(other: Displayable): Int
}
