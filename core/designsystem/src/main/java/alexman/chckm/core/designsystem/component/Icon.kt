package alexman.chckm.core.designsystem.component

import alexman.chckm.core.designsystem.R
import alexman.chckm.core.designsystem.theme.ChckmTheme
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

@Composable
fun DeleteIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    sizeVariation: SizeVariation? = null,
    enabled: Boolean = true,
) {
    IconClickable(
        id = R.drawable.ic_trashcan,
        alt = "Delete",
        onClick = onClick,
        modifier = modifier,
        sizeVariation = sizeVariation,
        enabled = enabled,
    )
}

@Composable
fun EditIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    sizeVariation: SizeVariation? = null,
    enabled: Boolean = true,
) {
    IconClickable(
        id = R.drawable.ic_pencil,
        alt = "Edit",
        onClick = onClick,
        modifier = modifier,
        sizeVariation = sizeVariation,
        enabled = enabled,
    )
}

@Composable
fun ResetIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    sizeVariation: SizeVariation? = null,
    enabled: Boolean = true,
) {
    IconClickable(
        id = R.drawable.ic_restart,
        alt = "Reset time",
        onClick = onClick,
        modifier = modifier,
        sizeVariation = sizeVariation,
        enabled = enabled,
    )
}

@Composable
fun PreviousIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    sizeVariation: SizeVariation? = null,
    enabled: Boolean = true,
) {
    IconClickable(
        id = R.drawable.ic_arrow_left,
        alt = "Previous",
        onClick = onClick,
        modifier = modifier,
        sizeVariation = sizeVariation,
        enabled = enabled,
    )
}

@Composable
fun NextIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    sizeVariation: SizeVariation? = null,
    enabled: Boolean = true,
) {
    IconClickable(
        id = R.drawable.ic_arrow_right,
        alt = "Next",
        onClick = onClick,
        modifier = modifier,
        sizeVariation = sizeVariation,
        enabled = enabled,
    )
}

@Composable
fun PauseIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    sizeVariation: SizeVariation? = null,
    enabled: Boolean = true,
) {
    IconClickable(
        id = R.drawable.ic_pause,
        alt = "Pause",
        onClick = onClick,
        modifier = modifier,
        sizeVariation = sizeVariation,
        enabled = enabled,
    )
}

@Composable
fun ResumeIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    sizeVariation: SizeVariation? = null,
    enabled: Boolean = true,
) {
    IconClickable(
        id = R.drawable.ic_play,
        alt = "Resume",
        onClick = onClick,
        modifier = modifier,
        sizeVariation = sizeVariation,
        enabled = enabled,
    )
}

@Composable
fun FlagIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    sizeVariation: SizeVariation? = null,
    enabled: Boolean = true,
) {
    IconClickable(
        id = R.drawable.ic_flag,
        alt = "Flag",
        onClick = onClick,
        modifier = modifier,
        sizeVariation = sizeVariation,
        enabled = enabled,
    )
}

@Composable
fun IconClickable(
    @DrawableRes id: Int,
    alt: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    sizeVariation: SizeVariation? = null,
    enabled: Boolean = true,
) {
    Icon(
        id = id,
        alt = alt,
        modifier = modifier
            .clickable(
                enabled = enabled,
                onClick = onClick,
            ),
        sizeVariation = sizeVariation,
        enabled = enabled,
    )
}

@Composable
fun Icon(
    @DrawableRes id: Int,
    alt: String,
    modifier: Modifier = Modifier,
    sizeVariation: SizeVariation? = null,
    enabled: Boolean = true,
) {
    Image(
        painter = painterResource(id = id),
        contentDescription = alt,
        modifier = modifier
            .height(
                // parameter sizeVariation overrides CompositionLocal
                when (sizeVariation ?: LocalSizes.current.sizeVariation) {
                    SizeVariation.PRIMARY -> 64.dp
                    SizeVariation.SECONDARY -> 32.dp
                    SizeVariation.SCAFFOLD -> 56.dp
                }
            )
            .wrapContentWidth(),
        alpha = if (enabled) 1.0f else 0.5f,
    )
}
