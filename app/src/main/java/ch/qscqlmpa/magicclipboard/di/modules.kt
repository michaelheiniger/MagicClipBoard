package ch.qscqlmpa.magicclipboard.di

import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import ch.qscqlmpa.magicclipboard.clipboard.MagicClipboardRepository
import ch.qscqlmpa.magicclipboard.clipboard.usecases.DeleteClipboardItemUsecase
import ch.qscqlmpa.magicclipboard.clipboard.usecases.DeviceClipboardUsecases
import ch.qscqlmpa.magicclipboard.clipboard.usecases.NewClipboardItemUsecase
import ch.qscqlmpa.magicclipboard.clipboard.usecases.ToggleFavoriteItemUsecase
import ch.qscqlmpa.magicclipboard.idlingresource.McbIdlingResource
import ch.qscqlmpa.magicclipboard.idlingresource.StubIdlingResource
import ch.qscqlmpa.magicclipboard.ui.ScreenNavigator
import ch.qscqlmpa.magicclipboard.ui.magicclipboard.allitems.AllItemsClipboardViewModel
import ch.qscqlmpa.magicclipboard.ui.magicclipboard.favoriteitems.FavoriteItemsClipboardViewModel
import ch.qscqlmpa.magicclipboard.ui.signin.SignInViewModel
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

val repositoriesModule = module {
    single { MagicClipboardRepository(get()) }
}

val usecasesModule = module {
    single { DeviceClipboardUsecases(androidContext(), get()) }
    single { DeleteClipboardItemUsecase(get()) }
    single { NewClipboardItemUsecase(get()) }
    single { ToggleFavoriteItemUsecase(get()) }
}

val navigationModule = module {
    single { ScreenNavigator() }
}

val viewModelsModule = module {
    viewModel { SignInViewModel(get(), get()) }
    viewModel { parameters ->
        AllItemsClipboardViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            parameters.getOrNull()
        )
    }
    viewModel { FavoriteItemsClipboardViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
}
