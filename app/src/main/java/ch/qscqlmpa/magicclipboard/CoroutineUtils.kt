package ch.qscqlmpa.magicclipboard

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun CoroutineScope.launch(
    beforeLaunch: () -> Unit = {},
    onCompletion: () -> Unit = {},
    context: CoroutineContext = EmptyCoroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    block: suspend CoroutineScope.() -> Unit
): Job {
    beforeLaunch()
    val job = this.launch(context, start, block)
    job.invokeOnCompletion { onCompletion() }
    return job
}
