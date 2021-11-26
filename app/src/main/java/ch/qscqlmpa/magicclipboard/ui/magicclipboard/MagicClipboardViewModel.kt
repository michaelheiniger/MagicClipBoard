package ch.qscqlmpa.magicclipboard.ui.magicclipboard

import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import ch.qscqlmpa.magicclipboard.R
import ch.qscqlmpa.magicclipboard.clipboard.ClipboardUsecases
import ch.qscqlmpa.magicclipboard.clipboard.McbItem
import ch.qscqlmpa.magicclipboard.clipboard.McbItemId
import ch.qscqlmpa.magicclipboard.data.Result
import ch.qscqlmpa.magicclipboard.data.store.local.LocalStore
import ch.qscqlmpa.magicclipboard.viewmodel.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

sealed interface MagicClipboardUiState {
    val isLoading: Boolean
    val messages: List<Message>

    data class NoItems(
        override val isLoading: Boolean,
        override val messages: List<Message>
    ) : MagicClipboardUiState

    data class HasItems(
        val items: List<McbItem>,
        override val isLoading: Boolean,
        override val messages: List<Message>
    ) : MagicClipboardUiState
}

sealed interface Message {
    val messageId: Long
    val textId: Int
    val actionTextId: Int?
}

sealed interface ItemMessage : Message {
    val itemId: McbItemId

    data class Deletion(
        override val messageId: Long,
        @StringRes override val textId: Int,
        @StringRes override val actionTextId: Int? = null,
        override val itemId: McbItemId,
        val deletionSuccessful: Boolean
    ) : ItemMessage

    data class ItemLoadedInDeviceClipboard(
        override val messageId: Long,
        @StringRes override val textId: Int,
        @StringRes override val actionTextId: Int? = null,
        override val itemId: McbItemId,
    ) : ItemMessage

    data class ItemPastedInMagicClipboard(
        override val messageId: Long,
        @StringRes override val textId: Int,
        @StringRes override val actionTextId: Int? = null,
        override val itemId: McbItemId,
    ) : ItemMessage
}

private data class MagicClipboardViewModelState(
    val items: List<McbItem> = emptyList(),
    val isLoading: Boolean = false,
    val messages: List<Message> = emptyList()
) {
    fun toUiState(): MagicClipboardUiState =
        if (items.isEmpty()) {
            MagicClipboardUiState.NoItems(
                isLoading = isLoading,
                messages = messages
            )
        } else {
            MagicClipboardUiState.HasItems(
                items = items,
                isLoading = isLoading,
                messages = messages
            )
        }
}

class MagicClipboardViewModel(
    private val localStore: LocalStore,
    private val clipboardUsecases: ClipboardUsecases
) : BaseViewModel() {

    private val viewModelState = MutableStateFlow(MagicClipboardViewModelState(isLoading = true))

    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    private lateinit var observeCliboardItemsJob: Job

    fun onDeleteItem(itemId: McbItemId) {
        viewModelScope.launch {
            val result = localStore.deleteItem(itemId)
            viewModelState.update {
                val message = when (result) {
                    is Result.Error -> {
                        ItemMessage.Deletion(
                            messageId = UUID.randomUUID().mostSignificantBits,
                            textId = R.string.error_deleting_item,
                            actionTextId = R.string.retry,
                            itemId = itemId,
                            deletionSuccessful = false
                        )
                    }

                    Result.Success -> {
                        ItemMessage.Deletion(
                            messageId = UUID.randomUUID().mostSignificantBits,
                            textId = R.string.item_deleted_succesfully,
                            actionTextId = R.string.ok,
                            itemId = itemId,
                            deletionSuccessful = true
                        )
                    }
                }
                it.copy(messages = it.messages + message)
            }
        }
    }

    fun onPasteToMagicClipboard() {
        viewModelScope.launch {
            clipboardUsecases.pasteItemFromDeviceClipboard()?.let { newItemId ->
                viewModelState.update {
                    val message = ItemMessage.ItemPastedInMagicClipboard(
                        messageId = UUID.randomUUID().mostSignificantBits,
                        textId = R.string.item_pasted_in_magic_clipbaord,
                        actionTextId = R.string.ok,
                        itemId = newItemId
                    )
                    it.copy(messages = it.messages + message)
                }
            }
        }
    }

    fun onCopyItemToDeviceClipboard(item: McbItem) {
        clipboardUsecases.pasteItemIntoDeviceClipboard(item)
        viewModelState.update {
            val message = ItemMessage.ItemLoadedInDeviceClipboard(
                messageId = UUID.randomUUID().mostSignificantBits,
                textId = R.string.item_pasted_in_device_clipboard,
                actionTextId = R.string.ok,
                itemId = item.id
            )
            it.copy(messages = it.messages + message)
        }
    }

    /**
     * Notifies that the message has been shown on the screen.
     */
    fun messageShown(messageId: Long) {
        viewModelState.update { currenUiState ->
            val messages = currenUiState.messages.filterNot { it.messageId == messageId }
            currenUiState.copy(messages = messages)
        }
    }

    override fun onStart() {
        super.onStart()
        observeCliboardItemsJob = viewModelScope.launch {
            localStore.observeItems().collect { items ->
                viewModelState.update { it.copy(items = items, isLoading = false) }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        observeCliboardItemsJob.cancel()
    }
}
