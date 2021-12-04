package ch.qscqlmpa.magicclipboard.ui.magicclipboard

import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import ch.qscqlmpa.magicclipboard.R
import ch.qscqlmpa.magicclipboard.clipboard.McbItem
import ch.qscqlmpa.magicclipboard.clipboard.McbItemId
import ch.qscqlmpa.magicclipboard.debugClipBoardItems
import ch.qscqlmpa.magicclipboard.ui.MagicClipboardSimpleTopBar
import ch.qscqlmpa.magicclipboard.ui.common.DefaultSnackbar
import ch.qscqlmpa.magicclipboard.ui.common.InfoDialog
import ch.qscqlmpa.magicclipboard.ui.common.LoadingSpinner
import ch.qscqlmpa.magicclipboard.ui.common.UiTags
import ch.qscqlmpa.magicclipboard.ui.components.multifab.MultiFabItem
import ch.qscqlmpa.magicclipboard.ui.components.multifab.MultiFabState
import ch.qscqlmpa.magicclipboard.ui.components.multifab.MultiFloatingActionButton
import ch.qscqlmpa.magicclipboard.ui.qrcodescanner.QrCodeScanResult
import ch.qscqlmpa.magicclipboard.ui.qrcodescanner.ScanQrCodeResultContract
import ch.qscqlmpa.magicclipboard.ui.theme.MagicClipBoardTheme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

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
            onCopyItemToDeviceClipboard = {},
            onPasteFromDeviceClipboard = {},
            onPasteFromQrCode = {},
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
        onMessageDismissState = viewModel::messageShown,
        onPasteFromDeviceClipboard = viewModel::onPasteToMagicClipboard,
        onCopyItemToDeviceClipboard = viewModel::onCopyItemToDeviceClipboard,
        onPasteFromQrCode = viewModel::onPasteFromQrCode
    )
}

sealed class ClipboardFabItem(
    override val id: String,
    override val content: @Composable () -> Unit,
    override val label: Int
) : MultiFabItem

object PasteFromDeviceClipboard : ClipboardFabItem(
    id = UiTags.pasteFromDeviceClipboard,
    content = @Composable {
        ClipboardFabIcon(
            primaryIcon = R.drawable.ic_baseline_content_paste_24,
            secondaryIcon = R.drawable.ic_baseline_phone_android_24
        )
    },
    label = R.string.paste_from_device_clipboard
)

object PasteFromQrCode : ClipboardFabItem(
    id = UiTags.pasteFromQrCode,
    content = @Composable {
        ClipboardFabIcon(
            primaryIcon = R.drawable.ic_baseline_content_paste_24,
            secondaryIcon = R.drawable.ic_baseline_qr_code_24
        )
    },
    label = R.string.paste_from_qr_code
)

@Composable
private fun ClipboardFabIcon(
    primaryIcon: Int,
    secondaryIcon: Int
) {
    Box {
        Icon(
            painter = painterResource(primaryIcon),
            tint = Color.White,
            contentDescription = "", // TODO
        )
        Icon(
            painter = painterResource(secondaryIcon),
            tint = Color.Black,
            contentDescription = "", // TODO
            modifier = Modifier
                .scale(0.7f)
                .align(Alignment.BottomEnd)
                .offset(12.dp, 12.dp)
        )
    }
}

@Composable
fun MagicClipboardBody(
    uiState: MagicClipboardUiState,
    onDeleteItem: (McbItemId) -> Unit,
    onCopyItemToDeviceClipboard: (McbItem) -> Unit,
    onPasteFromDeviceClipboard: () -> Unit,
    onPasteFromQrCode: (String) -> Unit,
    onMessageDismissState: (Long) -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { MagicClipboardSimpleTopBar() },
        snackbarHost = { DefaultSnackbar(snackbarHostState = scaffoldState.snackbarHostState) }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                if (uiState.isLoading) LoadingSpinner()
                else {
                    when (uiState) {
                        is MagicClipboardUiState.NoItems -> NoItems()
                        is MagicClipboardUiState.HasItems -> {
                            ClipboardItemList(
                                items = uiState.items,
                                onDeleteItem = { item -> onDeleteItem(item) },
                                onCopyItemToDeviceClipboard = onCopyItemToDeviceClipboard
                            )
                        }
                    }
                }
            }
            var offsetX by remember { mutableStateOf(0f) }
            var offsetY by remember { mutableStateOf(0f) }
            PasteFab(
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
                onPasteFromDeviceClipboard = onPasteFromDeviceClipboard,
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
                    is ItemMessage.Deletion -> {
                        if (!message.deletionSuccessful) onDeleteItemState(message.itemId) // retry
                    }
                    is ItemMessage.ItemLoadedInDeviceClipboard -> {
                        // Nothing to do
                    }
                    is ItemMessage.ItemPastedInMagicClipboard -> {
                    }
                }
            }
            // Once the message is displayed and dismissed, notify the ViewModel
            onMessageDismissStateState(message.messageId)
        }
    }
}

