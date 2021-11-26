package ch.qscqlmpa.magicclipboard.clipboard

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import ch.qscqlmpa.magicclipboard.data.store.local.LocalStore

class ClipboardUsecases(
    private val context: Context,
    private val clipboardManager: ClipboardManager,
    private val localStore: LocalStore
) {

    fun pasteItemIntoDeviceClipboard(item: McbItem) {
        clipboardManager.setPrimaryClip(ClipData.newPlainText(item.label, item.value))
    }

    suspend fun pasteItemFromDeviceClipboard(): McbItemId? {
        val clip = clipboardManager.primaryClip
        return if (clip != null) {
            val clipItem = clip.getItemAt(0)
            val item = McbItem(
                label = "", // TODO: What value should be set for the label ?!?
                value = clipItem.coerceToText(context).toString()
            )
            localStore.addItem(item)
            item.id
        } else null
    }
}
