package ch.qscqlmpa.magicclipboard.ui.magicclipboard

import android.content.Intent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import ch.qscqlmpa.magicclipboard.R
import ch.qscqlmpa.magicclipboard.clipboard.McbItem
import ch.qscqlmpa.magicclipboard.clipboard.McbItemId
import ch.qscqlmpa.magicclipboard.debugClipBoardItems
import ch.qscqlmpa.magicclipboard.ui.common.DefaultSnackbar
import ch.qscqlmpa.magicclipboard.ui.common.InfoDialog
import ch.qscqlmpa.magicclipboard.ui.common.UiTags
import ch.qscqlmpa.magicclipboard.ui.common.YesNoDialog
import ch.qscqlmpa.magicclipboard.ui.theme.MagicClipBoardTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import java.time.LocalDateTime
import kotlin.math.roundToInt

@Preview(showBackground = true)
@Composable
private fun MagicClipboardBodyPreview() {
    MagicClipBoardTheme {
        MagicClipboardBody(
            uiState = MagicClipboardUiState(
                currentDateTime = LocalDateTime.now(),
                items = debugClipBoardItems.toList(),
                newItemsAdded = false,
                messages = emptyList(),
                deviceClipboardValue = "Here is an example of the value from device clipboard",
                username = "Ned Stark",
                newClipboardValue = null
            ),
            onDeleteItem = {},
            onPasteItemToDeviceClipboard = {},
            onPasteValueToMcb = {},
            onPasteFromQrCode = {},
            onDismissNewClipboardValue = {},
            onMessageDismissState = {},
            onSignOut = {},
        )
    }
}

@Composable
fun MagicClipboardScreen(viewModel: MagicClipboardViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    MagicClipboardBody(
        uiState = uiState,
        onDeleteItem = { itemId -> viewModel.onDeleteItem(itemId) },
        onPasteValueToMcb = viewModel::onPasteValueToMcb,
        onPasteItemToDeviceClipboard = viewModel::onPasteItemToDeviceClipboard,
        onPasteFromQrCode = viewModel::onPasteFromQrCode,
        onDismissNewClipboardValue = viewModel::onDismissNewClipboardValue,
        onMessageDismissState = viewModel::messageShown,
        onSignOut = viewModel::onLogout,
    )
}

@Composable
fun MagicClipboardBody(
    uiState: MagicClipboardUiState,
    onDeleteItem: (McbItemId) -> Unit,
    onPasteItemToDeviceClipboard: (McbItem) -> Unit,
    onPasteValueToMcb: (String) -> Unit,
    onPasteFromQrCode: (String) -> Unit,
    onDismissNewClipboardValue: () -> Unit,
    onMessageDismissState: (Long) -> Unit,
    onSignOut: () -> Unit,
) {
    if (uiState.newClipboardValue != null) {
        YesNoDialog(
            content = {
                Text(text = stringResource(R.string.add_new_value))
                Surface(color = Color.DarkGray) {
                    Text(text = uiState.newClipboardValue)
                }
            },
            onYesClick = { onPasteValueToMcb(uiState.newClipboardValue) },
            onNoClick = onDismissNewClipboardValue
        )
    }

    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            MagicClipboardTopBar(
                username = uiState.username,
                onSignOut = onSignOut
            )
        },
        snackbarHost = { DefaultSnackbar(snackbarHostState = scaffoldState.snackbarHostState) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
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
                    true -> NoClipboardItems()
                    false -> {
                        ClipboardItemList(
                            items = uiState.items,
                            currentDateTime = uiState.currentDateTime,
                            newItemsAdded = uiState.newItemsAdded,
                            onDeleteItem = { item -> onDeleteItem(item) },
                            onPasteItemToDeviceClipboard = onPasteItemToDeviceClipboard
                        )
                    }
                }
            }

            var offsetX by remember { mutableStateOf(0f) }
            var offsetY by remember { mutableStateOf(0f) }
            PasteFab(
                deviceClipboardValue = uiState.deviceClipboardValue,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 10.dp, end = 10.dp)
                    .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consumeAllChanges()
                            offsetX += dragAmount.x
                            offsetY += dragAmount.y
                        }
                    },
                onPasteFromDeviceClipboard = onPasteValueToMcb,
                onPasteFromQrCode = onPasteFromQrCode
            )
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

@Composable
private fun MagicClipboardTopBar(
    username: String,
    onSignOut: () -> Unit
) {
    TopAppBar(
        title = {
            var showSignOutDialog by remember { mutableStateOf(false) }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = username,
                    color = MaterialTheme.colors.onPrimary
                )
                TextButton(onClick = {
                    showSignOutDialog = true
                }) {
                    Text(
                        text = stringResource(R.string.signOut),
                        color = MaterialTheme.colors.onPrimary
                    )
                }
            }
            if (showSignOutDialog) {
                val context = LocalContext.current
                YesNoDialog(
                    text = R.string.signOutConfirmation,
                    onYesClick = {
                        onSignOut()
                        val gso = GoogleSignInOptions.Builder().build()
                        GoogleSignIn.getClient(context, gso).signOut()
                    },
                    onClose = { showSignOutDialog = false }
                )
            }
        }
    )
}

