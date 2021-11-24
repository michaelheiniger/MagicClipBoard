package ch.qscqlmpa.magicclipboard.store.local

import ch.qscqlmpa.magicclipboard.clipboard.McbItem
import ch.qscqlmpa.magicclipboard.clipboard.McbItemId
import java.util.*

fun buildTestMcbItem1(): McbItem = McbItem(id = McbItemId(id = UUID.randomUUID()), value = "value 1")
fun buildTestMcbItem2(): McbItem = McbItem(id = McbItemId(id = UUID.randomUUID()), value = "value 2")
fun buildTestMcbItem3(): McbItem = McbItem(id = McbItemId(id = UUID.randomUUID()), value = "value 3")
fun buildTestMcbItem4(): McbItem = McbItem(id = McbItemId(id = UUID.randomUUID()), value = "value 4")
fun buildTestMcbItem5(): McbItem = McbItem(id = McbItemId(id = UUID.randomUUID()), value = "value 5")
