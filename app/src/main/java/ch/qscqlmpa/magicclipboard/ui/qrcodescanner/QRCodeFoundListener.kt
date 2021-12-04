package ch.qscqlmpa.magicclipboard.ui.qrcodescanner

interface QRCodeFoundListener {
    fun onQRCodeFound(data: String)
    fun errorDecodingQrCode(qrCodeDecodingException: QrCodeDecodingException) {
        // Nothing to do
    }
}
