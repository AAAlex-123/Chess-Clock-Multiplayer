package alexman.chckm.model

// TODO: document
data class Clock private constructor( // use new() and load() instead of constructor
    val profile: Profile,
    val timeControl: TimeControl,
    val timeLeftMillis: Long = timeControl.timeSeconds * 1000L,
    val lastSessionTimeMillis: Long = timeLeftMillis,
) {
    companion object {
        fun new(profile: Profile, timeControl: TimeControl): Clock =
            Clock(
                profile,
                timeControl,
                timeLeftMillis = timeControl.timeSeconds * 1000L,
                lastSessionTimeMillis = timeControl.timeSeconds * 1000L,
            )

        fun load(
            profile: Profile,
            timeControl: TimeControl,
            timeLeftMillis: Long,
            lastSessionTimeMillis: Long,
        ): Clock = Clock(profile, timeControl, timeLeftMillis, lastSessionTimeMillis)
    }
}
