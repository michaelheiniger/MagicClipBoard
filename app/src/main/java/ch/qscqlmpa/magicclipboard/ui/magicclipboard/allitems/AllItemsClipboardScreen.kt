package ch.qscqlmpa.magicclipboard.ui.magicclipboard.allitems

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.qscqlmpa.magicclipboard.R
import ch.qscqlmpa.magicclipboard.clipboard.McbItem
import ch.qscqlmpa.magicclipboard.clipboard.McbItemId
import ch.qscqlmpa.magicclipboard.data.remote.ConnectionStatus
import ch.qscqlmpa.magicclipboard.debugClipBoardItems
import ch.qscqlmpa.magicclipboard.ui.Destination
import ch.qscqlmpa.magicclipboard.ui.common.DefaultSnackbar
import ch.qscqlmpa.magicclipboard.ui.common.UiTags
import ch.qscqlmpa.magicclipboard.ui.common.YesNoDialog
import ch.qscqlmpa.magicclipboard.ui.components.animations.SingleStateWithTransitionAnimState
import ch.qscqlmpa.magicclipboard.ui.magicclipboard.BottomNavItem
import ch.qscqlmpa.magicclipboard.ui.magicclipboard.ClipboardBottomBar
import ch.qscqlmpa.magicclipboard.ui.magicclipboard.ClipboardItemList
import ch.qscqlmpa.magicclipboard.ui.magicclipboard.MagicClipboardUiState
import ch.qscqlmpa.magicclipboard.ui.magicclipboard.NoClipboardItems
import ch.qscqlmpa.magicclipboard.ui.magicclipboard.SignedInTopBar
import ch.qscqlmpa.magicclipboard.ui.magicclipboard.favoriteitems.SnackbarMessageDisplayer
import ch.qscqlmpa.magicclipboard.ui.theme.MagicClipBoardTheme
import java.time.LocalDateTime
import kotlin.math.roundToInt

@Preview(showBackground = true)
@Composable
private fun AllItemsClipboardBodyPreview() {
    MagicClipBoardTheme {
        AllItemsClipboardBody(
            uiState = MagicClipboardUiState(
                currentDateTime = LocalDateTime.now(),
                items = debugClipBoardItems.toList(),
                newItemsAdded = false,
                messages = emptyList(),
                deviceClipboardValue = "Here is an example of the value from device clipboard",
                username = "Sam Gamegie",
                connectionStatus = ConnectionStatus.Connected,
                newClipboardValue = null
            ),
            currentRoute = Destination.Clipboard.routeName,
            onBottomBarItemClick = {},
            onToggleDarkTheme = {},
            onDeleteItem = {},
            onItemFavoriteToggle = {},
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
fun AllItemsClipboardScreen(
    viewModel: AllItemsClipboardViewModel,
    currentRoute: String?,
    onBottomBarItemClick: (BottomNavItem) -> Unit,
    onToggleDarkTheme: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    AllItemsClipboardBody(
        uiState = uiState,
        currentRoute = currentRoute,
        onBottomBarItemClick = onBottomBarItemClick,
        onToggleDarkTheme = onToggleDarkTheme,
        onDeleteItem = viewModel::onDeleteItem,
        onItemFavoriteToggle = viewModel::onItemFavoriteToggle,
        onPasteValueToMcb = viewModel::onPasteValueToMcb,
        onPasteItemToDeviceClipboard = viewModel::onPasteItemToDeviceClipboard,
        onPasteFromQrCode = viewModel::onPasteValueToMcb,
        onDismissNewClipboardValue = viewModel::onDismissNewClipboardValue,
        onMessageDismissState = viewModel::messageShown,
        onSignOut = viewModel::onSignOut,
    )
}

@Composable
fun AllItemsClipboardBody(
    uiState: MagicClipboardUiState,
    currentRoute: String?,
    onBottomBarItemClick: (BottomNavItem) -> Unit,
    onToggleDarkTheme: () -> Unit,
    onDeleteItem: (McbItemId) -> Unit,
    onItemFavoriteToggle: (McbItem) -> Unit,
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
            SignedInTopBar(
                username = uiState.username,
                onToggleDarkTheme = onToggleDarkTheme,
                onSignOut = onSignOut
            )
        },
        snackbarHost = { DefaultSnackbar(snackbarHostState = scaffoldState.snackbarHostState) },
        bottomBar = { ClipboardBottomBar(currentRoute, onBottomBarItemClick) }
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
                if (uiState.connectionStatus == ConnectionStatus.Disconnected) {
                    ConnectionStatusBanner()
                    Spacer(Modifier.height(8.dp))
                }
                if (uiState.deviceClipboardValue != null) {
                    DeviceClipboardValue(
                        deviceClipboardValue = uiState.deviceClipboardValue,
                        onPasteValueToMcb = onPasteValueToMcb
                    )
                    Spacer(Modifier.height(8.dp))
                }
                when (uiState.items.isEmpty()) {
                    true -> NoClipboardItems(R.string.no_clipboard_items)
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

            var offsetX by remember { mutableStateOf(0f) }
            var offsetY by remember { mutableStateOf(0f) }
            PasteFab(
                deviceClipboardValue = uiState.deviceClipboardValue,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 10.dp, end = 10.dp)
                    .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount.x
                            offsetY += dragAmount.y
                        }
                    },
                onPasteFromDeviceClipboard = onPasteValueToMcb,
                onPasteFromQrCode = onPasteFromQrCode
            )
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
fun ConnectionStatusBanner() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
            .background(Color.Red)
            .padding(8.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_baseline_offline_bolt_24),
            tint = Color.White,
            contentDescription = stringResource(R.string.delete_item_cd),
        )
        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
        Text(
            text = stringResource(R.string.disconnected_from_store),
            color = Color.White,
            fontSize = 20.sp,
        )
    }
}

