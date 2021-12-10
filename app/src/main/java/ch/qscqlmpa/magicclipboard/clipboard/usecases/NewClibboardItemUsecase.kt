package ch.qscqlmpa.magicclipboard.clipboard.usecases

import ch.qscqlmpa.magicclipboard.clipboard.McbItem
import ch.qscqlmpa.magicclipboard.data.store.local.LocalStore

class NewClibboardItemUsecase(
    private val localStore: LocalStore
) {

    suspend fun pasteValueToMcb(value: String) {
        localStore.addItem(
            McbItem(
                label = "label", // TODO: is "label" really useful ? 
                value = value
            )
        )
    }
}
