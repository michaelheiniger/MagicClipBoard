package ch.qscqlmpa.magicclipboard.ui.magicclipboard.favoriteitems

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarResult
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ch.qscqlmpa.magicclipboard.R
import ch.qscqlmpa.magicclipboard.clipboard.McbItem
import ch.qscqlmpa.magicclipboard.clipboard.McbItemId
import ch.qscqlmpa.magicclipboard.debugClipBoardItems
import ch.qscqlmpa.magicclipboard.ui.Destination
import ch.qscqlmpa.magicclipboard.ui.common.DefaultSnackbar
import ch.qscqlmpa.magicclipboard.ui.magicclipboard.BottomNavItem
import ch.qscqlmpa.magicclipboard.ui.magicclipboard.ClipboardBottomBar
import ch.qscqlmpa.magicclipboard.ui.magicclipboard.ClipboardItemList
import ch.qscqlmpa.magicclipboard.ui.magicclipboard.ClipboardTopBar
import ch.qscqlmpa.magicclipboard.ui.magicclipboard.ItemMessage
import ch.qscqlmpa.magicclipboard.ui.magicclipboard.MagicClipboardUiState
import ch.qscqlmpa.magicclipboard.ui.magicclipboard.NoClipboardItems
import ch.qscqlmpa.magicclipboard.ui.magicclipboard.allitems.DeviceClipboardValue
import ch.qscqlmpa.magicclipboard.ui.theme.MagicClipBoardTheme
import java.time.LocalDateTime

@Preview(showBackground = true)
@Composable
private fun FavoritesBodyPreview() {
    MagicClipBoardTheme {
        FavoritesBody(
            uiState = MagicClipboardUiState(
                currentDateTime = LocalDateTime.now(),
                items = debugClipBoardItems.toList(),
                newItemsAdded = false,
                messages = emptyList(),
                deviceClipboardValue = "Here is an example of the value from device clipboard",
                username = "Ned Stark",
                newClipboardValue = null
            ),
            currentRoute = Destination.Clipboard.routeName,
            onItemClick = {},
            onDeleteItem = {},
            onItemFavoriteToggle = {},
            onPasteItemToDeviceClipboard = {},
            onPasteValueToMcb = {},
            onMessageDismissState = {},
            onSignOut = {},
        )
    }
}

@Composable
fun FavoritesScreen(
    viewModel: FavoriteItemsClipboardViewModel,
    currentRoute: String?,
    onItemClick: (BottomNavItem) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    FavoritesBody(
        uiState = uiState,
        currentRoute = currentRoute,
        onItemClick = onItemClick,
        onDeleteItem = viewModel::onDeleteItem,
        onItemFavoriteToggle = viewModel::onItemFavoriteToggle,
        onPasteItemToDeviceClipboard = viewModel::onPasteItemToDeviceClipboard,
        onPasteValueToMcb = viewModel::onPasteValueToMcb,
        onMessageDismissState = viewModel::messageShown,
        onSignOut = viewModel::onSignOut,
    )
}

@Composable
fun FavoritesBody(
    uiState: MagicClipboardUiState,
    currentRoute: String?,
    onItemClick: (BottomNavItem) -> Unit,
    onDeleteItem: (McbItemId) -> Unit,
    onItemFavoriteToggle: (McbItem) -> Unit,
    onPasteItemToDeviceClipboard: (McbItem) -> Unit,
    onPasteValueToMcb: (String) -> Unit,
    onMessageDismissState: (Long) -> Unit,
    onSignOut: () -> Unit,
) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            ClipboardTopBar(
                username = uiState.username,
                onSignOut = onSignOut
            )
        },
        snackbarHost = { DefaultSnackbar(snackbarHostState = scaffoldState.snackbarHostState) },
        bottomBar = { ClipboardBottomBar(currentRoute, onItemClick) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            if (uiState.deviceClipboardValue != null) {
                DeviceClipboardValue(
                    deviceClipboardValue = uiState.deviceClipboardValue,
                    onPasteValueToMcb = onPasteValueToMcb
                )
                Spacer(Modifier.height(8.dp))
            }
            when (uiState.items.isEmpty()) {
                true -> NoClipboardItems(R.string.no_favorite_items)
                false -> {
                    ClipboardItemList(
                        items = uiState.items,
                        currentDateTime = uiState.currentDateTime,
                        newItemsAdded = uiState.newItemsAdded,
                        onDeleteItem = onDeleteItem,
                        onItemFavoriteToggle = onItemFavoriteToggle,
                        onPasteItemToDeviceClipboard = onPasteItemToDeviceClipboard
                    )
                }
            }
        }
    }
    SnackbarMessageDisplayer(
        uiState = uiState,
        scaffoldState = scaffoldState,
        onDeleteItem = onDeleteItem,
        onMessageDismissState = onMessageDismissState
    )
}

@Composable
fun SnackbarMessageDisplayer(
    uiState: MagicClipboardUiState,
    scaffoldState: ScaffoldState,
    onDeleteItem: (McbItemId) -> Unit,
    onMessageDismissState: (Long) -> Unit
) {
    // Process one message at a time and show them as Snackbars in the UI
    if (uiState.messages.isNotEmpty()) {
        val message = remember(uiState) { uiState.messages.first() }

        val messageText = stringResource(message.textId)
        val actionTextId = message.actionTextId
        val actionText = actionTextId?.let { stringResource(actionTextId) }

        // If onDeleteItem or onMessageDismissState change while the LaunchedEffect is running,
        // don't restart the effect and use the latest lambda values.
        val onDeleteItemState by rememberUpdatedState(onDeleteItem)
        val onMessageDismissStateState by rememberUpdatedState(onMessageDismissState)

        LaunchedEffect(message.messageId, scaffoldState) {
            val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                message = messageText,
                actionLabel = actionText
            )
            if (snackbarResult == SnackbarResult.ActionPerformed) {
                when (message) {
                    is ItemMessage.Deletion -> onDeleteItemState(message.itemId) // retry
                    is ItemMessage.ItemLoadedInDeviceClipboard -> {
                        // Nothing to do
                    }
                }
            }
            // Once the message is displayed and dismissed, notify the ViewModel
            onMessageDismissStateState(message.messageId)
        }
    }
}
