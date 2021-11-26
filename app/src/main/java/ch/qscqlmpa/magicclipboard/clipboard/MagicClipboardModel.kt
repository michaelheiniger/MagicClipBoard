package ch.qscqlmpa.magicclipboard.clipboard

import java.time.LocalDateTime
import java.util.*

data class McbItem(
    val id: McbItemId = McbItemId(UUID.randomUUID()),

    /**
     * Describes the value (see https://developer.android.com/reference/android/content/ClipData)
     */
    val label: String,

    val value: String,
    val creationDate: LocalDateTime = LocalDateTime.now()
)

@JvmInline
value class McbItemId(val value: UUID)