@Composable
private fun DeviceClipboardValue(
    modifier: Modifier = Modifier,
    deviceClipboardValue: String,
    onPasteValueToMcb: (String) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
            .background(color = MaterialTheme.colors.secondary)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val label = stringResource(R.string.valueInDeviceClipboard)
        Text(
            modifier = Modifier
                .testTag(UiTags.deviceClipboardValue)
                .weight(1f),
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(label)
                }
                append(text = " $deviceClipboardValue")
            },
            color = Color.White,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2
        )
        IconButton(
            modifier = Modifier.defaultMinSize(24.dp),
            onClick = { onPasteValueToMcb(deviceClipboardValue) }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_baseline_content_paste_24),
                tint = Color.White,
                contentDescription = stringResource(R.string.paste_value_from_device_clipboard_cd)
            )
        }
    }
}

@Composable
private fun NoClipboardItems(modifier: Modifier = Modifier) {
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
    currentDateTime: LocalDateTime,
    newItemsAdded: Boolean,
    onDeleteItem: (McbItemId) -> Unit,
    onPasteItemToDeviceClipboard: (McbItem) -> Unit
) {
    val listState = rememberLazyListState()
    if (newItemsAdded) LaunchedEffect(key1 = items) { listState.animateScrollToItem(0) }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .animateContentSize()
            .testTag(UiTags.clipboardItemList),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items, key = { item -> item.id.value }) { item ->
            ClipboardItem(
                item = item,
                currentDateTime = currentDateTime,
                onDeleteItem = onDeleteItem,
                onPasteItemToDeviceClipboard = onPasteItemToDeviceClipboard
            )
        }
    }
}

@Composable
private fun ClipboardItem(
    item: McbItem,
    currentDateTime: LocalDateTime,
    onDeleteItem: (McbItemId) -> Unit,
    onPasteItemToDeviceClipboard: (McbItem) -> Unit
) {
    val dismissState = rememberDismissState()
    if (dismissState.isDismissed(DismissDirection.EndToStart)) onDeleteItem(item.id)

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
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp)
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
            ClipboardItemContent(
                item = item,
                currentDateTime = currentDateTime,
                sliding = dismissState.dismissDirection != null,
                onPasteItemToDeviceClipboard = onPasteItemToDeviceClipboard
            )
        }
    )
}

fun clipboardItemRootTag(itemId: McbItemId) = "${UiTags.clipboardItem}_${itemId.value}"
fun clipboardItemCreationDateTag(itemId: McbItemId) = "${clipboardItemRootTag(itemId)}_creationDate"
fun clipboardItemValueTag(itemId: McbItemId) = "${clipboardItemRootTag(itemId)}_value"
fun clipboardItemRootTag(item: McbItem) = clipboardItemRootTag(item.id)
fun clipboardItemCreationDateTag(item: McbItem) = clipboardItemCreationDateTag(item.id)
fun clipboardItemValueTag(item: McbItem) = clipboardItemValueTag(item.id)

@Composable
private fun ClipboardItemContent(
    item: McbItem,
    currentDateTime: LocalDateTime,
    sliding: Boolean,
    onPasteItemToDeviceClipboard: (McbItem) -> Unit
) {
    val itemCd = stringResource(R.string.clipboard_item_cd, item.value) // Can't inline: composable
    val context = LocalContext.current
    var showQrCodeModal by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(clipboardItemRootTag(item))
            .semantics { contentDescription = itemCd },
        elevation = animateDpAsState(if (sliding) 8.dp else 4.dp).value
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Text(
                modifier = Modifier.testTag(clipboardItemCreationDateTag(item)),
                text = formatClipBoardDate(item.creationDate, currentDateTime),
                fontSize = 10.sp
            )
            Text(
                modifier = Modifier.testTag(clipboardItemValueTag(item)),
                text = item.value,
                overflow = TextOverflow.Ellipsis
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = { showQrCodeModal = true }) {
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_qr_code_24),
                        contentDescription = stringResource(R.string.show_value_as_qr_code_cd)
                    )
                }
                IconButton(onClick = {
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, item.value)
                        type = "text/plain"
                    }
                    startActivity(context, intent, null)
                }) {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = stringResource(R.string.share_clipboard_item_cd)
                    )
                }
                IconButton(
                    modifier = Modifier.testTag("${UiTags.clipboardItem}_${item.id.value}_copyToDevice"),
                    onClick = { onPasteItemToDeviceClipboard(item) }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_baseline_content_copy_24),
                        contentDescription = stringResource(R.string.load_clipboard_item_into_device_clipboard_cd)
                    )
                }
            }
        }
    }

    if (showQrCodeModal) {
        ShowQrCodeModal(item, onCloseClick = { showQrCodeModal = false })
    }
}

@Composable
private fun ShowQrCodeModal(item: McbItem, onCloseClick: () -> Unit) {
    InfoDialog(
        text = R.string.value_as_qr_code,
        content = {
            Image(
                bitmap = item.valueAsQrCode.asImageBitmap(),
                contentDescription = stringResource(R.string.value_as_qr_code_cd),
                modifier = Modifier.fillMaxWidth()
            )
        },
        onCloseClick = onCloseClick
    )
}

@Composable
private fun formatClipBoardDate(date: LocalDateTime, currentDateTime: LocalDateTime): String {
    return when {
        currentDateTime.minusMinutes(1).isBefore(date) -> stringResource(R.string.clipboard_date_a_few_seconds_ago)
        currentDateTime.minusMinutes(5).isBefore(date) -> stringResource(R.string.clipboard_date_a_few_minutes_ago)
        currentDateTime.toLocalDate().isEqual(date.toLocalDate()) -> stringResource(
            R.string.clipboard_date_today_at,
            timeFormatter.format(date)
        )
        else -> date.format(dateTimeFormatter)
    }
}
