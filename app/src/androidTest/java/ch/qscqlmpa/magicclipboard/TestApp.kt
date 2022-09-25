package ch.qscqlmpa.magicclipboard

import ch.qscqlmpa.magicclipboard.di.appModule
import ch.qscqlmpa.magicclipboard.di.navigationModule
import ch.qscqlmpa.magicclipboard.di.repositoriesModule
import ch.qscqlmpa.magicclipboard.di.testSessionModule
import ch.qscqlmpa.magicclipboard.di.testStoreModule
import ch.qscqlmpa.magicclipboard.di.usecasesModule
import ch.qscqlmpa.magicclipboard.di.viewModelsModule
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
                testSessionModule,
                testStoreModule,
                usecasesModule,
                repositoriesModule,
                viewModelsModule,
                navigationModule
            )
        }
    }
}
