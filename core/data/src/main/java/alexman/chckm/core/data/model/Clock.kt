package alexman.chckm.core.data.model

/**
 * Represents a Clock of a set of clocks in a game.
 *
 * It pairs together a [Profile] and a [TimeControl], and keeps track of the total
 * time passed for this player.
 *
 * @property[profile] the Profile object associated with this clock.
 * @property[timeControl] the TimeControl object associated with this clock.
 * @property[timeLeftMillis] the time left, in milliseconds, on this clock.
 * @property[lastSessionTimeMillis] the time left, in milliseconds, on this
 * clock when it was last used.
 *
 * @constructor private, use the [Clock.new] and [Clock.load] functions
 * instead.
 *
 * @author Alex Mandelias
 */
data class Clock private constructor(
    val profile: Profile,
    val timeControl: TimeControl,
    val timeLeftMillis: Long = timeControl.timeSeconds * 1000L,
    val lastSessionTimeMillis: Long = timeLeftMillis,
) {
    companion object {

        /** The default empty Clock object. */
        val EMPTY: Clock = Clock(
            profile = Profile.EMPTY,
            timeControl = TimeControl.EMPTY,
            timeLeftMillis = 0,
            lastSessionTimeMillis = 0,
        )

        /**
         * Creates a new Clock object with the given parameters.
         *
         * The [timeLeftMillis] and [lastSessionTimeMillis] parameters are set
         * to their defaults, which is the time control's time in seconds.
         *
         * @param[profile] the Profile associated with the new Clock
         * @param[timeControl] the TimeControl associated with the new Clock
         *
         * @return the new Clock object.
         */
        fun new(profile: Profile, timeControl: TimeControl): Clock =
            Clock(
                profile,
                timeControl,
                timeLeftMillis = timeControl.timeSeconds * 1000L,
                lastSessionTimeMillis = timeControl.timeSeconds * 1000L,
            )

        /**
         * Creates a new Clock object with the given parameters.
         *
         * These parameters are (supposed to be) known, since they are read from
         * some sort of persistent storage, therefore they are all known here.
         *
         * @param[profile] the value for [Clock.profile] read
         * @param[timeControl] the value for [Clock.timeControl] read
         * @param[timeLeftMillis] the value for [Clock.timeLeftMillis] read
         * @param[lastSessionTimeMillis] the value for
         * [Clock.lastSessionTimeMillis] read
         *
         * @return the Clock object as loaded from persistent storage.
         */
        fun load(
            profile: Profile,
            timeControl: TimeControl,
            timeLeftMillis: Long,
            lastSessionTimeMillis: Long,
        ): Clock = Clock(profile, timeControl, timeLeftMillis, lastSessionTimeMillis)
    }
}
