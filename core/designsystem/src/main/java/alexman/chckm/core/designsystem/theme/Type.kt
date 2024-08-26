package alexman.chckm.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

private val baseTextStyle = TextStyle(
    color = Color.Unspecified,
    fontSize = TextUnit.Unspecified,
    fontWeight = FontWeight.Normal,
    fontStyle = FontStyle.Normal,
    fontSynthesis = FontSynthesis.All,
    fontFamily = FontFamily.Default,
    letterSpacing = TextUnit.Unspecified,
    baselineShift = BaselineShift.None,
    localeList = LocaleList.current,
    background = Color.Unspecified,
    textDecoration = TextDecoration.None,
    shadow = Shadow.None,
    textAlign = TextAlign.Left,
    textDirection = TextDirection.Content,
    lineHeight = TextUnit.Unspecified,
    textIndent = TextIndent.None,
)

internal val ChckmTypography = Typography (
    // large player timer
    displayMedium = baseTextStyle.copy(
        fontSize = 96.sp,
    ),
    // scaffold title
    titleMedium = baseTextStyle.copy(
        fontSize = 40.sp,
    ),
    // buttons
    bodyLarge = baseTextStyle.copy(
        fontSize = 32.sp,
    ),
    // rest of text
    bodyMedium = baseTextStyle.copy(
        fontSize = 24.sp,
    ),
    // text field placeholder
    labelMedium = baseTextStyle.copy(
        fontSize = 20.sp,
    )
)
