package ch.qscqlmpa.magicclipboard.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ch.qscqlmpa.magicclipboard.R

@Composable
fun InfoDialog(
    text: Int,
    content: @Composable () -> Unit,
    closeLabel: Int = R.string.close,
    onCloseClick: () -> Unit = {}
) {
    Dialog(
        onDismissRequest = onCloseClick,
        properties = DialogProperties(),
        content = {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colors.surface,
                contentColor = contentColorFor(MaterialTheme.colors.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = stringResource(text), color = MaterialTheme.colors.primary)
                    Spacer(Modifier.height(8.dp))
                    content()
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = onCloseClick,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(closeLabel))
                    }
                }
            }
        }
    )
}
