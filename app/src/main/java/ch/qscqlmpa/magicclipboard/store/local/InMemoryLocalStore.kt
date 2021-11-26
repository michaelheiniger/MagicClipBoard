package ch.qscqlmpa.magicclipboard.store.local

import ch.qscqlmpa.magicclipboard.clipboard.McbItem
import ch.qscqlmpa.magicclipboard.clipboard.McbItemId
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class InMemoryLocalStore(
    private val ioDispatcher: CoroutineDispatcher,
    initialItems: Set<McbItem>
) : LocalStore {

    private val mutex = Mutex()
    private val itemsStateFlow = MutableStateFlow(initialItems.map { item -> item.id to item }.toMap())

    override suspend fun addItem(item: McbItem) {
        withContext(ioDispatcher) {
            mutex.withLock {
                val itemsSnapshot = itemsStateFlow.value.toMutableMap()
                itemsSnapshot[item.id] = item
                itemsStateFlow.value = itemsSnapshot
            }
        }
    }

    override suspend fun deleteItem(id: McbItemId) {
        withContext(ioDispatcher) {
            mutex.withLock {
                val itemsSnapshot = itemsStateFlow.value.toMutableMap()
                itemsSnapshot.remove(id)
                itemsStateFlow.value = itemsSnapshot
            }
        }
    }

    override suspend fun getItems(): List<McbItem> {
        return withContext(ioDispatcher) {
            mutex.withLock { itemsAsSortedList(itemsStateFlow.value) }
        }
    }

    override fun observeItems(): Flow<List<McbItem>> = itemsStateFlow.asStateFlow().map(::itemsAsSortedList)

    private fun itemsAsSortedList(itemsMap: Map<McbItemId, McbItem>): List<McbItem> {
        return itemsMap.values.toList().sortedByDescending(McbItem::creationDate)
    }
}
