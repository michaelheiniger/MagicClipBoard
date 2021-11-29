package ch.qscqlmpa.magicclipboard.di

import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import ch.qscqlmpa.magicclipboard.BuildConfig
import ch.qscqlmpa.magicclipboard.clipboard.DeleteClipboardItemUsecase
import ch.qscqlmpa.magicclipboard.clipboard.DeviceClipboardUsecases
import ch.qscqlmpa.magicclipboard.clipboard.MagicClipboardRepository
import ch.qscqlmpa.magicclipboard.data.store.local.InMemoryLocalStore
import ch.qscqlmpa.magicclipboard.data.store.local.LocalStore
import ch.qscqlmpa.magicclipboard.debugClipBoardItems
import ch.qscqlmpa.magicclipboard.idlingresource.McbIdlingResource
import ch.qscqlmpa.magicclipboard.idlingresource.StubIdlingResource
import ch.qscqlmpa.magicclipboard.ui.magicclipboard.MagicClipboardViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val ioDispatcherName = named("io")

val appModule = module {
    single(qualifier = ioDispatcherName) { Dispatchers.IO }
    single { androidContext().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager }
}

val idlingResourceModule = module {
    single<McbIdlingResource> { StubIdlingResource() }
}

val localStoreModule = module {
    single { if (BuildConfig.DEBUG) debugClipBoardItems.toSet() else emptySet() }
    single<LocalStore> { InMemoryLocalStore(get(ioDispatcherName), get()) }
}

val repositoriesModule = module {
    single { MagicClipboardRepository(get()) }
}

val usecasesModule = module {
    single { DeviceClipboardUsecases(androidContext(), get(), get()) }
    single { DeleteClipboardItemUsecase(get()) }
}

val viewModelsModule = module {
    viewModel { MagicClipboardViewModel(get(), get(), get(), get()) }
}
