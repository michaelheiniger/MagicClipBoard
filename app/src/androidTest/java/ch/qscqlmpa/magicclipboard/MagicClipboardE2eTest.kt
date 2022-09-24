package ch.qscqlmpa.magicclipboard

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.hasAnyDescendant
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.compose.ui.test.performScrollToKey
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import ch.qscqlmpa.magicclipboard.ui.common.UiTags
import org.junit.Test

// FIXME: fix tests and setup
@ExperimentalTestApi
class MagicClipboardE2eTest : BaseE2eTest() {

    @Test
    fun itemsShouldBeDisplayed() {
        // Some visible items
        testRule.onNodeWithTag(UiTags.loginAsIdentifiedUser).performClick()

        testRule.onNodeWithText("[item 1]").assertExists().assertIsDisplayed()
        testRule.onNodeWithText("[item 2] which is a bit longer").assertExists().assertIsDisplayed()
        testRule.onNodeWithText(
            """
            [item 3] which is a much longeeeeeeeeeeeeeeeeeer item
            bla bidi bla bla blaaaa didi dudu dada
            bla bidi bla bla blaaaa didi dudu dada
            bla bidi bla bla blaaaa didi dudu dada
            """.trimIndent()
        )
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
    fun itemsDeletedAreNoLongerShownInTheList() {
        testRule.onNodeWithText("[item 1]").assertExists().assertIsDisplayed()

        testRule.onNodeWithTag("${UiTags.clipboardItem}_${idItem1.value}")
            .performTouchInput { swipeLeft() }
            .assertDoesNotExist() // Required to somehow complete the swipe...

        testRule.onNodeWithText("[item 1]").assertDoesNotExist()
        testRule.onNodeWithTag(UiTags.snackbarText).assertExists()
    }

    @Test
    fun setDeviceClipboardAndPasteItInMagicClipboard() {
        // Check that there is only one item "item 3"
        testRule.onNodeWithTag(UiTags.clipboardItemList)
            .onChildren()
            .filter(hasAnyDescendant(hasText(text = "[item 3]", substring = true, ignoreCase = true)))
            .assertCountEquals(1)

        // Copy item 3 into device clipboard
        testRule.onNodeWithTag("${UiTags.clipboardItem}_${idItem3.value}_copyToDevice").performClick()
        testRule.onNodeWithTag(UiTags.snackbarText)
            .assertExists()
            .assertTextContains(getString(R.string.item_pasted_in_device_clipboard))
        testRule.onNodeWithTag(UiTags.snackbarAction).performClick()

        // Paste value of device clipboard into MagicClipboard
        testRule.onNodeWithTag(UiTags.mainFab).performClick()

        testRule.onNodeWithTag(UiTags.pasteFromDeviceClipboard).performClick()

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
