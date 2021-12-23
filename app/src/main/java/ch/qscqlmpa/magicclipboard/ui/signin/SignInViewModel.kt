package ch.qscqlmpa.magicclipboard.ui.signin

import androidx.lifecycle.viewModelScope
import ch.qscqlmpa.magicclipboard.auth.SessionManager
import ch.qscqlmpa.magicclipboard.auth.SessionState
import ch.qscqlmpa.magicclipboard.ui.Destination
import ch.qscqlmpa.magicclipboard.ui.ScreenNavigator
import ch.qscqlmpa.magicclipboard.ui.navOptionsPopUpToInclusive
import ch.qscqlmpa.magicclipboard.viewmodel.BaseViewModel
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class SignInViewModel(
    private val sessionManager: SessionManager,
    private val screenNavigator: ScreenNavigator,
) : BaseViewModel() {

    private val viewModelState = MutableStateFlow(LoginViewModelState(isLoading = true))

    private lateinit var observeLoginJob: Job

    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    fun onSignIn(credential: AuthCredential) {
        viewModelScope.launch {
            Firebase.auth.signInWithCredential(credential).await()
        }
    }

    override fun onStart() {
        super.onStart()
        viewModelState.update { it.copy(isLoading = true) }
        observeLoginJob = viewModelScope.launch {
            sessionManager.sessionState().collect { sessionState ->
                when (sessionState) {
                    is SessionState.SignedIn -> {
                        screenNavigator.navigate(
                            destination = Destination.MagicClipboard,
                            navOptions = navOptionsPopUpToInclusive(Destination.SignIn.routeName)
                        )
                    }
                    SessionState.Unauthenticated -> {
                        viewModelState.update { it.copy(isLoading = false) }
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        observeLoginJob.cancel()
    }
}

data class LoginUiState(
    val isLoading: Boolean,
)

private data class LoginViewModelState(
    val isLoading: Boolean,
) {
    fun toUiState(): LoginUiState = LoginUiState(isLoading = isLoading)
}
