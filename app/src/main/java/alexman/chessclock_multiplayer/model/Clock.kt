package alexman.chessclock_multiplayer.model

// TODO: document
data class Clock(
    val profile: Profile,
    var timeControl: TimeControl,
    var timeLeftMillis: Int = timeControl.timeSeconds * 1000,
    var lastSessionTimeMillis: Int = timeLeftMillis,
) {
    companion object {
        fun new(
            profile: Profile,
            timeControl: TimeControl,
            timeLeftMillis: Int,
            lastSessionTimeMillis: Int
        ): Clock = Clock(
            profile = profile,
            timeControl = timeControl,
            timeLeftMillis = timeLeftMillis,
            lastSessionTimeMillis = lastSessionTimeMillis,
        )

        fun new(
            profile: Profile,
            timeControl: TimeControl,
        ): Clock = Clock(
            profile = profile,
            timeControl = timeControl,
            timeLeftMillis = timeControl.timeSeconds * 1000,
            lastSessionTimeMillis = timeControl.timeSeconds * 1000,
        )
    }
}
