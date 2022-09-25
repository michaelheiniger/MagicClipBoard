package ch.qscqlmpa.magicclipboard.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@Composable
fun DefaultSnackbar(
    snackbarHostState: SnackbarHostState
) {
    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { data ->
            Snackbar(
                modifier = Modifier.padding(16.dp),
                action = {
                    data.actionLabel?.let { actionLabel ->
                        TextButton(
                            modifier = Modifier.testTag(UiTags.snackbarAction),
                            onClick = { snackbarHostState.currentSnackbarData?.performAction() }
                        ) {
                            Text(
                                text = actionLabel,
                                color = SnackbarDefaults.primaryActionColor
                            )
                        }
                    }
                }
            ) {
                Text(
                    modifier = Modifier.testTag(UiTags.snackbarText),
                    text = data.message
                )
            }
        },
    )
}
