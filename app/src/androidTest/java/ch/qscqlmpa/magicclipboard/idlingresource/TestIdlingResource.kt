package ch.qscqlmpa.magicclipboard.idlingresource

import androidx.test.espresso.idling.CountingIdlingResource
import org.tinylog.Logger

class TestIdlingResource(resourceName: String) : McbIdlingResource {

    private val resource = CountingIdlingResource(resourceName)

    override fun isIdleNow() = resource.isIdleNow

    override fun increment(reason: String) {
        Logger.debug("Increment counter: $reason")
        resource.increment()
    }

    override fun decrement(reason: String) {
        Logger.debug("Decrement counter: $reason")
        resource.decrement()
    }
}
