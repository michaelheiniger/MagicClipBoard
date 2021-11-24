package ch.qscqlmpa.magicclipboard.store.local

import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class InMemoryLocalStoreTest {

    private lateinit var localStore: LocalStore

    @BeforeEach
    fun setup() {
        localStore = InMemoryLocalStore(TestCoroutineDispatcher())
    }

    @Test
    fun `A new item can be added to the store`() = runBlockingTest {
        // Given
        assertThat(localStore.getItems()).isEmpty()

        // When
        val newItem1 = buildTestMcbItem1()
        localStore.addItem(newItem1)

        // Then
        assertThat(localStore.getItems()).hasSize(1)
        assertThat(localStore.getItems()).contains(newItem1)

        // When
        val newItem2 = buildTestMcbItem2()
        localStore.addItem(newItem2)

        // Then
        assertThat(localStore.getItems()).hasSize(2)
        assertThat(localStore.getItems()).contains(newItem1, newItem2)
    }

    @Test
    fun `An existing item can be deleted from the store`() = runBlockingTest {
        // Given
        val newItem1 = buildTestMcbItem1()
        val newItem2 = buildTestMcbItem2()
        val newItem3 = buildTestMcbItem3()
        localStore.addItem(newItem1)
        localStore.addItem(newItem2)
        localStore.addItem(newItem3)
        assertThat(localStore.getItems()).contains(newItem1, newItem2, newItem3)

        // When
        localStore.deleteItem(newItem2.id)

        // Then
        assertThat(localStore.getItems()).hasSize(2)
        assertThat(localStore.getItems()).contains(newItem1, newItem3)
    }

    @Test
    fun `The state of the store can be observed`() = runBlockingTest {
        // Given

        // When

        // Then
    }
}
