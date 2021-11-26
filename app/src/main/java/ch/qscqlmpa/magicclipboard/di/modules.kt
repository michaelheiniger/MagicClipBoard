package ch.qscqlmpa.magicclipboard.di

import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import ch.qscqlmpa.magicclipboard.BuildConfig
import ch.qscqlmpa.magicclipboard.clipboard.ClipboardUsecases
import ch.qscqlmpa.magicclipboard.data.store.local.InMemoryLocalStore
import ch.qscqlmpa.magicclipboard.data.store.local.LocalStore
import ch.qscqlmpa.magicclipboard.debugClipBoardItems
import ch.qscqlmpa.magicclipboard.ui.magicclipboard.MagicClipboardViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val ioDispatcherName = named("io")

val appModule = module {
    single(qualifier = ioDispatcherName) { Dispatchers.IO }
    single { androidContext().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager }
}

val localStoreModule = module {
    single { if (BuildConfig.DEBUG) debugClipBoardItems.toSet() else emptySet() }
    single<LocalStore> { InMemoryLocalStore(get(ioDispatcherName), get()) }
}

val usecasesModule = module {
    single { ClipboardUsecases(androidContext(), get(), get()) }
}

val viewModelsModule = module {
    viewModel { MagicClipboardViewModel(get(), get()) }
}
