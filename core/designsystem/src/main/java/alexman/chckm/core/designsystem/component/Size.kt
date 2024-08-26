package alexman.chckm.core.designsystem.component

import androidx.compose.runtime.staticCompositionLocalOf

enum class SizeVariation {
    PRIMARY, SECONDARY, SCAFFOLD,
}

data class Sizes internal constructor(
    val sizeVariation: SizeVariation
) {
    companion object {
        // static vals which correspond to SizeVariation enum constants
        val Primary = Sizes(SizeVariation.PRIMARY)
        val Secondary = Sizes(SizeVariation.SECONDARY)
        val Scaffold = Sizes(SizeVariation.SCAFFOLD)
    }
}

val LocalSizes = staticCompositionLocalOf {
    Sizes(SizeVariation.SECONDARY)
}
