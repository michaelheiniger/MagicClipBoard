package ch.qscqlmpa.magicclipboard.store.local

import ch.qscqlmpa.magicclipboard.clipboard.McbItem
import ch.qscqlmpa.magicclipboard.clipboard.McbItemId
import java.time.LocalDateTime
import java.util.*

fun buildTestMcbItem1(creationDate: LocalDateTime = LocalDateTime.now()): McbItem = McbItem(
    id = McbItemId(value = UUID.randomUUID()),
    value = "value 1",
    creationDate = creationDate
)

fun buildTestMcbItem2(creationDate: LocalDateTime = LocalDateTime.now()): McbItem = McbItem(
    id = McbItemId(value = UUID.randomUUID()),
    value = "value 2",
    creationDate = creationDate
)

fun buildTestMcbItem3(creationDate: LocalDateTime = LocalDateTime.now()): McbItem = McbItem(
    id = McbItemId(value = UUID.randomUUID()),
    value = "value 3",
    creationDate = creationDate
)

fun buildTestMcbItem4(creationDate: LocalDateTime = LocalDateTime.now()): McbItem = McbItem(
    id = McbItemId(value = UUID.randomUUID()),
    value = "value 4",
    creationDate = creationDate
)

fun buildTestMcbItem5(creationDate: LocalDateTime = LocalDateTime.now()): McbItem = McbItem(
    id = McbItemId(value = UUID.randomUUID()),
    value = "value 5",
    creationDate = creationDate
)
