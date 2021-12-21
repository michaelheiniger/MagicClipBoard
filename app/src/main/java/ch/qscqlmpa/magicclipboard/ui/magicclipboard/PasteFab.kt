package ch.qscqlmpa.magicclipboard.ui.magicclipboard

import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ch.qscqlmpa.magicclipboard.R
import ch.qscqlmpa.magicclipboard.ui.common.InfoDialog
import ch.qscqlmpa.magicclipboard.ui.common.UiTags
import ch.qscqlmpa.magicclipboard.ui.components.multifab.MultiFabItem
import ch.qscqlmpa.magicclipboard.ui.components.multifab.MultiFabState
import ch.qscqlmpa.magicclipboard.ui.components.multifab.MultiFloatingActionButton
import ch.qscqlmpa.magicclipboard.ui.qrcodescanner.QrCodeScanResult
import ch.qscqlmpa.magicclipboard.ui.qrcodescanner.ScanQrCodeResultContract

@Composable
fun PasteFab(
    modifier: Modifier = Modifier,
    deviceClipboardValue: String?,
    onPasteFromDeviceClipboard: (String) -> Unit,
    onPasteFromQrCode: (String) -> Unit
) {
    fun buildFabItems(deviceClipboardValue: String?): List<ClipboardFabItem> {
        val fabItems = mutableListOf<ClipboardFabItem>(PasteFromQrCode)
        if (deviceClipboardValue != null) fabItems.add(0, PasteFromDeviceClipboard)
        return fabItems.toList()
    }

    var state by remember { mutableStateOf(MultiFabState.COLLAPSED) }
    val launcher = qrCodeScanner(onQrCodeScan = onPasteFromQrCode)

    Column(modifier = modifier) {
        MultiFloatingActionButton(
            mainFabText = R.string.paste,
            mainFabIcon = R.drawable.ic_baseline_content_paste_24,
            items = buildFabItems(deviceClipboardValue),
            targetState = state,
            stateChanged = { targetState -> state = targetState },
            onFabItemClicked = { item ->
                when (item) {
                    PasteFromDeviceClipboard -> if (deviceClipboardValue != null) onPasteFromDeviceClipboard(deviceClipboardValue)
                    PasteFromQrCode -> launcher.launch(Unit)
                }
                state = MultiFabState.COLLAPSED
            }
        )
    }
}

sealed class ClipboardFabItem(
    override val id: String,
    override val content: @Composable () -> Unit,
    override val label: Int
) : MultiFabItem

object PasteFromDeviceClipboard : ClipboardFabItem(
    id = UiTags.pasteFromDeviceClipboard,
    content = @Composable {
        ClipboardFabIcon(
            primaryIcon = R.drawable.ic_baseline_content_paste_24,
            secondaryIcon = R.drawable.ic_baseline_phone_android_24
        )
    },
    label = R.string.paste_from_device_clipboard
)

object PasteFromQrCode : ClipboardFabItem(
    id = UiTags.pasteFromQrCode,
    content = @Composable {
        ClipboardFabIcon(
            primaryIcon = R.drawable.ic_baseline_content_paste_24,
            secondaryIcon = R.drawable.ic_baseline_qr_code_24
        )
    },
    label = R.string.paste_from_qr_code
)

@Composable
private fun ClipboardFabIcon(
    primaryIcon: Int,
    secondaryIcon: Int
) {
    Box {
        Icon(
            painter = painterResource(primaryIcon),
            tint = Color.White,
            contentDescription = "", // TODO
        )
        Icon(
            painter = painterResource(secondaryIcon),
            tint = Color.Black,
            contentDescription = "", // TODO
            modifier = Modifier
                .scale(0.7f)
                .align(Alignment.BottomEnd)
                .offset(12.dp, 12.dp)
        )
    }
}

@Composable
private fun qrCodeScanner(
    onQrCodeScan: (String) -> Unit,
    onClose: () -> Unit = {}
): ManagedActivityResultLauncher<Unit, QrCodeScanResult> {
    val showErrorDialog = remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(ScanQrCodeResultContract()) { scanResult ->
        when (scanResult) {
            QrCodeScanResult.NoResult -> {
            } // Nothing to do
            QrCodeScanResult.Error -> showErrorDialog.value = true
            is QrCodeScanResult.Success -> onQrCodeScan(scanResult.value)
        }
        onClose()
    }

    if (showErrorDialog.value) {
        InfoDialog(
            text = R.string.error_scanning_qr_code,
            onCloseClick = { showErrorDialog.value = false }
        ) {}
    }
    return launcher
}
