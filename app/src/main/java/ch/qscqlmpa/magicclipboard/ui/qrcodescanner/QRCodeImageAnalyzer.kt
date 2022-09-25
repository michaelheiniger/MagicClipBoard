package ch.qscqlmpa.magicclipboard.ui.qrcodescanner

import android.graphics.ImageFormat
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.zxing.BinaryBitmap
import com.google.zxing.ChecksumException
import com.google.zxing.FormatException
import com.google.zxing.NotFoundException
import com.google.zxing.PlanarYUVLuminanceSource
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.multi.qrcode.QRCodeMultiReader
import org.tinylog.Logger

class QRCodeImageAnalyzer(
    private val listener: QRCodeFoundListener
) : ImageAnalysis.Analyzer {

    @Suppress("SwallowedException")
    override fun analyze(image: ImageProxy) {
        if (image.format == ImageFormat.YUV_420_888 ||
            image.format == ImageFormat.YUV_422_888 ||
            image.format == ImageFormat.YUV_444_888
        ) {
            val byteBuffer = image.planes[0].buffer
            val imageData = ByteArray(byteBuffer.capacity())
            byteBuffer[imageData]
            val source = PlanarYUVLuminanceSource(
                imageData,
                image.width, image.height,
                0, 0,
                image.width, image.height,
                false
            )
            val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
            try {
                val result = QRCodeMultiReader().decode(binaryBitmap)
                listener.onQRCodeFound(result.text)
            } catch (e: FormatException) {
                Logger.error(e) { "Error decoding QR code." }
                listener.errorDecodingQrCode(QrCodeDecodingException.FormatException)
            } catch (e: ChecksumException) {
                Logger.error(e) { "Error decoding QR code." }
                listener.errorDecodingQrCode(QrCodeDecodingException.ChecksumException)
            } catch (e: NotFoundException) {
                // Nothing to do, keep scanning
            }
        }
        image.close()
    }
}

sealed class QrCodeDecodingException {

    /**
     * QR code could be successfully decoded but doesn't have the expected data structure defined by the generic type T.
     */
    object InvalidQrCodeException : QrCodeDecodingException()

    /**
     * QR code couldn't not be successfully decoded: wrong format.
     */
    object FormatException : QrCodeDecodingException()

    /**
     * QR code couldn't not be successfully decoded: wrong checksum.
     */
    object ChecksumException : QrCodeDecodingException()
}
