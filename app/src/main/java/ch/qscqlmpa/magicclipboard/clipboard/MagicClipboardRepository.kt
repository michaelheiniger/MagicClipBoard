package ch.qscqlmpa.magicclipboard.clipboard

import ch.qscqlmpa.magicclipboard.data.ResultWithData
import ch.qscqlmpa.magicclipboard.data.store.local.LocalStore
import kotlinx.coroutines.flow.Flow

class MagicClipboardRepository(
    private val localStore: LocalStore
) {
    suspend fun getItems(): ResultWithData<List<McbItem>> {
        return localStore.getItems()
    }

    fun observeItems(): Flow<List<McbItem>> {
        return localStore.observeItems()
    }
}
