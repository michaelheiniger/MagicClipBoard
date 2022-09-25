package ch.qscqlmpa.magicclipboard.idlingresource

import ch.qscqlmpa.magicclipboard.BuildConfig
import org.tinylog.Logger

class StubIdlingResource : McbIdlingResource {
    override fun isIdleNow(): Boolean {
        return true
    }

    override fun increment(reason: String) {
        // Nothing to do: this is the production implementation
        if (BuildConfig.DEBUG) Logger.debug { "Increment counter: $reason" }
    }

    override fun decrement(reason: String) {
        // Nothing to do: this is the production implementation
        if (BuildConfig.DEBUG) Logger.debug { "Decrement counter: $reason" }
    }
}
