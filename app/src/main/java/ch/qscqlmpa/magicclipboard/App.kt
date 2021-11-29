package ch.qscqlmpa.magicclipboard

import android.app.Application
import ch.qscqlmpa.magicclipboard.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

open class App : Application() {

    override fun onCreate() {
        super.onCreate()
        setupKoin()
    }

    open fun setupKoin() {
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                appModule,
                idlingResourceModule,
                localStoreModule,
                usecasesModule,
                repositoriesModule,
                viewModelsModule
            )
        }
    }
}
