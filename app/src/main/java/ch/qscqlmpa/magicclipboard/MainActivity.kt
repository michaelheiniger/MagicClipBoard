package ch.qscqlmpa.magicclipboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.DisposableEffect
import ch.qscqlmpa.magicclipboard.ui.MagicClipBoard
import ch.qscqlmpa.magicclipboard.ui.magicclipboard.MagicClipboardScreen
import ch.qscqlmpa.magicclipboard.ui.magicclipboard.MagicClipboardVM
import org.koin.androidx.compose.viewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MagicClipBoard {
                val viewModel by viewModel<MagicClipboardVM>()
                DisposableEffect(viewModel) {
                    viewModel.onStart()
                    onDispose { viewModel.onStop() }
                }
                MagicClipboardScreen(viewModel)
            }
        }
    }
}
