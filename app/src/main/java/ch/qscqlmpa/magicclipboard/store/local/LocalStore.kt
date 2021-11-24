package ch.qscqlmpa.magicclipboard.store.local

import ch.qscqlmpa.magicclipboard.clipboard.McbItem
import ch.qscqlmpa.magicclipboard.clipboard.McbItemId
import kotlinx.coroutines.flow.Flow

interface LocalStore {
    suspend fun addItem(item: McbItem)
    suspend fun deleteItem(id: McbItemId)
    suspend fun getItems(): List<McbItem>
    fun observeItems(): Flow<McbItem>
}
