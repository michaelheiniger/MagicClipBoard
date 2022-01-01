package ch.qscqlmpa.magicclipboard.ui.magicclipboard.allitems

import androidx.lifecycle.viewModelScope
import ch.qscqlmpa.magicclipboard.auth.SessionManager
import ch.qscqlmpa.magicclipboard.clipboard.Items
import ch.qscqlmpa.magicclipboard.clipboard.MagicClipboardRepository
import ch.qscqlmpa.magicclipboard.clipboard.usecases.DeleteClipboardItemUsecase
import ch.qscqlmpa.magicclipboard.clipboard.usecases.DeviceClipboardUsecases
import ch.qscqlmpa.magicclipboard.clipboard.usecases.NewClipboardItemUsecase
import ch.qscqlmpa.magicclipboard.clipboard.usecases.ToggleFavoriteItemUsecase
import ch.qscqlmpa.magicclipboard.idlingresource.McbIdlingResource
import ch.qscqlmpa.magicclipboard.launch
import ch.qscqlmpa.magicclipboard.ui.ScreenNavigator
import ch.qscqlmpa.magicclipboard.ui.magicclipboard.ClipboardViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.update

class AllItemsClipboardViewModel(
    private val magicClipboardRepository: MagicClipboardRepository,
    deviceClipboardUsecases: DeviceClipboardUsecases,
    deleteClipboardItemUsecase: DeleteClipboardItemUsecase,
    toggleFavoriteItemUsecase: ToggleFavoriteItemUsecase,
    private val newClipboardItemUsecase: NewClipboardItemUsecase,
    private val idlingResource: McbIdlingResource,
    sessionManager: SessionManager,
    screenNavigator: ScreenNavigator,
    private val newClipboardValue: String?
) : ClipboardViewModel(
    deviceClipboardUsecases,
    deleteClipboardItemUsecase,
    toggleFavoriteItemUsecase,
    idlingResource,
    sessionManager,
    screenNavigator,
    newClipboardValue
) {
    fun onPasteValueToMcb(value: String) {
        if (value == newClipboardValue) viewModelState.update { it.copy(newClipboardValue = null) }
        viewModelScope.launch(
            beforeLaunch = { idlingResource.increment("Pasting to MagicClipboard") }
        ) {
            newClipboardItemUsecase.addNewItem(value)
        }
    }

    override fun observeItems(): Flow<Items> {
        return magicClipboardRepository.observeItems()
    }
}
