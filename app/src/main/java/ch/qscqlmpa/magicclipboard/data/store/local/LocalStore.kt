package ch.qscqlmpa.magicclipboard.data.store.local

import ch.qscqlmpa.magicclipboard.clipboard.McbItem
import ch.qscqlmpa.magicclipboard.clipboard.McbItemId
import ch.qscqlmpa.magicclipboard.data.Result
import ch.qscqlmpa.magicclipboard.data.ResultWithData
import kotlinx.coroutines.flow.Flow

interface LocalStore {
    suspend fun addItem(item: McbItem)
    suspend fun deleteItem(id: McbItemId): Result
    suspend fun getItems(): ResultWithData<List<McbItem>>
    fun observeItems(): Flow<List<McbItem>>
}
