package ch.qscqlmpa.magicclipboard

import android.app.Application
import ch.qscqlmpa.magicclipboard.di.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

open class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Firebase.initialize(this)
        setupKoin()
    }

    open fun setupKoin() {
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                appModule,
                idlingResourceModule,
                prodSessionModule,
                prodStoreModule,
//                localSessionModule,
//                localStoreModule,
                usecasesModule,
                repositoriesModule,
                viewModelsModule,
                navigationModule
            )
        }
    }
}
