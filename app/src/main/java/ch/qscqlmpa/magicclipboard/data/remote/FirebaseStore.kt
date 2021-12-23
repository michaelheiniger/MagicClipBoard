package ch.qscqlmpa.magicclipboard.data.remote

import ch.qscqlmpa.magicclipboard.auth.SessionStateProvider
import ch.qscqlmpa.magicclipboard.clipboard.McbItem
import ch.qscqlmpa.magicclipboard.clipboard.McbItemId
import com.google.firebase.database.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.tinylog.kotlin.Logger

class FirebaseStore(
    private val ioDispatcher: CoroutineDispatcher,
    private val db: FirebaseDatabase,
    private val sessionStateProvider: SessionStateProvider
) : Store {

    override suspend fun addNewItem(item: McbItem) {
        withContext(ioDispatcher) {
            clipboardItemsReference.child(item.id.value.toString()).setValue(
                McbItemDto().apply {
                    id = item.id.value.toString()
                    value = item.value
                    creationDate = item.creationDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                }
            ).await()
        }
    }

    override suspend fun deleteItem(id: McbItemId) {
        withContext(ioDispatcher) {
            clipboardItemsReference.child(id.value.toString()).setValue(null).await()
        }
    }

    override suspend fun clearStore() {
        withContext(ioDispatcher) {
            clipboardItemsReference.setValue(null).await()
        }
    }

    override fun observeClipboardItems(): Flow<List<McbItem>> {
        val clipboardItemsTypeIndicator =
            object : GenericTypeIndicator<Map<String, McbItemDto>>() {} // ktlint-disable max-line-length
        return callbackFlow<List<McbItem>> {
            val callback = object : ValueEventListener { // Implementation of some callback interface
                override fun onDataChange(snapshot: DataSnapshot) {
                    Logger.debug { "Clipboard items changed: $snapshot" }
                    val itemsMap = snapshot.getValue(clipboardItemsTypeIndicator) ?: emptyMap()
                    trySend(itemsMap.values.map(McbItemDto::toMcbItem).sortedByDescending { i -> i.creationDate })
                        .onFailure { throwable ->
                            // Downstream has been cancelled or failed, can log here
                            val msg = "Error while observing clipboard items from remote database."
                            if (throwable == null) Logger.error { msg }
                            else Logger.error(throwable) { msg }
                        }
                }

                override fun onCancelled(error: DatabaseError) {
                    channel.close()
                }
            }
            clipboardItemsReference.addValueEventListener(callback)
            awaitClose { clipboardItemsReference.removeEventListener(callback) }
        }.buffer(Channel.CONFLATED)
    }

    private val clipboardItemsReference: DatabaseReference by lazy {
        val userId = sessionStateProvider.userId ?: throw IllegalStateException("User is unauthenticated")
        db.getReference("users/${userId.value}/clipboardItems")
    }
}

private class McbItemDto {
    var id: String? = null
    var value: String? = null
    var creationDate: String? = null

    fun toMcbItem(): McbItem = McbItem(
        id = McbItemId(UUID.fromString(id!!)),
        value = value!!,
        creationDate = LocalDateTime.parse(creationDate!!)
    )
}
