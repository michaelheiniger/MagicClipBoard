package ch.qscqlmpa.magicclipboard.ui.magicclipboard

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.qscqlmpa.magicclipboard.R
import ch.qscqlmpa.magicclipboard.clipboard.McbItem
import ch.qscqlmpa.magicclipboard.debugClipBoardItems
import ch.qscqlmpa.magicclipboard.ui.MagicClipBoard
import ch.qscqlmpa.magicclipboard.ui.common.LoadingSpinner
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object ClipboardUtils {
    val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")
    val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
}

@Preview(showBackground = true)
@Composable
private fun ClipboardBodyPreview() {
    MagicClipBoard {
        MagicClipboardBody(clipBoardItemsState = McbItemsState.Success(debugClipBoardItems.toList()))
    }
}

@Composable
fun MagicClipboardScreen(viewModel: MagicClipboardVM) {
    MagicClipboardBody(clipBoardItemsState = viewModel.clipBoardItemsState.collectAsState().value)
}

@Composable
fun MagicClipboardBody(clipBoardItemsState: McbItemsState) {
    Column(modifier = Modifier.fillMaxSize()) {
        when (clipBoardItemsState) {
            McbItemsState.Loading -> LoadingSpinner()
            is McbItemsState.Success -> ClipboardItemList(clipBoardItemsState = clipBoardItemsState.items)
        }
    }
}

@Composable
private fun ClipboardItemList(clipBoardItemsState: List<McbItem>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize().animateContentSize()
    ) {
        if (clipBoardItemsState.isEmpty()) {
            item { Text(stringResource(R.string.no_clipboard_item)) }
        }
        items(clipBoardItemsState, key = { item -> item.id.value }) { item -> ClipboardItem(item) }
    }
}

@Composable
private fun ClipboardItem(item: McbItem) {
    val itemCd = stringResource(R.string.clipboard_item_cd, item.value) // Can't inline: composable
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
            .semantics { contentDescription = itemCd },
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 4.dp)
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

@Composable
private fun formatClipBoardDate(date: LocalDateTime): String {
    val now = LocalDateTime.now()
    return when {
        now.minusMinutes(1).isBefore(date) -> stringResource(R.string.clipboard_date_a_few_seconds_ago)
        now.minusMinutes(5).isBefore(date) -> stringResource(R.string.clipboard_date_a_few_minutes_ago)
        now.toLocalDate().isEqual(date.toLocalDate()) -> stringResource(
            R.string.clipboard_date_today_at,
            ClipboardUtils.timeFormatter.format(date)
        )
        else -> date.format(ClipboardUtils.dateTimeFormatter)
    }
}
