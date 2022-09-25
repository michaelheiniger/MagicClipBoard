package ch.qscqlmpa.magicclipboard.clipboard.usecases

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import ch.qscqlmpa.magicclipboard.clipboard.McbItem

class DeviceClipboardUsecases(
    private val context: Context,
    private val clipboardManager: ClipboardManager
) {

    fun pasteItemIntoDeviceClipboard(item: McbItem) {
        clipboardManager.setPrimaryClip(ClipData.newPlainText("", item.value))
    }

    fun getDeviceClipboardValue(): String? {
        val clip = clipboardManager.primaryClip
        return if (clip != null) {
            val clipItem = clip.getItemAt(0)
            clipItem.coerceToText(context).toString()
        } else null
    }
}
