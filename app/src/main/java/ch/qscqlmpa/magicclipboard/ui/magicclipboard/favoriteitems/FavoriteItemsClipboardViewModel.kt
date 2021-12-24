package ch.qscqlmpa.magicclipboard.ui.magicclipboard.favoriteitems

import androidx.lifecycle.viewModelScope
import ch.qscqlmpa.magicclipboard.auth.SessionManager
import ch.qscqlmpa.magicclipboard.clipboard.MagicClipboardRepository
import ch.qscqlmpa.magicclipboard.clipboard.McbItem
import ch.qscqlmpa.magicclipboard.clipboard.usecases.DeleteClipboardItemUsecase
import ch.qscqlmpa.magicclipboard.clipboard.usecases.DeviceClipboardUsecases
import ch.qscqlmpa.magicclipboard.clipboard.usecases.NewClipboardItemUsecase
import ch.qscqlmpa.magicclipboard.clipboard.usecases.ToggleFavoriteItemUsecase
import ch.qscqlmpa.magicclipboard.idlingresource.McbIdlingResource
import ch.qscqlmpa.magicclipboard.launch
import ch.qscqlmpa.magicclipboard.ui.ScreenNavigator
import ch.qscqlmpa.magicclipboard.ui.magicclipboard.ClipboardViewModel
import kotlinx.coroutines.flow.Flow

class FavoriteItemsClipboardViewModel(
    private val magicClipboardRepository: MagicClipboardRepository,
    deviceClipboardUsecases: DeviceClipboardUsecases,
    deleteClipboardItemUsecase: DeleteClipboardItemUsecase,
    toggleFavoriteItemUsecase: ToggleFavoriteItemUsecase,
    private val newClipboardItemUsecase: NewClipboardItemUsecase,
    private val idlingResource: McbIdlingResource,
    sessionManager: SessionManager,
    screenNavigator: ScreenNavigator,
) : ClipboardViewModel(
    deviceClipboardUsecases,
    deleteClipboardItemUsecase,
    toggleFavoriteItemUsecase,
    idlingResource,
    sessionManager,
    screenNavigator,
    null
) {

    fun onPasteValueToMcb(value: String) {
        viewModelScope.launch(
            beforeLaunch = { idlingResource.increment("Pasting to MagicClipboard") }
        ) {
            newClipboardItemUsecase.addNewFavoriteItem(value)
        }
    }

    override fun observeItems(): Flow<List<McbItem>> {
        return magicClipboardRepository.observeFavoriteItems()
    }
}
