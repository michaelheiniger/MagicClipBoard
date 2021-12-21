package ch.qscqlmpa.magicclipboard.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ch.qscqlmpa.magicclipboard.R
import ch.qscqlmpa.magicclipboard.ui.theme.MagicClipBoardTheme

@Composable
fun InfoDialog(
    text: Int,
    closeLabel: Int = R.string.close,
    onCloseClick: () -> Unit = {},
    content: @Composable () -> Unit = {}
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

@Composable
fun YesNoDialog(
    text: Int,
    onNoClick: () -> Unit = {},
    onYesClick: () -> Unit = {},
    onClose: () -> Unit = {},
) {
    Dialog(
        onDismissRequest = onClose,
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = {
                                onNoClick()
                                onClose()
                            },
                            modifier = Modifier
                                .testTag(UiTags.dialogNoBtn)
                        ) {
                            Text(stringResource(R.string.no))
                        }
                        Spacer(Modifier.width(8.dp))
                        TextButton(
                            onClick = {
                                onYesClick()
                                onClose()
                            },
                            modifier = Modifier.testTag(UiTags.dialogYesBtn)
                        ) {
                            Text(stringResource(R.string.yes))
                        }
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun LoadingDialogPreview() {
    MagicClipBoardTheme {
        LoadingDialog(text = R.string.app_name)
    }
}

@Composable
fun LoadingDialog(
    @StringRes text: Int
) {
    Dialog(
        onDismissRequest = {}, // User cannot dismiss loading dialog
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
                    LoadingSpinner()
                }
            }
        }
    )
}
