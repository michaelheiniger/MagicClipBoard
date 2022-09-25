package ch.qscqlmpa.magicclipboard

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import ch.qscqlmpa.magicclipboard.ui.theme.MagicClipBoardTheme
import org.junit.Rule

abstract class BaseUiUnitTest {

    private val res = InstrumentationRegistry.getInstrumentation().targetContext.resources

    @get:Rule
    val testRule = createComposeRule() // Must be public

    protected fun getString(resourceId: Int) = res.getString(resourceId)

    protected fun launchTestWithContent(content: @Composable () -> Unit) {
        testRule.setContent {
            MagicClipBoardTheme {
                content()
            }
        }
    }
}
