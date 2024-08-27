package alexman.chckm.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChckmScaffold(
    titleText: String,
    onNavigateBack: (() -> Unit)?,
    content: @Composable () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { ChckmTextTM(text = titleText) },
                navigationIcon = {
                    onNavigateBack?.let {
                        PreviousIcon(
                            onClick = { it() },
                            modifier = Modifier.padding(8.dp),
                            sizeVariation = SizeVariation.SCAFFOLD,
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            content()
        }
    }
}
