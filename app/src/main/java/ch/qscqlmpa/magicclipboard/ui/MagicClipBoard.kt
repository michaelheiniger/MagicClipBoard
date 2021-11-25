package ch.qscqlmpa.magicclipboard.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ch.qscqlmpa.magicclipboard.ui.theme.MagicClipBoardTheme

@Preview(showBackground = true)
@Composable
fun MagicClipBoardPreview() {
    MagicClipBoard {
        Text("Coucou")
    }
}

@Composable
fun MagicClipBoard(
    content: @Composable () -> Unit
) {
    MagicClipBoardTheme {
        Scaffold(
            topBar = { McbTopBar() },
        ) { innerPaddings ->
            Surface(
                modifier = Modifier.padding(innerPaddings).fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                content()
            }
        }
    }
}

@Composable
private fun McbTopBar() {
    TopAppBar(
        title = {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Magic Clipboard")
            }
        }
    )
}
