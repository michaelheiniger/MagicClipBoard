package ch.qscqlmpa.magicclipboard

import android.app.Application
import ch.qscqlmpa.magicclipboard.di.appModule
import ch.qscqlmpa.magicclipboard.di.idlingResourceModule
import ch.qscqlmpa.magicclipboard.di.navigationModule
import ch.qscqlmpa.magicclipboard.di.prodSessionModule
import ch.qscqlmpa.magicclipboard.di.prodStoreModule
import ch.qscqlmpa.magicclipboard.di.repositoriesModule
import ch.qscqlmpa.magicclipboard.di.usecasesModule
import ch.qscqlmpa.magicclipboard.di.viewModelsModule
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
            // Using "androidLogger()" produces java.lang.IncompatibleClassChangeError: Found interface kotlin.time.TimeMark
            androidLogger(org.koin.core.logger.Level.ERROR) //
            androidContext(this@App)
            modules(
                appModule,
                idlingResourceModule,
                prodSessionModule,
                prodStoreModule,
                usecasesModule,
                repositoriesModule,
                viewModelsModule,
                navigationModule
            )
        }
    }
}
