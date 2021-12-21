package ch.qscqlmpa.magicclipboard.di

import ch.qscqlmpa.magicclipboard.auth.SessionManager
import ch.qscqlmpa.magicclipboard.auth.SessionStateProvider
import ch.qscqlmpa.magicclipboard.data.remote.FirebaseStore
import ch.qscqlmpa.magicclipboard.data.remote.Store
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Logger
import org.koin.dsl.bind
import org.koin.dsl.module

val localSessionModule = module {
    single { SessionManager(useFirebaseEmulator = true) }
        .bind(SessionStateProvider::class)
}

val localStoreModule = module {
    single<Store> {
        val database = FirebaseDatabase.getInstance()
        // For info: https://firebase.google.com/docs/emulator-suite/install_and_configure
        database.useEmulator("192.168.0.173", 9000)
//        database.useEmulator("10.0.2.2", 9000)
        database.setPersistenceEnabled(true)
        database.setLogLevel(Logger.Level.DEBUG)
        FirebaseStore(get(ioDispatcherName), database, get())
    }
}
