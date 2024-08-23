package alexman.chckm.core.data.model

// TODO: document
data class Clock private constructor(
    val profile: Profile,
    val timeControl: TimeControl,
    val timeLeftMillis: Long = timeControl.timeSeconds * 1000L,
    val lastSessionTimeMillis: Long = timeLeftMillis,
) {
    companion object {

        val EMPTY: Clock = Clock(
            profile = Profile.EMPTY,
            timeControl = TimeControl.EMPTY,
            timeLeftMillis = 0,
            lastSessionTimeMillis = 0,
        )

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
