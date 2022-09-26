package ch.qscqlmpa.magicclipboard.ui.common

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.contentColorFor
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
    YesNoDialog(
        text = stringResource(text),
        onNoClick = onNoClick,
        onYesClick = onYesClick,
        onClose = onClose
    )
}

@Composable
fun YesNoDialog(
    text: String,
    onNoClick: () -> Unit = {},
    onYesClick: () -> Unit = {},
    onClose: () -> Unit = {},
) {
    YesNoDialog(
        content = { Text(text = text, color = MaterialTheme.colors.onSurface) },
        onNoClick,
        onYesClick,
        onClose
    )
}

@Composable
fun YesNoDialog(
    content: @Composable () -> Unit = {},
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
                    content()
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.primary,
                                contentColor = MaterialTheme.colors.onPrimary
                            ),
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
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = MaterialTheme.colors.primary,
                                contentColor = MaterialTheme.colors.onPrimary
                            ),
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
fun YesNoDialogPreview() {
    MagicClipBoardTheme {
        YesNoDialog(text = R.string.app_name)
    }
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
