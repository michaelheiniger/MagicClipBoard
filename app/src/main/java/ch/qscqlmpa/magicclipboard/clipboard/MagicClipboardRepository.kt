package ch.qscqlmpa.magicclipboard.clipboard

import ch.qscqlmpa.magicclipboard.data.remote.Store
import kotlinx.coroutines.flow.Flow

class MagicClipboardRepository(
    private val store: Store
) {
    fun observeItems(): Flow<List<McbItem>> {
        return store.observeClipboardItems()
    }
}
