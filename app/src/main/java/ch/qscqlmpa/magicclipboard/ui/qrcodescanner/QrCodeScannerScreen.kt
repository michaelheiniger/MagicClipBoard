package ch.qscqlmpa.magicclipboard.ui.qrcodescanner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ch.qscqlmpa.magicclipboard.R
import ch.qscqlmpa.magicclipboard.ui.common.InfoDialog
import ch.qscqlmpa.magicclipboard.ui.common.YesNoDialog
import ch.qscqlmpa.magicclipboard.ui.theme.MagicClipBoardTheme

@Composable
fun QrCodeScannerTopScreen() {
    MagicClipBoardTheme {
        Scaffold { innerPadding ->
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                Text(stringResource(R.string.scan_qr_code_hint))
            }
        }
    }
}

@Composable
fun QrCodeScannerBottomScreen(
    showPermissionDenied: State<Boolean>,
    showPermissionHint: State<Boolean>,
    showQrCodeDecodingFailed: State<Boolean>,
    showError: State<Boolean>,
    onCameraPermissionDeniedNoClick: () -> Unit,
    onCameraPermissionDeniedYesClick: () -> Unit,
    onCameraPermissionHintOkClick: () -> Unit,
    onCameraQrCodeDecodingFailedTryAgainNoClick: () -> Unit,
    onCameraQrCodeDecodingFailedTryAgainYesClick: () -> Unit,
    onStartingCameraErrorOkClick: () -> Unit
) {
    MagicClipBoardTheme {
        Scaffold {
            if (showPermissionDenied.value) {
                YesNoDialog(
                    text = R.string.camera_permission_denied,
                    onNoClick = onCameraPermissionDeniedNoClick,
                    onYesClick = onCameraPermissionDeniedYesClick
                )
            }
            if (showPermissionHint.value) {
                InfoDialog(
                    text = R.string.qr_code_camera_permission_hint,
                    onCloseClick = onCameraPermissionHintOkClick
                ) {}
            }
            if (showQrCodeDecodingFailed.value) {
                YesNoDialog(
                    text = R.string.qr_code_decoding_failed,
                    onNoClick = onCameraQrCodeDecodingFailedTryAgainNoClick,
                    onYesClick = onCameraQrCodeDecodingFailedTryAgainYesClick
                )
            }
            if (showError.value) {
                InfoDialog(
                    text = R.string.error_starting_camera,
                    onCloseClick = onStartingCameraErrorOkClick
                ) {}
            }
        }
    }
}
