package ch.qscqlmpa.magicclipboard.clipboard.usecases

import ch.qscqlmpa.magicclipboard.clipboard.McbItem
import ch.qscqlmpa.magicclipboard.data.remote.Store

class ToggleFavoriteItemUsecase(
    private val store: Store
) {

    suspend fun toggleItemFavoriteItem(item: McbItem) {
        store.updateItem(item.copy(favorite = !item.favorite))
    }
}
