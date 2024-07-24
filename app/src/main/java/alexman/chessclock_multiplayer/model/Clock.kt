package alexman.chessclock_multiplayer.model

// TODO: document
data class Clock private constructor( // use new() and load() instead of constructor
    val profile: Profile,
    val timeControl: TimeControl,
    val timeLeftMillis: Int = timeControl.timeSeconds * 1000,
    val lastSessionTimeMillis: Int = timeLeftMillis,
) {
    companion object {
        fun new(profile: Profile, timeControl: TimeControl): Clock =
            Clock(
                profile,
                timeControl,
                timeLeftMillis = timeControl.timeSeconds * 1000,
                lastSessionTimeMillis = timeControl.timeSeconds * 1000,
            )

        fun load(
            profile: Profile,
            timeControl: TimeControl,
            timeLeftMillis: Int,
            lastSessionTimeMillis: Int
        ): Clock = Clock(profile, timeControl, timeLeftMillis, lastSessionTimeMillis)
    }
}
