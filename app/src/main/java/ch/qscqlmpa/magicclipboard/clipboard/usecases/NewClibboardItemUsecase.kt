package ch.qscqlmpa.magicclipboard.clipboard.usecases

import ch.qscqlmpa.magicclipboard.clipboard.McbItem
import ch.qscqlmpa.magicclipboard.data.remote.Store

class NewClibboardItemUsecase(
    private val store: Store
) {

    suspend fun addNewItem(value: String) {
        store.addNewItem(McbItem(value = value))
    }
}
