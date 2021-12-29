package ch.qscqlmpa.magicclipboard.ui.magicclipboard

import android.content.Intent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import ch.qscqlmpa.magicclipboard.R
import ch.qscqlmpa.magicclipboard.clipboard.McbItem
import ch.qscqlmpa.magicclipboard.clipboard.McbItemId
import ch.qscqlmpa.magicclipboard.ui.common.InfoDialog
import ch.qscqlmpa.magicclipboard.ui.common.UiTags
import java.time.LocalDateTime

@Composable
fun NoClipboardItems(
    text: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = 4.dp
    ) {
        Column(modifier.padding(horizontal = 8.dp, vertical = 8.dp)) {
            Text(stringResource(text))
        }
    }
}

@Composable
fun ClipboardItemList(
    items: List<McbItem>,
    currentDateTime: LocalDateTime,
    newItemsAdded: Boolean,
    onDeleteItem: (McbItemId) -> Unit,
    onItemFavoriteToggle: (McbItem) -> Unit,
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
                onItemFavoriteToggle = onItemFavoriteToggle,
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
    onItemFavoriteToggle: (McbItem) -> Unit,
    onPasteItemToDeviceClipboard: (McbItem) -> Unit
) {
    val delete by rememberUpdatedState { onDeleteItem(item.id) }
    val toggleFavorite by rememberUpdatedState { onItemFavoriteToggle(item) }
    val dismissState = rememberDismissState(
        confirmStateChange = {
            when (it) {
                DismissValue.DismissedToStart -> delete()
                DismissValue.DismissedToEnd -> toggleFavorite()
                else -> {
                    // Nothing to do
                }
            }
            it != DismissValue.DismissedToEnd
        }
    )

    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
        dismissThresholds = { direction ->
            FractionalThreshold(if (direction == DismissDirection.StartToEnd) 0.25f else 0.5f)
        },

        background = {
            val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
            val color by animateColorAsState(
                when (dismissState.targetValue) {
                    DismissValue.Default -> Color.LightGray
                    DismissValue.DismissedToEnd -> Color.Green
                    DismissValue.DismissedToStart -> Color.Red
                }
            )
            val arrangement = when (direction) {
                DismissDirection.StartToEnd -> Arrangement.Start
                DismissDirection.EndToStart -> Arrangement.End
            }
            val text = when (direction) {
                DismissDirection.StartToEnd -> if (item.favorite) R.string.remove_from_favorites else R.string.add_to_favorites
                DismissDirection.EndToStart -> R.string.delete_item
            }
            val icon = when (direction) {
                DismissDirection.StartToEnd -> if (item.favorite) Icons.Default.FavoriteBorder else Icons.Default.Favorite
                DismissDirection.EndToStart -> Icons.Default.Delete
            }
            val iconTint = when (direction) {
                DismissDirection.StartToEnd -> Color.Red
                DismissDirection.EndToStart -> Color.White
            }
            val scale by animateFloatAsState(
                if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
            )

            Row(
                horizontalArrangement = arrangement,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(horizontal = 20.dp)
            ) {
                if (direction == DismissDirection.EndToStart) {
                    Text(
                        text = stringResource(text),
                        color = Color.White,
                        fontSize = 20.sp,
                        modifier = Modifier.scale(scale)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                }
                Icon(
                    imageVector = icon,
                    tint = iconTint,
                    contentDescription = stringResource(R.string.delete_item_cd),
                    modifier = Modifier.scale(scale)
                )
                if (direction == DismissDirection.StartToEnd) {
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(
                        text = stringResource(text),
                        color = Color.White,
                        fontSize = 20.sp,
                        modifier = Modifier.scale(scale)
                    )
                }
            }
        },
        dismissContent = {
            ClipboardItemContent(
                item = item,
                currentDateTime = currentDateTime,
                sliding = dismissState.dismissDirection != null,
                onItemFavoriteToggle = onItemFavoriteToggle,
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
    onItemFavoriteToggle: (McbItem) -> Unit,
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
        var expanded by remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(Modifier.weight(1f)) {
                    Text(
                        modifier = Modifier.testTag(clipboardItemCreationDateTag(item)),
                        text = formatClipBoardDate(item.creationDate, currentDateTime),
                        fontSize = 10.sp
                    )
                    Text(
                        modifier = Modifier.testTag(clipboardItemValueTag(item))
                            .clickable { expanded = !expanded }
                            .animateContentSize(
                                animationSpec = spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessLow,
                                )
                            ),
                        text = item.value,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = if (expanded) Int.MAX_VALUE else 3
                    )
                }
                FavoriteIcon(item, onItemFavoriteToggle)
            }
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
                    ContextCompat.startActivity(context, intent, null)
                }) {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        tint = Color.Green,
                        contentDescription = stringResource(R.string.share_clipboard_item_cd)
                    )
                }
                CopyToDeviceClipboardIcon(item, onPasteItemToDeviceClipboard)
            }
        }
    }

    if (showQrCodeModal) {
        ShowQrCodeModal(item, onCloseClick = { showQrCodeModal = false })
    }
}

