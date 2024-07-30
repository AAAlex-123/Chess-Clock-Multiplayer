package alexman.chessclock_multiplayer.designsystem.component

import alexman.chessclock_multiplayer.R
import alexman.chessclock_multiplayer.designsystem.theme.ChckmTheme
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
private fun IconPreview() {
    ChckmTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(40.dp)
        ) {
            Icon(
                R.drawable.ic_placeholder,
                alt = "placeholder",
                sizeVariation = SizeVariation.PRIMARY,
            )
            Icon(
                R.drawable.ic_placeholder,
                alt = "placeholder",
                sizeVariation = SizeVariation.SECONDARY,
            )
        }
    }
}

enum class SizeVariation {
    PRIMARY, SECONDARY,
}

@Composable
fun DeleteIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    sizeVariation: SizeVariation = SizeVariation.SECONDARY,
) {
    IconClickable(
        id = R.drawable.ic_trashcan,
        alt = "Delete",
        onClick = onClick,
        modifier = modifier,
        sizeVariation = sizeVariation,
    )
}

@Composable
fun EditIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    sizeVariation: SizeVariation = SizeVariation.SECONDARY,
) {
    IconClickable(
        id = R.drawable.ic_pencil,
        alt = "Edit",
        onClick = onClick,
        modifier = modifier,
        sizeVariation = sizeVariation,
    )
}

@Composable
fun ResetIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    sizeVariation: SizeVariation = SizeVariation.PRIMARY,
) {
    IconClickable(
        id = R.drawable.ic_restart,
        alt = "Reset time",
        onClick = onClick,
        modifier = modifier,
        sizeVariation = sizeVariation,
    )
}

@Composable
fun PreviousIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    sizeVariation: SizeVariation = SizeVariation.PRIMARY,
) {
    IconClickable(
        id = R.drawable.ic_arrow_left,
        alt = "Previous",
        onClick = onClick,
        modifier = modifier,
        sizeVariation = sizeVariation,
    )
}

@Composable
fun NextIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    sizeVariation: SizeVariation = SizeVariation.PRIMARY,
) {
    IconClickable(
        id = R.drawable.ic_arrow_right,
        alt = "Next",
        onClick = onClick,
        modifier = modifier,
        sizeVariation = sizeVariation,
    )
}

@Composable
fun PauseIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    sizeVariation: SizeVariation = SizeVariation.PRIMARY,
) {
    IconClickable(
        id = R.drawable.ic_pause,
        alt = "Pause",
        onClick = onClick,
        modifier = modifier,
        sizeVariation = sizeVariation,
    )
}

@Composable
fun ResumeIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    sizeVariation: SizeVariation = SizeVariation.PRIMARY,
) {
    IconClickable(
        id = R.drawable.ic_play,
        alt = "Resume",
        onClick = onClick,
        modifier = modifier,
        sizeVariation = sizeVariation,
    )
}

@Composable
fun FlagIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    sizeVariation: SizeVariation = SizeVariation.PRIMARY,
) {
    IconClickable(
        id = R.drawable.ic_flag,
        alt = "Flag",
        onClick = onClick,
        modifier = modifier,
        sizeVariation = sizeVariation,
    )
}

@Composable
fun IconClickable(
    @DrawableRes id: Int,
    alt: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    sizeVariation: SizeVariation = SizeVariation.SECONDARY,
) {
    Icon(
        id = id,
        alt = alt,
        modifier = modifier
            .clickable(onClick = onClick),
        sizeVariation = sizeVariation,
    )
}

@Composable
fun Icon(
    @DrawableRes id: Int,
    alt: String,
    modifier: Modifier = Modifier,
    sizeVariation: SizeVariation = SizeVariation.SECONDARY,
) {
    Image(
        painter = painterResource(id = id),
        contentDescription = alt,
        modifier = modifier
            .height(
                when (sizeVariation) {
                    SizeVariation.PRIMARY -> 40.dp
                    SizeVariation.SECONDARY -> 32.dp
                }
            )
            .wrapContentWidth(),
    )
}
