package ch.qscqlmpa.magicclipboard.ui.magicclipboard

import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import ch.qscqlmpa.magicclipboard.R
import ch.qscqlmpa.magicclipboard.auth.SessionManager
import ch.qscqlmpa.magicclipboard.auth.SessionState
import ch.qscqlmpa.magicclipboard.clipboard.MagicClipboardRepository
import ch.qscqlmpa.magicclipboard.clipboard.McbItem
import ch.qscqlmpa.magicclipboard.clipboard.McbItemId
import ch.qscqlmpa.magicclipboard.clipboard.usecases.DeleteClipboardItemUsecase
import ch.qscqlmpa.magicclipboard.clipboard.usecases.DeviceClipboardUsecases
import ch.qscqlmpa.magicclipboard.clipboard.usecases.NewClipboardItemUsecase
import ch.qscqlmpa.magicclipboard.idlingresource.McbIdlingResource
import ch.qscqlmpa.magicclipboard.launch
import ch.qscqlmpa.magicclipboard.ui.Destination
import ch.qscqlmpa.magicclipboard.ui.ScreenNavigator
import ch.qscqlmpa.magicclipboard.ui.navOptionsPopUpToInclusive
import ch.qscqlmpa.magicclipboard.viewmodel.BaseViewModel
import java.time.LocalDateTime
import java.util.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MagicClipboardViewModel(
    private val magicClipboardRepository: MagicClipboardRepository,
    private val deviceClipboardUsecases: DeviceClipboardUsecases,
    private val deleteClipboardItemUsecase: DeleteClipboardItemUsecase,
    private val newClipboardItemUsecase: NewClipboardItemUsecase,
    private val idlingResource: McbIdlingResource,
    private val sessionManager: SessionManager,
    private val screenNavigator: ScreenNavigator,
    private val newClipboardValue: String?
) : BaseViewModel() {

    private val viewModelState = MutableStateFlow(
        MagicClipboardViewModelState(
            deviceClipboardValue = deviceClipboardUsecases.getDeviceClipboardValue(),
            newClipboardValue = newClipboardValue
        )
    )

    val uiState = viewModelState
        .map { it.toUiState() }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            viewModelState.value.toUiState()
        )

    private lateinit var observeCliboardItemsJob: Job
    private lateinit var observeLoginJob: Job
    private lateinit var recompositionSchedulerJob: Job

    fun onDeleteItem(itemId: McbItemId) {
        viewModelScope.launch(
            beforeLaunch = { idlingResource.increment("Deleting item $itemId") }
        ) {
            deleteClipboardItemUsecase.deleteItem(itemId)
        }
    }

    fun onPasteValueToMcb(value: String) {
        if (value == newClipboardValue) viewModelState.update { it.copy(newClipboardValue = null) }
        viewModelScope.launch(
            beforeLaunch = { idlingResource.increment("Pasting to MagicClipboard") }
        ) {
            newClipboardItemUsecase.addNewItem(value)
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
                // Device clipboard value has changed so we need to update the value displayed
                deviceClipboardValue = deviceClipboardUsecases.getDeviceClipboardValue(),
                messages = it.messages + message
            )
        }
    }

    fun onPasteFromQrCode(value: String) {
        viewModelScope.launch {
            newClipboardItemUsecase.addNewItem(value)
        }
    }

    fun onDismissNewClipboardValue() {
        viewModelState.update { it.copy(newClipboardValue = null) }
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

    fun onLogout() {
        viewModelScope.launch {
            sessionManager.signOut()
        }
    }

    override fun onStart() {
        super.onStart()
        observeLoginJob = viewModelScope.launch {
            sessionManager.sessionState().collect { state ->
                when (state) {
                    SessionState.Unauthenticated -> {
                        screenNavigator.navigate(
                            Destination.SignIn,
                            navOptions = navOptionsPopUpToInclusive(Destination.MagicClipboard.routeName)
                        )
                    }
                    is SessionState.SignedIn.Authenticated -> viewModelState.update { it.copy(username = state.username) }
                    else -> {
                        // Nothing to do
                    }
                }
            }
        }

        idlingResource.increment("Loading Item list")
        observeCliboardItemsJob = viewModelScope.launch {
            magicClipboardRepository.observeItems().collect { items ->
                viewModelState.update {
                    it.copy(
                        previousItemsState = it.currentItemsState,
                        currentItemsState = items
                    )
                }
                idlingResource.decrement("Item list updated")
            }
        }
        recompositionSchedulerJob = viewModelScope.launch {
            tickerFlow(1).collect { viewModelState.update { it.copy(currentDateTime = LocalDateTime.now()) } }
        }
        viewModelState.update { it.copy(deviceClipboardValue = deviceClipboardUsecases.getDeviceClipboardValue()) }
    }

    override fun onStop() {
        super.onStop()
        observeCliboardItemsJob.cancel()
        observeLoginJob.cancel()
        recompositionSchedulerJob.cancel()
    }

    private fun tickerFlow(
        start: Long = 0,
        initialDelayMs: Long = 0,
        periodMs: Long = 60_000 // Every minute since the resolution of the time-based content is one minute
    ) = flow {
        delay(initialDelayMs)

        var counter = start
        while (true) {
            emit(counter)
            counter += 1
            delay(periodMs)
        }
    }
}

data class MagicClipboardUiState(
    val currentDateTime: LocalDateTime,
    val items: List<McbItem>,
    val newItemsAdded: Boolean,
    val messages: List<Message>,
    val deviceClipboardValue: String?,
    val username: String,
    val newClipboardValue: String?
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
    val currentDateTime: LocalDateTime = LocalDateTime.now(),
    val previousItemsState: List<McbItem> = emptyList(),
    val currentItemsState: List<McbItem> = emptyList(),
    val messages: List<Message> = emptyList(),
    val newClipboardValue: String?,
    val deviceClipboardValue: String? = null,
    val username: String = ""
) {
    fun toUiState(): MagicClipboardUiState =
        MagicClipboardUiState(
            currentDateTime = currentDateTime,
            items = currentItemsState,
            newItemsAdded = currentItemsState.size > previousItemsState.size,
            messages = messages,
            deviceClipboardValue = deviceClipboardValue,
            username = username,
            newClipboardValue = newClipboardValue
        )
}
