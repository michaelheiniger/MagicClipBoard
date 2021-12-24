package ch.qscqlmpa.magicclipboard.ui.magicclipboard

import androidx.compose.ui.test.*
import ch.qscqlmpa.magicclipboard.*
import ch.qscqlmpa.magicclipboard.clipboard.McbItem
import ch.qscqlmpa.magicclipboard.clipboard.McbItemId
import ch.qscqlmpa.magicclipboard.ui.common.UiTags
import java.time.LocalDateTime
import org.junit.Test

internal class ClipboardScreenTest : BaseUiUnitTest() {

    private var items: List<McbItem> = emptyList()
    private var newItemsAdded = false
    private var messages: List<Message> = emptyList()
    private var deviceClipboardValue: String = "Winter is coming !"
    private var username: String = "Ned Stark"

    private var deletedItem: McbItemId? = null
    private var pastedValueFromDeviceClipboard: String? = null
    private var pastedItemToDeviceClipboard: McbItem? = null
    private var pastedValueFromQrCode: String? = null
    private var dismissedMessageId: Long? = null
    private var signedOutClicked: Boolean = false

    @Test
    fun withoutItems() {
        // Given
        items = emptyList()
        deviceClipboardValue = "Winter is coming !"
        username = "Ned Stark"

        // When
        launchTest()

        // Then
        testRule.onNodeWithText(getString(R.string.no_clipboard_item)).assertIsDisplayed()
        assertDeviceClipboardPanel()
        assertToolbarContent()
        assertPasteFab()
    }

    @Test
    fun withItems() {
        // Given
        items = clipBoardItems
        deviceClipboardValue = "Winter is coming !"
        username = "Ned Stark"

        // When
        launchTest()

        // Then
        testRule.onNodeWithText(getString(R.string.no_clipboard_item)).assertDoesNotExist()
        testRule.onNodeWithText("[item 1]", substring = true).assertIsDisplayed()
        testRule.onNodeWithText("[item 2]", substring = true).assertIsDisplayed()
        testRule.onNodeWithText("[item 3]", substring = true).assertIsDisplayed()
        assertDeviceClipboardPanel()
        assertToolbarContent()
        assertPasteFab()
    }

    @Test
    fun itemValueIsDisplayed() {
        // Given
        items = clipBoardItems

        // When
        launchTest()

        // Then
        assertItemValue(item1.id, "[item 1]")
        assertItemValue(item2.id, "[item 2] which is a bit longer")
        assertItemValue(
            item3.id,
            "[item 3] which is a much longeeeeeeeeeeeeeeeeeer item\n bla bidi bla bla blaaaa didi dudu dada\n bla bidi bla bla blaaaa didi dudu dada\n bla bidi bla bla blaaaa didi dudu dada"
        )
        assertItemValue(item4.id, "[item 4]")
    }

    @Test
    fun itemDateTimeIsProperlyFormatted() {
        // Given
        items = clipBoardItems

        // When
        launchTest()

        // Then
        assertItemDateTime(item1, getString(R.string.clipboard_date_a_few_seconds_ago))
        assertItemDateTime(item2, getString(R.string.clipboard_date_a_few_minutes_ago))
        assertItemDateTime(item3, "Today at 13:45:24")
        assertItemDateTime(item5, "26.07.1989 19:55:00")
    }

    private fun assertItemValue(itemId: McbItemId, expectedString: String) {
        testRule.onNodeWithTag(clipboardItemValueTag(itemId))
            .assertTextEquals(expectedString)
            .assertIsDisplayed()
    }

    private fun assertItemDateTime(item: McbItem, expectedString: String) {
        testRule.onNodeWithTag(clipboardItemCreationDateTag(item.id))
            .assertTextEquals(expectedString)
            .assertIsDisplayed()
    }

    private fun assertDeviceClipboardPanel() {
        testRule.onNodeWithTag(UiTags.deviceClipboardValue)
            .assertTextContains("Winter is coming !", substring = true)
            .assertIsDisplayed()
    }

    private fun assertToolbarContent() {
        testRule.onNodeWithText("Ned Stark").assertIsDisplayed()
        testRule.onNodeWithText(getString(R.string.signOut)).assertIsDisplayed()
    }

    private fun assertPasteFab() {
        testRule.onNodeWithTag(UiTags.mainFab)
            .assertTextContains(getString(R.string.paste))
            .assertIsDisplayed()
    }

    private fun launchTest() {
        launchTestWithContent {
            ClipboardBody(
                uiState = MagicClipboardUiState(
                    currentDateTime = LocalDateTime.now(),
                    items = items,
                    newItemsAdded = newItemsAdded,
                    messages = messages,
                    deviceClipboardValue = deviceClipboardValue,
                    username = username,
                    newClipboardValue = null
                ),
                onDeleteItem = { deletedItem = it },
                onPasteItemToDeviceClipboard = { pastedItemToDeviceClipboard = it },
                onPasteValueToMcb = { pastedValueFromDeviceClipboard = it },
                onPasteFromQrCode = { pastedValueFromQrCode = it },
                onDismissNewClipboardValue = {},
                onMessageDismissState = { dismissedMessageId = it },
                onSignOut = { signedOutClicked = true }
            )
        }
    }
}
