package ch.qscqlmpa.magicclipboard

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
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeLeft
import ch.qscqlmpa.magicclipboard.ui.common.UiTags
import org.junit.Before
import org.junit.Test

class MagicClipboardE2eTest : BaseE2eTest() {

    @Before
    fun signIn() {
        signInWithEmulatorFirebaseUser1IfNeeded()
    }

    @Test
    fun itemsShouldBeDisplayed() {
        testRule.onNodeWithText(item1.value).assertExists().assertIsDisplayed()
        testRule.onNodeWithText(item2.value).assertExists().assertIsDisplayed()
    }

    @Test
    fun itemsDownTheListCanBeScrolledTo() {
        // Item not yet visible
        testRule.onNodeWithText(item20.value).assertDoesNotExist()

        // Scroll down to show last item
        testRule.onNodeWithTag(UiTags.clipboardItemList)
            .performScrollToKey(idItem20.value)
            .assertExists()
        testRule.onNodeWithText(item20.value).assertIsDisplayed()
    }

    @Test
    fun itemsDeletedAreNoLongerShownInTheList() {
        val itemToDelete = item2
        testRule.onNodeWithText(itemToDelete.value).assertExists().assertIsDisplayed()

        testRule.onNodeWithTag("${UiTags.clipboardItem}_${itemToDelete.id.value}")
            .performTouchInput { swipeLeft() }
            .assertDoesNotExist() // Somehow required to complete the swipe...

        testRule.onNodeWithText(itemToDelete.value).assertDoesNotExist()
    }

    @Test
    fun setDeviceClipboardAndPasteItInMagicClipboard() {
        // Check that there is only one item "item 3"
        testRule.onNodeWithTag(UiTags.clipboardItemList)
            .onChildren()
            .filter(hasAnyDescendant(hasText(item3.value)))
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

        // New item is at the top of the list (list is sorted on the creation date DESC)
        testRule.onNodeWithTag(UiTags.clipboardItemList).performScrollToIndex(0)

        // Check value of first item in the list
        testRule.onNodeWithTag(UiTags.clipboardItemList)
            .onChildren()
            .onFirst()
            .assert(hasAnyDescendant(hasText(item3.value)))

        // Check that there are now two items "item 3" in the list
        testRule.onNodeWithTag(UiTags.clipboardItemList)
            .onChildren()
            .filter(hasAnyDescendant(hasText(item3.value)))
            .assertCountEquals(2)

        // Delete fist item (cleanup)
        testRule.onNodeWithTag(UiTags.clipboardItemList)
            .onChildren()
            .onFirst()
            .performTouchInput { swipeLeft() }
    }

    private fun signInWithEmulatorFirebaseUser1IfNeeded() {
        val user = TestUserFactory.emulatorFirebaseUser1
        testRule.onNodeWithTag(UiTags.emailInput).performTextInput(user.email)
        testRule.onNodeWithTag(UiTags.passwordInput).performTextInput(user.password)
        testRule.onNodeWithTag(UiTags.signInWithEmailAndPassword).performClick()
    }
}
