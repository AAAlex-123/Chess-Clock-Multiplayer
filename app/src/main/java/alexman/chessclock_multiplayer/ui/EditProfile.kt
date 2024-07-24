package alexman.chessclock_multiplayer.ui

import alexman.chessclock_multiplayer.designsystem.component.ChckmButton
import alexman.chessclock_multiplayer.designsystem.component.ChckmDropdownMenu
import alexman.chessclock_multiplayer.designsystem.component.ChckmTextField
import alexman.chessclock_multiplayer.designsystem.component.DropdownType
import alexman.chessclock_multiplayer.designsystem.theme.ChckmTheme
import alexman.chessclock_multiplayer.model.Profile
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
private fun EditProfileScreenPreview() {
    ChckmTheme {
        EditProfileScreen(
            profile = Profile.new(
                name = "Alice",
                color = Color.Red,
            ),
            onSubmitProfile = { _ -> },
        )
    }
}

@Composable
fun EditProfileScreen(
    profile: Profile,
    onSubmitProfile: (Profile) -> Unit,
) {
    // copied from Color companion object
    val colorList = listOf(Color.Black, Color.DarkGray, Color.Gray, Color.LightGray, Color.White,
        Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Cyan, Color.Magenta)

    fun onSubmit(name: String, color: Color) {
        onSubmitProfile(
            profile.copy(
                name = name,
                color = color,
            )
        )
    }

    // regex allows: letters, numbers, dash, space
    val nameRegex = Regex(pattern = "^[\\w\\- ]+$")

    fun validateName(name: String): Boolean =
        nameRegex.matches(name)

    EditProfileScreenContent(
        initialName = profile.name,
        initialColor = profile.color,
        colorList = colorList,
        onSubmit = ::onSubmit,
        validateName = ::validateName,
    )
}

@Composable
private fun EditProfileScreenContent(
    initialName: String,
    initialColor: Color,
    colorList: List<Color>,
    onSubmit: (String, Color) -> Unit,
    validateName: (String) -> Boolean,
) {
    var name by remember { mutableStateOf(initialName) }
    var color by remember { mutableStateOf(initialColor) }

    var nameIsError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(64.dp),
        verticalArrangement = Arrangement.spacedBy(40.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        ChckmTextField(
            title = "Name",
            value = name,
            onValueChanged = {
                name = it
                nameIsError = !validateName(it)
            },
            placeholderText = "Alice",
            isError = nameIsError,
        )
        ChckmDropdownMenu(
            title = "Color",
            options = colorList,
            initialSelected = color,
            type = DropdownType.COLOR,
            onSelectedChanged = { color = it },
        )
        ChckmButton(
            text = "OK",
            onClick = {
                if (!nameIsError) {
                    onSubmit(name, color)
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
    }
}
