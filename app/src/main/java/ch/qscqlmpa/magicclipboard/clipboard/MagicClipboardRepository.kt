package ch.qscqlmpa.magicclipboard.clipboard

import ch.qscqlmpa.magicclipboard.data.remote.ConnectionStatus
import ch.qscqlmpa.magicclipboard.data.remote.Store
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class MagicClipboardRepository(
    private val store: Store
) {
    fun observeItems(): Flow<Items> {
        return store.observeClipboardItems()
            .combine(store.connectionStatus()) { items, status -> Items(items, status) }
    }

    fun observeFavoriteItems(): Flow<Items> {
        return store.observeClipboardItems().map { items -> items.filter(McbItem::favorite) }
            .combine(store.connectionStatus()) { items, status -> Items(items, status) }
    }
}

data class Items(
    val items: List<McbItem>,
    val connectionStatus: ConnectionStatus
)
