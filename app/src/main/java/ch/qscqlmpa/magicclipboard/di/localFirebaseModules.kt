package ch.qscqlmpa.magicclipboard.di

import ch.qscqlmpa.magicclipboard.auth.SessionManager
import ch.qscqlmpa.magicclipboard.auth.SessionStateProvider
import ch.qscqlmpa.magicclipboard.data.remote.FirebaseStore
import ch.qscqlmpa.magicclipboard.data.remote.Store
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Logger
import org.koin.dsl.bind
import org.koin.dsl.module

// 10.0.2.2 is special IP address allowing to reach Android emulator
private const val EMULATOR_HOST = "10.0.2.2"

val testSessionModule = module {
    single {
        val auth = FirebaseAuth.getInstance()
        auth.useEmulator(EMULATOR_HOST, 9099)
        auth.signOut()
        SessionManager(auth)
    }.bind(SessionStateProvider::class)
}

val testStoreModule = module {
    single<Store> {
        val database = FirebaseDatabase.getInstance()
        // For info: https://firebase.google.com/docs/emulator-suite/install_and_configure
        database.useEmulator(EMULATOR_HOST, 9000)
//        database.setPersistenceEnabled(true) // Would make E2E tests fail when launching several times
        database.setLogLevel(Logger.Level.DEBUG)
        FirebaseStore(get(ioDispatcherName), database, get())
    }
}
