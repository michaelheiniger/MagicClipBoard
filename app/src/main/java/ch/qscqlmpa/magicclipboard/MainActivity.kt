package ch.qscqlmpa.magicclipboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import ch.qscqlmpa.magicclipboard.data.UserPreferences
import ch.qscqlmpa.magicclipboard.ui.MagicClipboard
import ch.qscqlmpa.magicclipboard.ui.theme.MagicClipBoardTheme
import org.koin.android.ext.android.get

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val userPreferences = application.get<UserPreferences>()
            val coroutineScope = rememberCoroutineScope()
            MagicClipBoardTheme(darkTheme = userPreferences.darkThemeEnabled().collectAsState(false).value) {
                MagicClipboard(
                    lifecycleOwner = this,
                    screenNavigator = application.get(),
                    onToggleDarkTheme = { coroutineScope.launch { userPreferences.toggleDarkTheme() } }
                )
            }
        }
    }
}
