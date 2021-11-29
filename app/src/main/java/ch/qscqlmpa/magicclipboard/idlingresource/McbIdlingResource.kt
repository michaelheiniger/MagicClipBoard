package ch.qscqlmpa.magicclipboard.idlingresource

/**
 * Counter representing the number of async ongoing operations.
 * Call [increment] to increment the counter: Compose UI checks are paused.
 * Call [decrement] to decrease the counter: if [decrement] has been called as many times as [increment],
 * Compose UI checks are resumed.
 * Notes: - [decrement] must always be called after a matching increment.
 *        - [decrement] must eventually be called otherwise the test is going to fail (timeout).
 */
interface McbIdlingResource {
    fun isIdleNow(): Boolean
    fun increment(reason: String)
    fun decrement(reason: String)
}
