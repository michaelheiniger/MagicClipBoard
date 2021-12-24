package ch.qscqlmpa.magicclipboard.ui.magicclipboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import ch.qscqlmpa.magicclipboard.R
import ch.qscqlmpa.magicclipboard.ui.common.YesNoDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

@Composable
fun ClipboardTopBar(
    username: String,
    onSignOut: () -> Unit
) {
    TopAppBar(
        title = {
            var showSignOutDialog by remember { mutableStateOf(false) }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = username,
                    color = MaterialTheme.colors.onPrimary
                )
                TextButton(onClick = { showSignOutDialog = true }) {
                    Text(
                        text = stringResource(R.string.signOut),
                        color = MaterialTheme.colors.onPrimary
                    )
                }
            }
            if (showSignOutDialog) {
                val context = LocalContext.current
                YesNoDialog(
                    text = R.string.signOutConfirmation,
                    onYesClick = {
                        onSignOut()
                        val gso = GoogleSignInOptions.Builder().build()
                        GoogleSignIn.getClient(context, gso).signOut()
                    },
                    onClose = { showSignOutDialog = false }
                )
            }
        }
    )
}
