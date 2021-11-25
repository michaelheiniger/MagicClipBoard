package ch.qscqlmpa.magicclipboard.store.local

import ch.qscqlmpa.magicclipboard.clipboard.McbItem
import ch.qscqlmpa.magicclipboard.clipboard.McbItemId
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class InMemoryLocalStore(
    private val ioDispatcher: CoroutineDispatcher,
    initialItems: Set<McbItem>
) : LocalStore {

    private val mutex = Mutex()
    private val items: MutableMap<McbItemId, McbItem> = initialItems.map { item -> item.id to item }.toMap().toMutableMap()
    private val itemsFlow = MutableSharedFlow<List<McbItem>>(replay = 1)

    private val itemsSortedOnCreationDateDesc: List<McbItem>
        get() =
            items.values.toList().sortedByDescending(McbItem::creationDate)

    init {
        itemsFlow.tryEmit(itemsSortedOnCreationDateDesc)
    }

    override suspend fun addItem(item: McbItem) {
        withContext(ioDispatcher) {
            mutex.withLock {
                items[item.id] = item
                itemsFlow.emit(itemsSortedOnCreationDateDesc)
            }
        }
    }

    override suspend fun deleteItem(id: McbItemId) {
        withContext(ioDispatcher) {
            mutex.withLock {
                items.remove(id)
                itemsFlow.emit(itemsSortedOnCreationDateDesc)
            }
        }
    }

    override suspend fun getItems(): List<McbItem> {
        return withContext(ioDispatcher) {
            mutex.withLock { itemsSortedOnCreationDateDesc }
        }
    }

    override fun observeItems(): Flow<List<McbItem>> = itemsFlow.asSharedFlow()
}
