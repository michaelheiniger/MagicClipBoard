package ch.qscqlmpa.magicclipboard.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import org.tinylog.Logger

sealed interface SessionState {
    object Unauthenticated : SessionState
    sealed interface SignedIn : SessionState {
        object Anonymous : SignedIn
        data class Authenticated(val username: String) : SignedIn
    }
}

@JvmInline
value class UserId(val value: String)

class SessionManager : SessionStateProvider {

    override val userId: UserId?
        get() {
            val uid = auth.uid
            return if (uid != null) UserId(uid) else null
        }

    fun sessionState(): Flow<SessionState> {
        return callbackFlow {
            val callback = FirebaseAuth.AuthStateListener { newAuthState ->
                Logger.info {
                    """
                        Auth state updated:
                         - user:
                           ${newAuthState.currentUser?.getInfo()}
                         - session state: ${buildSessionState(newAuthState)}
                    """
                }
                trySend(buildSessionState(newAuthState))
            }
            auth.addAuthStateListener(callback)
            awaitClose { auth.removeAuthStateListener(callback) }
        }.buffer(Channel.CONFLATED)
    }

    fun signOut() {
        auth.signOut()
    }

    private val auth: FirebaseAuth
        get() = FirebaseAuth.getInstance()

    private fun buildSessionState(auth: FirebaseAuth): SessionState {
        val user = auth.currentUser ?: return SessionState.Unauthenticated
        return when (user.isAnonymous) {
            true -> SessionState.SignedIn.Anonymous
            false -> SessionState.SignedIn.Authenticated(username = user.displayName ?: "")
        }
    }

    private fun FirebaseUser.getInfo() =
        """
            - uid: ${this.uid}
            - displayName: ${this.displayName}
        """.trimIndent()
}
