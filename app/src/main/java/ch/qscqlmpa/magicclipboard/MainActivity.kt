package ch.qscqlmpa.magicclipboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import ch.qscqlmpa.magicclipboard.ui.MagicClipboard
import ch.qscqlmpa.magicclipboard.ui.theme.MagicClipBoardTheme
import org.koin.android.ext.android.get

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var darkTheme by remember { mutableStateOf(false) }
            MagicClipBoardTheme(darkTheme = darkTheme) {
                MagicClipboard(
                    lifecycleOwner = this,
                    screenNavigator = application.get(),
                    onToggleDarkTheme = { darkTheme = !darkTheme }
                )
            }
        }
    }
}
