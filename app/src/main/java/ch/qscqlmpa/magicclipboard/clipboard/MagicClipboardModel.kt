package ch.qscqlmpa.magicclipboard.clipboard

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import java.time.LocalDateTime
import java.util.*

data class McbItem(
    val id: McbItemId = McbItemId(UUID.randomUUID()),
    val value: String,
    val creationDate: LocalDateTime = LocalDateTime.now(),
    val favorite: Boolean = false
) {
    val valueAsQrCode: Bitmap by lazy {
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(value, BarcodeFormat.QR_CODE, 1024, 1024)
        val bitmap = Bitmap.createBitmap(bitMatrix.width, bitMatrix.height, Bitmap.Config.RGB_565)
        for (x in 0 until bitMatrix.width) {
            for (y in 0 until bitMatrix.height) {
                bitmap.setPixel(x, y, if (bitMatrix.get(x, y)) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        bitmap
    }
}

@JvmInline
value class McbItemId(val value: UUID)
