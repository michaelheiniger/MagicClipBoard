package ch.qscqlmpa.magicclipboard.data.store.local

import ch.qscqlmpa.magicclipboard.clipboard.McbItem
import ch.qscqlmpa.magicclipboard.clipboard.McbItemId
import ch.qscqlmpa.magicclipboard.data.Result
import ch.qscqlmpa.magicclipboard.data.ResultWithData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import org.tinylog.kotlin.Logger

class InMemoryLocalStore(
    private val ioDispatcher: CoroutineDispatcher,
    initialItems: Set<McbItem>
) : LocalStore {

    init {
        Logger.info { "Store loaded with ${initialItems.size} items" }
    }

    private val mutex = Mutex()
    private val itemsStateFlow = MutableStateFlow(initialItems.map { item -> item.id to item }.toMap())

    suspend fun initializeWith(items: Set<McbItem>) {
        withContext(ioDispatcher) {
            mutex.withLock { itemsStateFlow.value = items.map { item -> item.id to item }.toMap() }
        }
    }

    suspend fun clearAll() {
        withContext(ioDispatcher) {
            mutex.withLock { itemsStateFlow.value = emptyMap() }
        }
    }

    override suspend fun addItem(item: McbItem) {
        withContext(ioDispatcher) {
            mutex.withLock {
                val itemsSnapshot = itemsStateFlow.value.toMutableMap()
                itemsSnapshot[item.id] = item
                itemsStateFlow.value = itemsSnapshot
            }
        }
    }

    override suspend fun deleteItem(id: McbItemId): Result {
        return withContext(ioDispatcher) {
            mutex.withLock {
                val itemsSnapshot = itemsStateFlow.value.toMutableMap()
                val deletedItem = itemsSnapshot.remove(id)
                itemsStateFlow.value = itemsSnapshot
                if (deletedItem != null) Result.Success else Result.Error(IllegalArgumentException("Unable to find item"))
            }
        }
    }

    override suspend fun getItems(): ResultWithData<List<McbItem>> {
        return withContext(ioDispatcher) {
            mutex.withLock { ResultWithData.Success(itemsAsSortedList(itemsStateFlow.value)) }
        }
    }

    override fun observeItems(): Flow<List<McbItem>> = itemsStateFlow.asStateFlow().map(::itemsAsSortedList)

    private fun itemsAsSortedList(itemsMap: Map<McbItemId, McbItem>): List<McbItem> {
        return itemsMap.values.toList().sortedByDescending(McbItem::creationDate)
    }
}
