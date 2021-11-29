package ch.qscqlmpa.magicclipboard

import androidx.compose.ui.test.*
import ch.qscqlmpa.magicclipboard.ui.common.UiTags
import org.junit.Test

@ExperimentalTestApi
class MagicClipboardE2eTest : BaseE2eTest() {

    @Test
    fun itemsShouldBeDisplayed() {
        // Some visible items
        testRule.onNodeWithText("[item 1]").assertExists().assertIsDisplayed()
        testRule.onNodeWithText("[item 2] which is a bit longer").assertExists().assertIsDisplayed()
        testRule.onNodeWithText("[item 3] which is a much longeeeeeeeeeeeeeeeeeer item\n bla bidi bla bla blaaaa didi dudu dada\n bla bidi bla bla blaaaa didi dudu dada\n bla bidi bla bla blaaaa didi dudu dada")
            .assertExists().assertIsDisplayed()
    }

    @Test
    fun itemsDownTheListCanBeScrolledTo() {
        // Item not yet visible
        testRule.onNodeWithText("[item 20]").assertDoesNotExist()

        // Scroll down to show last item
        testRule.onNodeWithTag(UiTags.clipboardItemList)
            .performScrollToKey(idItem20.value)
            .assertIsDisplayed()
        testRule.onNodeWithText("[item 20]").assertIsDisplayed()
    }

    @Test
    fun setDeviceClipboardAndPasteItInMagicClipboard() {
        // Check that there is only one item "item 3"
        testRule.onNodeWithTag(UiTags.clipboardItemList)
            .onChildren()
            .filter(hasAnyDescendant(hasText(text = "[item 3]", substring = true, ignoreCase = true)))
            .assertCountEquals(1)

        // Copy item 3 into device clipboard
        testRule.onNodeWithTag("${UiTags.clipboardItem}_${idItem3.value}_copyToDevice")
            .performClick()

        // Paste value of device clipboard into MagicClipboard
        testRule.onNodeWithTag(UiTags.pasteToMagicClipboard).performClick()

        // New item is at the top of the list (list is sorted on creationDate DESC)
        testRule.onNodeWithTag(UiTags.clipboardItemList).performScrollToIndex(0)

        // Check value of first item in the list
        testRule.onNodeWithTag(UiTags.clipboardItemList)
            .onChildren()
            .onFirst()
            .assert(hasAnyDescendant(hasText(text = "[item 3]", substring = true, ignoreCase = true)))

        // Check that there are now two items "item 3" in the list
        testRule.onNodeWithTag(UiTags.clipboardItemList)
            .onChildren()
            .filter(hasAnyDescendant(hasText(text = "[item 3]", substring = true, ignoreCase = true)))
            .assertCountEquals(2)
    }
}
