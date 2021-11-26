package ch.qscqlmpa.magicclipboard.ui.magicclipboard

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.qscqlmpa.magicclipboard.R
import ch.qscqlmpa.magicclipboard.clipboard.McbItem
import ch.qscqlmpa.magicclipboard.clipboard.McbItemId
import ch.qscqlmpa.magicclipboard.debugClipBoardItems
import ch.qscqlmpa.magicclipboard.ui.MagicClipboardSimpleTopBar
import ch.qscqlmpa.magicclipboard.ui.common.DefaultSnackbar
import ch.qscqlmpa.magicclipboard.ui.common.LoadingSpinner
import ch.qscqlmpa.magicclipboard.ui.theme.MagicClipBoardTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object MagicClipboardUtils {
    val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
    val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
}

@Preview(showBackground = true)
@Composable
private fun MagicClipboardBodyPreview() {
    MagicClipBoardTheme {
        MagicClipboardBody(
            uiState = MagicClipboardUiState.HasItems(
                items = debugClipBoardItems.toList(),
                isLoading = false,
                messages = emptyList()
            ),
            onDeleteItem = {},
            onMessageDismissState = {}
        )
    }
}

@Composable
fun MagicClipboardScreen(viewModel: MagicClipboardViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    MagicClipboardBody(
        uiState = uiState,
        onDeleteItem = { itemId -> viewModel.onDeleteItem(itemId) },
        onMessageDismissState = viewModel::messageShown
    )
}

@Composable
fun MagicClipboardBody(
    uiState: MagicClipboardUiState,
    onDeleteItem: (McbItemId) -> Unit,
    onMessageDismissState: (Long) -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { MagicClipboardSimpleTopBar() },
        backgroundColor = MaterialTheme.colors.background,
        snackbarHost = { DefaultSnackbar(snackbarHostState = scaffoldState.snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 8.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            if (uiState.isLoading) LoadingSpinner()
            else {
                when (uiState) {
                    is MagicClipboardUiState.NoItems -> NoItems()
                    is MagicClipboardUiState.HasItems -> {
                        ClipboardItemList(
                            items = uiState.items,
                            onDeleteItem = { item -> onDeleteItem(item) }
                        )
                    }
                }
            }
        }
    }

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

        // Effect running in a coroutine that displays the Snackbar on the screen
        // If there's a change to messageText, actionText or scaffoldState,
        // the previous effect will be cancelled and a new one will start with the new values
        LaunchedEffect(messageText, actionText, scaffoldState) {
            val snackbarResult = scaffoldState.snackbarHostState.showSnackbar(
                message = messageText,
                actionLabel = actionText
            )
            if (snackbarResult == SnackbarResult.ActionPerformed) {
                when (message) {
                    is ItemMessage.Deletion -> {
                        if (!message.deletionSuccessful) onDeleteItemState(message.itemId) // retry
                    }
                }
            }
            // Once the message is displayed and dismissed, notify the ViewModel
            onMessageDismissStateState(message.messageId)
        }
    }
}

@Composable
private fun NoItems(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = 4.dp
    ) {
        Column(modifier.padding(horizontal = 8.dp, vertical = 8.dp)) {
            Text(stringResource(R.string.no_clipboard_item))
        }
    }
}

@Composable
private fun ClipboardItemList(
    items: List<McbItem>,
    onDeleteItem: (McbItemId) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().animateContentSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items, key = { item -> item.id.value }) { item -> ClipboardItem(item, onDeleteItem) }
    }
}

@Composable
private fun ClipboardItem(
    item: McbItem,
    onDeleteItem: (McbItemId) -> Unit
) {
    val dismissState = rememberDismissState()
    if (dismissState.isDismissed(DismissDirection.EndToStart)) {
        onDeleteItem(item.id)
    }
    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.EndToStart),
        dismissThresholds = { FractionalThreshold(0.5f) },
        background = {
            dismissState.dismissDirection ?: return@SwipeToDismiss
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    DismissValue.Default -> Color.LightGray
                    DismissValue.DismissedToEnd -> Color.Green
                    DismissValue.DismissedToStart -> Color.Red
                }
            )
            val scale by animateFloatAsState(
                if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
            )

            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxSize().background(color).padding(horizontal = 20.dp)
            ) {
                Text(
                    text = stringResource(R.string.delete_item),
                    color = Color.White,
                    fontSize = 20.sp,
                    modifier = Modifier.scale(scale)
                )
                Icon(
                    imageVector = Icons.Default.Delete,
                    tint = Color.White,
                    contentDescription = stringResource(R.string.delete_item_cd),
                    modifier = Modifier.scale(scale)
                )
            }
        },
        dismissContent = {
            val itemCd = stringResource(R.string.clipboard_item_cd, item.value) // Can't inline: composable
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = itemCd },
                elevation = animateDpAsState(if (dismissState.dismissDirection != null) 8.dp else 4.dp).value
            ) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(8.dp)
                ) {
                    Text(
                        text = formatClipBoardDate(item.creationDate),
                        fontSize = 10.sp
                    )
                    Text(
                        text = item.value,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    )
}

@Composable
private fun formatClipBoardDate(date: LocalDateTime): String {
    val now = LocalDateTime.now()
    return when {
        now.minusMinutes(1).isBefore(date) -> stringResource(R.string.clipboard_date_a_few_seconds_ago)
        now.minusMinutes(5).isBefore(date) -> stringResource(R.string.clipboard_date_a_few_minutes_ago)
        now.toLocalDate().isEqual(date.toLocalDate()) -> stringResource(
            R.string.clipboard_date_today_at,
            MagicClipboardUtils.timeFormatter.format(date)
        )
        else -> date.format(MagicClipboardUtils.dateTimeFormatter)
    }
}