@Composable
private fun PasteFab(
    modifier: Modifier = Modifier,
    onPasteFromDeviceClipboard: () -> Unit,
    onPasteFromQrCode: (String) -> Unit
) {
    var state by remember { mutableStateOf(MultiFabState.COLLAPSED) }
    val launcher = qrCodeScanner(onQrCodeScan = onPasteFromQrCode)

    Column(modifier = modifier, content = {
        MultiFloatingActionButton(
            mainFabText = R.string.paste,
            mainFabIcon = R.drawable.ic_baseline_content_paste_24,
            items = listOf(
                PasteFromDeviceClipboard,
                PasteFromQrCode
            ),
            targetState = state,
            stateChanged = { targetState -> state = targetState },
            onFabItemClicked = { item ->
                when (item) {
                    PasteFromDeviceClipboard -> onPasteFromDeviceClipboard()
                    PasteFromQrCode -> launcher.launch(Unit)
                }
                state = MultiFabState.COLLAPSED
            }
        )
    })
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
    onDeleteItem: (McbItemId) -> Unit,
    onCopyItemToDeviceClipboard: (McbItem) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .animateContentSize()
            .testTag(UiTags.clipboardItemList),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items, key = { item -> item.id.value }) { item ->
            ClipboardItem(
                item = item,
                onDeleteItem = onDeleteItem,
                onCopyItemToDeviceClipboard = onCopyItemToDeviceClipboard
            )
        }
    }
}

@Composable
private fun ClipboardItem(
    item: McbItem,
    onDeleteItem: (McbItemId) -> Unit,
    onCopyItemToDeviceClipboard: (McbItem) -> Unit
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
                sliding = dismissState.dismissDirection != null,
                onCopyItemToDeviceClipboard = onCopyItemToDeviceClipboard
            )
        }
    )
}

@Composable
private fun ClipboardItemContent(
    item: McbItem,
    sliding: Boolean,
    onCopyItemToDeviceClipboard: (McbItem) -> Unit
) {
    val itemCd = stringResource(R.string.clipboard_item_cd, item.value) // Can't inline: composable
    val context = LocalContext.current
    var showQrCodeModal by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("${UiTags.clipboardItem}_${item.id.value}")
            .semantics { contentDescription = itemCd },
        elevation = animateDpAsState(if (sliding) 8.dp else 4.dp).value
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Text(
                text = formatClipBoardDate(item.creationDate),
                fontSize = 10.sp
            )
            Text(
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
                    onClick = { onCopyItemToDeviceClipboard(item) }
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
private fun qrCodeScanner(
    onQrCodeScan: (String) -> Unit,
    onClose: () -> Unit = {}
): ManagedActivityResultLauncher<Unit, QrCodeScanResult> {
    val showErrorDialog = remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(ScanQrCodeResultContract()) { scanResult ->
        when (scanResult) {
            QrCodeScanResult.NoResult -> {
            } // Nothing to do
            QrCodeScanResult.Error -> showErrorDialog.value = true
            is QrCodeScanResult.Success -> onQrCodeScan(scanResult.value)
        }
        onClose()
    }

    if (showErrorDialog.value) {
        InfoDialog(
            text = R.string.error_scanning_qr_code,
            onCloseClick = { showErrorDialog.value = false }
        ) {}
    }
    return launcher
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
