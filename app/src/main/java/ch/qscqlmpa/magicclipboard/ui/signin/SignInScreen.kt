package ch.qscqlmpa.magicclipboard.ui.signin

import android.app.Activity.RESULT_OK
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.qscqlmpa.magicclipboard.R
import ch.qscqlmpa.magicclipboard.launch
import ch.qscqlmpa.magicclipboard.ui.common.InfoDialog
import ch.qscqlmpa.magicclipboard.ui.common.LoadingDialog
import ch.qscqlmpa.magicclipboard.ui.common.UiTags
import ch.qscqlmpa.magicclipboard.ui.magicclipboard.SignedOutTopBar
import ch.qscqlmpa.magicclipboard.ui.theme.MagicClipBoardTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import org.tinylog.kotlin.Logger

@Preview(showBackground = true)
@Composable
private fun SignInBodyPreview() {
    MagicClipBoardTheme {
        SignInBody(
            uiState = LoginUiState(
                isLoading = false,
            ),
            onToggleDarkTheme = {},
            onSignIn = {},
        )
    }
}

@Composable
fun SignInScreen(
    viewModel: SignInViewModel,
    onToggleDarkTheme: () -> Unit,
) {
    SignInBody(
        uiState = viewModel.uiState.collectAsState().value,
        onToggleDarkTheme = onToggleDarkTheme,
        onSignIn = viewModel::onSignIn,
    )
}

@Composable
private fun SignInBody(
    uiState: LoginUiState,
    onToggleDarkTheme: () -> Unit,
    onSignIn: (AuthCredential) -> Unit
) {
    Scaffold(topBar = { SignedOutTopBar(onToggleDarkTheme) }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.Top
        ) {
            val context = LocalContext.current
            if (uiState.isLoading) LoadingDialog(text = R.string.signingIn)
            else {
                var showSignInFailedDialog by remember { mutableStateOf(false) }
                val coroutineScope = rememberCoroutineScope()
                val signInLauncher =
                    rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                        Logger.debug { "Sign-in result: $result" }
                        coroutineScope.launch {
                            if (result.resultCode == RESULT_OK) {
                                try {
                                    val account = GoogleSignIn.getSignedInAccountFromIntent(result.data).await()
                                    Logger.debug { "Google sign-in success: $account" }
                                    val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)
                                    onSignIn(credential)
                                } catch (e: ApiException) {
                                    Logger.debug { "Google sign-in has failed" }
                                    showSignInFailedDialog = true
                                }
                            } else {
                                Logger.debug { "Google Sign-in has failed (user pressed back ?)" }
                                showSignInFailedDialog = true
                            }
                        }
                    }
                val token = stringResource(R.string.default_web_client_id)
                Button(
                    modifier = Modifier.testTag(UiTags.loginAsIdentifiedUser),
                    onClick = {
                        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestIdToken(token)
                            .build()
                        signInLauncher.launch(GoogleSignIn.getClient(context, gso).signInIntent)
                    }
                ) {
                    Text(text = stringResource(R.string.signIn))
                }
                if (showSignInFailedDialog) {
                    InfoDialog(
                        text = R.string.signInFailed,
                        onCloseClick = { showSignInFailedDialog = false }
                    )
                }
            }
        }
    }
}
