package ch.qscqlmpa.magicclipboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import ch.qscqlmpa.magicclipboard.ui.MagicClipboard
import ch.qscqlmpa.magicclipboard.ui.theme.MagicClipBoardTheme
import org.koin.android.ext.android.get

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MagicClipBoardTheme {
                MagicClipboard(this, screenNavigator = application.get())
            }
        }
    }
}
