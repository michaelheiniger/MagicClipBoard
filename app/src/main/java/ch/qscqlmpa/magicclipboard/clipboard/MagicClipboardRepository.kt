package ch.qscqlmpa.magicclipboard.clipboard

import ch.qscqlmpa.magicclipboard.data.remote.Store
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MagicClipboardRepository(
    private val store: Store
) {
    fun observeItems(): Flow<List<McbItem>> {
        return store.observeClipboardItems()
    }

    fun observeFavoriteItems(): Flow<List<McbItem>> {
        return store.observeClipboardItems().map { items -> items.filter(McbItem::favorite) }
    }
}
