package ch.qscqlmpa.magicclipboard.ui.magicclipboard

import androidx.lifecycle.viewModelScope
import ch.qscqlmpa.magicclipboard.clipboard.McbItem
import ch.qscqlmpa.magicclipboard.store.local.LocalStore
import ch.qscqlmpa.magicclipboard.viewmodel.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

sealed class McbItemsState {
    data class Success(val items: List<McbItem>) : McbItemsState()
    object Loading : McbItemsState()
}

@Suppress("LongParameterList")
class MagicClipboardVM(
    private val localStore: LocalStore
) : BaseViewModel() {

    private lateinit var clipBoardItemsJob: Job
    private val _clipBoardItemsState = MutableStateFlow<McbItemsState>(McbItemsState.Loading)
    val clipBoardItemsState: StateFlow<McbItemsState> = _clipBoardItemsState

    override fun onStart() {
        super.onStart()
        clipBoardItemsJob = viewModelScope.launch {
            localStore.observeItems().collect { items -> _clipBoardItemsState.value = McbItemsState.Success(items) }
        }
    }

    override fun onStop() {
        super.onStop()
        clipBoardItemsJob.cancel()
    }
}
