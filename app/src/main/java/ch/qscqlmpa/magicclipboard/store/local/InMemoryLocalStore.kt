package ch.qscqlmpa.magicclipboard.store.local

import ch.qscqlmpa.magicclipboard.clipboard.McbItem
import ch.qscqlmpa.magicclipboard.clipboard.McbItemId
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap

class InMemoryLocalStore(private val ioDispatcher: CoroutineDispatcher) : LocalStore {

    // FIXME: Is that a "correct" data-structure to use here ?
    private val items: ConcurrentHashMap<McbItemId, McbItem> = ConcurrentHashMap(mutableMapOf())

    override suspend fun addItem(item: McbItem) {
        withContext(ioDispatcher) {
            items[item.id] = item
        }
    }

    override suspend fun deleteItem(id: McbItemId) {
        withContext(ioDispatcher) {
            items.remove(id)
        }
    }

    override suspend fun getItems(): List<McbItem> {
        return withContext(ioDispatcher) {
            items.values.toList()
        }
    }

    override fun observeItems(): Flow<McbItem> {
        TODO("Not yet implemented")
    }
}
