package ch.qscqlmpa.magicclipboard.di

import ch.qscqlmpa.magicclipboard.BuildConfig
import ch.qscqlmpa.magicclipboard.auth.SessionManager
import ch.qscqlmpa.magicclipboard.auth.SessionStateProvider
import ch.qscqlmpa.magicclipboard.data.remote.FirebaseStore
import ch.qscqlmpa.magicclipboard.data.remote.Store
import com.google.firebase.database.Logger
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import org.koin.dsl.bind
import org.koin.dsl.module

val prodSessionModule = module {
    single { SessionManager() }.bind(SessionStateProvider::class)
}

val prodStoreModule = module {
    single<Store> {
        val database = Firebase.database("https://magicclipboard-7a33d-default-rtdb.europe-west1.firebasedatabase.app")
        database.setPersistenceEnabled(true) // Provide local caching of the Firebase DB
        if (BuildConfig.DEBUG) database.setLogLevel(Logger.Level.DEBUG)
        FirebaseStore(get(ioDispatcherName), database, get())
    }
}
