package ch.qscqlmpa.magicclipboard.clipboard.usecases

import ch.qscqlmpa.magicclipboard.clipboard.McbItemId
import ch.qscqlmpa.magicclipboard.data.Result
import ch.qscqlmpa.magicclipboard.data.store.local.LocalStore

class DeleteClipboardItemUsecase(
    private val localStore: LocalStore
) {

    suspend fun deleteItem(id: McbItemId): Result {
        return localStore.deleteItem(id)
    }
}
