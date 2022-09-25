package ch.qscqlmpa.magicclipboard.clipboard.usecases

import ch.qscqlmpa.magicclipboard.clipboard.McbItemId
import ch.qscqlmpa.magicclipboard.data.remote.Store

class DeleteClipboardItemUsecase(
    private val store: Store
) {

    suspend fun deleteItem(id: McbItemId) {
        store.deleteItem(id)
    }
}