@Composable
private fun CopyToDeviceClipboardIcon(
    item: McbItem,
    onPasteItemToDeviceClipboard: (McbItem) -> Unit
) {
    val transitionState by remember { mutableStateOf(MutableTransitionState(FavoriteIconAnimState.SteadyState)) }
    val transition = updateTransition(transitionState, label = "Copy to device icon transition")
    val scale by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 200, easing = LinearEasing) },
        label = "Copy to device icon scale"
    ) { targetState -> if (targetState == FavoriteIconAnimState.Transitioning) 2f else 1f }
    if (transitionState.currentState == FavoriteIconAnimState.Transitioning) {
        transitionState.targetState = FavoriteIconAnimState.SteadyState
    }
    IconButton(
        modifier = Modifier.testTag("${UiTags.clipboardItem}_${item.id.value}_copyToDevice").scale(scale),
        onClick = {
            onPasteItemToDeviceClipboard(item)
            transitionState.targetState = FavoriteIconAnimState.Transitioning
        }
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_baseline_content_copy_24),
            tint = MaterialTheme.colors.primary,
            contentDescription = stringResource(R.string.load_clipboard_item_into_device_clipboard_cd)
        )
    }
}

private enum class FavoriteIconAnimState { SteadyState, Transitioning }

@Composable
private fun FavoriteIcon(
    item: McbItem,
    onItemFavoriteToggle: (McbItem) -> Unit
) {
    val transitionState by remember { mutableStateOf(MutableTransitionState(FavoriteIconAnimState.SteadyState)) }
    val transition: Transition<FavoriteIconAnimState> = updateTransition(transitionState, label = "Favorite icon transition")
    val scale by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 300, easing = LinearEasing) },
        label = "Favorite icon scale"
    ) { targetState -> if (targetState == FavoriteIconAnimState.Transitioning) 3f else 1f }
    val rotation by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 300, easing = LinearEasing) },
        label = "Favorite icon rotation"
    ) { targetState ->
        when (targetState) {
            FavoriteIconAnimState.SteadyState -> 0f
            FavoriteIconAnimState.Transitioning -> -360f
        }
    }
    if (transitionState.currentState == FavoriteIconAnimState.Transitioning) {
        transitionState.targetState = FavoriteIconAnimState.SteadyState
    }
    val favoriteCd = if (item.favorite) R.string.remove_from_favorites else R.string.add_to_favorites
    IconButton(onClick = {
        onItemFavoriteToggle(item)
        transitionState.targetState = FavoriteIconAnimState.Transitioning
    }) {
        Icon(
            modifier = Modifier.scale(scale).rotate(rotation),
            imageVector = if (item.favorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            tint = Color.Red,
            contentDescription = stringResource(favoriteCd)
        )
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
