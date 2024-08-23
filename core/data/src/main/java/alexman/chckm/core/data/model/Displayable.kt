package alexman.chckm.core.data.model

/**
 * Defines the properties of an object which can be displayed as a list item.
 *
 * As such, it has a `displayString`, which summarizes the object's data in a
 * short string, and also the `compareTo()` member function, from implementing
 * the Comparable interface, which defines a total ordering among items of the
 * same Displayable subclass, so that they are displayed in a sorted order.
 *
 * @author Alex Mandelias
 */
abstract class Displayable : Comparable<Displayable> {

    /** A short summary of this object. */
    abstract val displayString: String

    /**
     * Checks if the `other` parameter is of the same class as `this` object,
     * and, if so, calls [compareToImpl] and returns its result.
     *
     * @param[other] The other Displayable to compare this one to
     *
     * @throws[IllegalArgumentException] if `this` and `other` are of different
     * class
     */
    final override fun compareTo(other: Displayable): Int {
        if (this::class != other::class) {
            throw IllegalArgumentException(
                "Cannot compare ${this::class.simpleName} to ${other::class.simpleName}"
            )
        }

        return compareToImpl(other)
    }

    /**
     * Implements the Comparable<Displayable> interface.
     *
     * The `other` parameter can safely be cast to the specific subclass of
     * Displayable in which this method is implemented.
     *
     * @param other The other Displayable to compare this one to
     */
    protected abstract fun compareToImpl(other: Displayable): Int
}
