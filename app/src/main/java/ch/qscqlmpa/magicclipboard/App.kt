package ch.qscqlmpa.magicclipboard

import android.app.Application
import ch.qscqlmpa.magicclipboard.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                appModule,
                localStoreModule,
                usecasesModule,
                repositoriesModule,
                viewModelsModule
            )
        }
    }
}
