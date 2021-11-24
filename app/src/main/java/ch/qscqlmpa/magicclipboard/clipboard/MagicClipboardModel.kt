package ch.qscqlmpa.magicclipboard.clipboard

import java.time.LocalDateTime
import java.util.*

data class McbItem(
    val id: McbItemId,
    val value: String,
    val creationDate: LocalDateTime = LocalDateTime.now()
)

@JvmInline
value class McbItemId(val id: UUID)
