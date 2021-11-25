package ch.qscqlmpa.magicclipboard.di

import ch.qscqlmpa.magicclipboard.BuildConfig
import ch.qscqlmpa.magicclipboard.debugClipBoardItems
import ch.qscqlmpa.magicclipboard.store.local.InMemoryLocalStore
import ch.qscqlmpa.magicclipboard.store.local.LocalStore
import ch.qscqlmpa.magicclipboard.ui.magicclipboard.MagicClipboardVM
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val ioDispatcherName = named("io")

val appModule = module {
    single(qualifier = ioDispatcherName) { Dispatchers.IO }
}

val localStoreModule = module {
    single { if (BuildConfig.DEBUG) debugClipBoardItems.toSet() else emptySet() }
    single<LocalStore> { InMemoryLocalStore(get(ioDispatcherName), get()) }
}

val viewModelsModule = module {
    viewModel { MagicClipboardVM(get()) }
}
