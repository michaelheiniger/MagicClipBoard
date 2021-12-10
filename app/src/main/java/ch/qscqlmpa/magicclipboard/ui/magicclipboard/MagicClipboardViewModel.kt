package ch.qscqlmpa.magicclipboard.ui.magicclipboard

import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import ch.qscqlmpa.magicclipboard.R
import ch.qscqlmpa.magicclipboard.clipboard.MagicClipboardRepository
import ch.qscqlmpa.magicclipboard.clipboard.McbItem
import ch.qscqlmpa.magicclipboard.clipboard.McbItemId
import ch.qscqlmpa.magicclipboard.clipboard.usecases.DeleteClipboardItemUsecase
import ch.qscqlmpa.magicclipboard.clipboard.usecases.DeviceClipboardUsecases
import ch.qscqlmpa.magicclipboard.clipboard.usecases.NewClibboardItemUsecase
import ch.qscqlmpa.magicclipboard.data.Result
import ch.qscqlmpa.magicclipboard.idlingresource.McbIdlingResource
import ch.qscqlmpa.magicclipboard.launch
import ch.qscqlmpa.magicclipboard.viewmodel.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

data class MagicClipboardUiState(
    val items: List<McbItem>,
    val newItemsAdded: Boolean,
    val isLoading: Boolean,
    val messages: List<Message>,
    val deviceClipboardValue: String?,
)

sealed interface Message {
    val messageId: Long
    val textId: Int
    val actionTextId: Int?
}

sealed interface ItemMessage : Message {
    val itemId: McbItemId

    data class Deletion(
        override val messageId: Long,
        @StringRes override val textId: Int = R.string.error_deleting_item,
        @StringRes override val actionTextId: Int? = R.string.retry,
        override val itemId: McbItemId
    ) : ItemMessage

    data class ItemLoadedInDeviceClipboard(
        override val messageId: Long,
        @StringRes override val textId: Int,
        @StringRes override val actionTextId: Int? = null,
        override val itemId: McbItemId,
    ) : ItemMessage
}

private data class MagicClipboardViewModelState(
    val previousItemsState: List<McbItem> = emptyList(),
    val currentItemsState: List<McbItem> = emptyList(),
    val isLoading: Boolean = false,
    val messages: List<Message> = emptyList(),
    val deviceClipboardValue: String? = null
) {
    fun toUiState(): MagicClipboardUiState =
        MagicClipboardUiState(
            items = currentItemsState,
            newItemsAdded = currentItemsState.size > previousItemsState.size,
            isLoading = isLoading,
            messages = messages,
            deviceClipboardValue = deviceClipboardValue
        )
}

class MagicClipboardViewModel(
    private val magicClipboardRepository: MagicClipboardRepository,
    private val deviceClipboardUsecases: DeviceClipboardUsecases,
    private val deleteClipboardItemUsecase: DeleteClipboardItemUsecase,
    private val newClibboardItemUsecase: NewClibboardItemUsecase,
    private val idlingResource: McbIdlingResource
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
        viewModelScope.launch(
            beforeLaunch = { idlingResource.increment("Deleting item $itemId") }
        ) {
            when (deleteClipboardItemUsecase.deleteItem(itemId)) {
                is Result.Error -> {
                    viewModelState.update {
                        val message = ItemMessage.Deletion(
                            messageId = UUID.randomUUID().mostSignificantBits,
                            itemId = itemId,
                        )
                        it.copy(messages = it.messages + message)
                    }
                }
                Result.Success -> {
                    // Nothing to do
                }
            }
        }
    }

    fun onPasteToMagicClipboard(value: String) {
        viewModelScope.launch(
            beforeLaunch = { idlingResource.increment("Pasting to MagicClipboard") }
        ) {
            newClibboardItemUsecase.pasteValueToMcb(value)
        }
    }

    fun onPasteItemToDeviceClipboard(item: McbItem) {
        deviceClipboardUsecases.pasteItemIntoDeviceClipboard(item)
        viewModelState.update {
            val message = ItemMessage.ItemLoadedInDeviceClipboard(
                messageId = UUID.randomUUID().mostSignificantBits,
                textId = R.string.item_pasted_in_device_clipboard,
                actionTextId = R.string.ok,
                itemId = item.id
            )
            it.copy(
                deviceClipboardValue = deviceClipboardUsecases.getDeviceClipboardValue(), // Device clipboard value has changed
                messages = it.messages + message
            )
        }
    }

    fun onPasteFromQrCode(value: String) {
        viewModelScope.launch {
            newClibboardItemUsecase.pasteValueToMcb(value)
        }
    }

    /**
     * Notifies that the message has been shown on the screen.
     */
    fun messageShown(messageId: Long) {
        viewModelState.update { currentUiState ->
            val messages = currentUiState.messages.filterNot { it.messageId == messageId }
            currentUiState.copy(messages = messages)
        }
    }

    override fun onStart() {
        super.onStart()
        idlingResource.increment("Loading Item list")
        observeCliboardItemsJob = viewModelScope.launch {
            magicClipboardRepository.observeItems().collect { items ->
                viewModelState.update {
                    it.copy(
                        previousItemsState = it.currentItemsState,
                        currentItemsState = items,
                        isLoading = false
                    )
                }
                idlingResource.decrement("Item list updated")
            }
        }
        viewModelState.update { it.copy(deviceClipboardValue = deviceClipboardUsecases.getDeviceClipboardValue()) }
    }

    override fun onStop() {
        super.onStop()
        observeCliboardItemsJob.cancel()
    }
}