@Composable
fun DeviceClipboardValue(
    modifier: Modifier = Modifier,
    deviceClipboardValue: String,
    onPasteValueToMcb: (String) -> Unit
) {
    val scrollState = rememberScrollState()
    var textExpanded by remember { mutableStateOf(false) }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
            .background(color = MaterialTheme.colors.secondary)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val label = stringResource(R.string.valueInDeviceClipboard)
        Row(
            modifier = Modifier
                .verticalScroll(scrollState)
                .weight(1f)
                .clickable { textExpanded = !textExpanded }
        ) {
            Text(
                modifier = Modifier
                    .testTag(UiTags.deviceClipboardValue),
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(label)
                    }
                    append(text = " $deviceClipboardValue")
                },
                color = Color.White,
                overflow = TextOverflow.Ellipsis,
                maxLines = if (textExpanded) Int.MAX_VALUE else 2
            )
        }
        PasteDeviceClipboardValueToMcbIcon(onPasteValueToMcb, deviceClipboardValue)
    }
}

@Composable
private fun PasteDeviceClipboardValueToMcbIcon(
    onPasteValueToMcb: (String) -> Unit,
    deviceClipboardValue: String
) {
    val transitionState by remember { mutableStateOf(MutableTransitionState(SingleStateWithTransitionAnimState.SteadyState)) }
    val transition = updateTransition(transitionState, label = "Copy from device clipboard to mcb icon transition")
    val scale by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 200, easing = LinearEasing) },
        label = "Copy from device clipboard to mcb icon scale"
    ) { targetState -> if (targetState == SingleStateWithTransitionAnimState.Transitioning) 2f else 1f }
    if (transitionState.currentState == SingleStateWithTransitionAnimState.Transitioning) {
        transitionState.targetState = SingleStateWithTransitionAnimState.SteadyState
    }
    IconButton(
        onClick = {
            onPasteValueToMcb(deviceClipboardValue)
            transitionState.targetState = SingleStateWithTransitionAnimState.Transitioning
        }
    ) {
        Icon(
            modifier = Modifier.scale(scale),
            painter = painterResource(R.drawable.ic_baseline_content_paste_24),
            tint = MaterialTheme.colors.onSecondary,
            contentDescription = stringResource(R.string.paste_value_from_device_clipboard_cd)
        )
    }
}
