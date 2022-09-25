package ch.qscqlmpa.magicclipboard.clipboard

import ch.qscqlmpa.magicclipboard.data.remote.ConnectionStatus
import ch.qscqlmpa.magicclipboard.data.remote.Store
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class Items(
    val items: List<McbItem>
)

class MagicClipboardRepository(
    private val store: Store
) {
    fun observeItems(): Flow<Items> = store.observeClipboardItems().map { items -> Items(items) }

    fun observeFavoriteItems(): Flow<Items> = store.observeClipboardItems()
        .map { items -> items.filter(McbItem::favorite) }
        .map { items -> Items(items) }

    fun observeStoreConnectionStatus(): Flow<ConnectionStatus> = store.connectionStatus()
}
