package alexman.chckm.ui.screen

import alexman.chckm.core.designsystem.component.ChckmButton
import alexman.chckm.core.designsystem.component.ChckmDropdownMenu
import alexman.chckm.core.designsystem.component.ChckmTextField
import alexman.chckm.core.designsystem.component.DropdownType
import alexman.chckm.core.designsystem.theme.ChckmTheme
import alexman.chckm.core.data.model.Profile
import alexman.chckm.core.designsystem.component.ChckmScaffold
import androidx.activity.compose.BackHandler
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
            onNavigateBack = { },
        )
    }
}

@Composable
fun EditProfileScreen(
    profile: Profile,
    onSubmitProfile: (Profile) -> Unit,
    onNavigateBack: () -> Unit,
) {
    BackHandler { onNavigateBack() }

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

    val validateName = Profile.Companion::validateName

    EditProfileScreenContent(
        initialName = profile.name,
        initialColor = profile.color,
        colorList = colorList,
        onSubmit = ::onSubmit,
        onNavigateBack = onNavigateBack,
        validateName = validateName,
    )
}

@Composable
private fun EditProfileScreenContent(
    initialName: String,
    initialColor: Color,
    colorList: List<Color>,
    onSubmit: (String, Color) -> Unit,
    onNavigateBack: () -> Unit,
    validateName: (String) -> Boolean,
) {
    var name by remember { mutableStateOf(initialName) }
    var color by remember { mutableStateOf(initialColor) }

    var nameIsError by remember { mutableStateOf(!validateName(initialName)) }

    ChckmScaffold(
        titleText = "Edit Profile",
        onNavigateBack = onNavigateBack,
    ) {
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
                placeholderText = "Your Name",
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
}
