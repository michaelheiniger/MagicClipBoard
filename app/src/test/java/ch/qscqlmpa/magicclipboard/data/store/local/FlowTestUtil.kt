package ch.qscqlmpa.magicclipboard.data.store.local

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.assertj.core.api.Assertions.assertThat

// Credits: https://proandroiddev.com/from-rxjava-to-kotlin-flow-testing-42f1641d8433

fun <T> Flow<T>.test(scope: CoroutineScope): TestObserver<T> {
    return TestObserver(scope, this)
}

class TestObserver<T>(
    scope: CoroutineScope,
    flow: Flow<T>
) {
    private val valuesEmitted = mutableListOf<T>()

    private val job: Job = scope.launch {
        flow.collect { t -> valuesEmitted.add(t) }
    }

    fun assertNoValues(): TestObserver<T> {
        assertThat(valuesEmitted).isEmpty()
        return this
    }

    fun assertValues(vararg values: T): TestObserver<T> {
        assertThat(valuesEmitted).containsExactly(*values)
        return this
    }

    fun finish() {
        job.cancel()
    }
}
