package ch.qscqlmpa.magicclipboard.ui.magicclipboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ch.qscqlmpa.magicclipboard.R
import ch.qscqlmpa.magicclipboard.ui.common.YesNoDialog
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

@Composable
fun SignedOutTopBar(
    onToggleDarkTheme: () -> Unit,
) {
    TopAppBar(
        title = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(R.string.app_name))
                DarkThemeToggle(onToggleDarkTheme)
            }
        }
    )
}

@Composable
fun SignedInTopBar(
    username: String,
    onToggleDarkTheme: () -> Unit,
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
                Row {
                    DarkThemeToggle(onToggleDarkTheme)
                    TextButton(onClick = { showSignOutDialog = true }) {
                        Text(
                            text = stringResource(R.string.signOut),
                            color = MaterialTheme.colors.onPrimary
                        )
                    }
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

@Composable
private fun DarkThemeToggle(onToggleDarkTheme: () -> Unit) {
    IconButton(onClick = onToggleDarkTheme) {
        Icon(
            painter = painterResource(R.drawable.ic_baseline_contrast_24),
            tint = MaterialTheme.colors.onPrimary,
            contentDescription = stringResource(R.string.toggle_dark_theme)
        )
    }
}
