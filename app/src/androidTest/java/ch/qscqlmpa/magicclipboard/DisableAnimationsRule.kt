package ch.qscqlmpa.magicclipboard

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.io.IOException

/**
 * Credits: Diogo Pires
 * https://medium.com/@diogopires_55864/disabling-android-animations-in-espresso-tests-automatically-e6cfd897e767
 */
class DisableAnimationsRule : TestRule {
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            @Throws(Throwable::class)
            override fun evaluate() {
                changeAnimationStatus(enable = false)
                try {
                    base.evaluate()
                } finally {
                    changeAnimationStatus(enable = true)
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun changeAnimationStatus(enable: Boolean = true) {
        with(UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())) {
            executeShellCommand(" $TRANSITION_ANIMATION_SCALE ${enable.toInt()}")
            executeShellCommand("$WINDOW_ANIMATION_SCALE ${enable.toInt()}")
            executeShellCommand("$ANIMATOR_DURATION ${enable.toInt()}")
        }
    }

    private fun Boolean.toInt(): Int {
        return if (this) 1 else 0
    }

    companion object {
        private const val TRANSITION_ANIMATION_SCALE = "settings put global transition_animation_scale"
        private const val WINDOW_ANIMATION_SCALE = "settings put global window_animation_sc"
        private const val ANIMATOR_DURATION = "settings put global animator_duration_scale"
    }
}
