package ch.qscqlmpa.magicclipboard.data.remote

import ch.qscqlmpa.magicclipboard.clipboard.McbItem
import ch.qscqlmpa.magicclipboard.clipboard.McbItemId
import kotlinx.coroutines.flow.Flow

interface Store {
    suspend fun addNewItem(item: McbItem)
    suspend fun updateItem(item: McbItem)
    suspend fun deleteItem(id: McbItemId)
    suspend fun clearStore()
    fun observeClipboardItems(): Flow<List<McbItem>>
}
