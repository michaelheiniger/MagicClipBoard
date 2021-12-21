package ch.qscqlmpa.magicclipboard

import ch.qscqlmpa.magicclipboard.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class TestApp : App() {

    override fun setupKoin() {
        startKoin {
            androidLogger()
            androidContext(this@TestApp)
            modules(
                appModule,
                testIdlingResourceModule,
                localSessionModule,
                localStoreModule,
                usecasesModule,
                repositoriesModule,
                viewModelsModule,
                navigationModule
            )
        }
    }
}
