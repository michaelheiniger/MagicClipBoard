package ch.qscqlmpa.magicclipboard.clipboard.usecases

import ch.qscqlmpa.magicclipboard.clipboard.McbItem
import ch.qscqlmpa.magicclipboard.data.remote.Store

class NewClipboardItemUsecase(
    private val store: Store
) {

    suspend fun addNewItem(value: String) {
        store.addNewItem(McbItem(value = value, favorite = false))
    }

    suspend fun addNewFavoriteItem(value: String) {
        store.addNewItem(McbItem(value = value, favorite = true))
    }
}
