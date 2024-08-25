package alexman.chckm.core.designsystem.component

import androidx.compose.runtime.staticCompositionLocalOf

enum class SizeVariation {
    PRIMARY, SECONDARY,
}

data class Sizes internal constructor(
    val sizeVariation: SizeVariation
) {
    companion object {
        // static vals which correspond to SizeVariation enum constants
        val Primary = Sizes(SizeVariation.PRIMARY)
        val Secondary = Sizes(SizeVariation.SECONDARY)
    }
}

val LocalSizes = staticCompositionLocalOf {
    Sizes(SizeVariation.SECONDARY)
}
