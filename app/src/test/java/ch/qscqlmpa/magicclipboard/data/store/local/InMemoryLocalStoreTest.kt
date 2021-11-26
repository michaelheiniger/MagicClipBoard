package ch.qscqlmpa.magicclipboard.data.store.local

import ch.qscqlmpa.magicclipboard.clipboard.McbItem
import ch.qscqlmpa.magicclipboard.data.Result
import ch.qscqlmpa.magicclipboard.data.data
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

internal class InMemoryLocalStoreTest {

    private lateinit var localStore: LocalStore

    private val oneHourAgo = LocalDateTime.now().minusHours(1)
    private val twoHoursAgo = LocalDateTime.now().minusHours(2)
    private val threeHoursAgo = LocalDateTime.now().minusHours(3)
    private val newItem1 = buildTestMcbItem1(creationDate = oneHourAgo)
    private val newItem2 = buildTestMcbItem2(creationDate = twoHoursAgo)
    private val newItem3 = buildTestMcbItem3(creationDate = threeHoursAgo)

    private fun setup(initialItems: Set<McbItem>) {
        localStore = InMemoryLocalStore(ioDispatcher = TestCoroutineDispatcher(), initialItems)
    }

    @Nested
    inner class AddingItem {
        @Test
        fun `A new item can be added to the store`() = runBlockingTest {
            // Given
            setup(initialItems = setOf())
            assertThat(localStore.getItems().data()).isEmpty()

            // When
            localStore.addItem(newItem1)

            // Then
            assertThat(localStore.getItems().data()).hasSize(1)
            assertThat(localStore.getItems().data()).contains(newItem1)

            // When
            localStore.addItem(newItem2)

            // Then
            assertThat(localStore.getItems().data()).hasSize(2)
            assertThat(localStore.getItems().data()).contains(newItem1, newItem2)
        }
    }

    @Nested
    inner class DeletingItem {
        @Test
        fun `An existing item can be deleted from the store`() = runBlockingTest {
            // Given
            setup(initialItems = setOf(newItem1, newItem2, newItem3))
            assertThat(localStore.getItems().data()).contains(newItem1, newItem2, newItem3)

            // When
            assertThat(localStore.deleteItem(newItem2.id)).isInstanceOf(Result.Success::class.java)

            // Then
            assertThat(localStore.getItems().data()).hasSize(2)
            assertThat(localStore.getItems().data()).contains(newItem1, newItem3)
        }
    }

    @Nested
    inner class GettingItemsFromStore {
        @Test
        fun `The store returns its items sorted on the creation date in an descending order`() = runBlockingTest {
            // Given
            setup(initialItems = setOf(newItem3, newItem1, newItem2))

            // When
            val returnedItems = localStore.getItems().data()

            // Then
            assertThat(returnedItems).containsExactly(newItem1, newItem2, newItem3)
        }
    }

    @Nested
    inner class ObserveStateOfStore {
        @Test
        fun `The state of the store is emitted when an item is added`() = runBlockingTest {
            // Given
            setup(initialItems = setOf())
            val observer = localStore.observeItems().test(this)

            // When
            val newItem = buildTestMcbItem1()
            localStore.addItem(newItem)

            // Then
            observer.assertValues(
                emptyList(), // Initial value
                listOf(newItem) // After addition
            )
            observer.finish()
        }

        @Test
        fun `The state of the store is emitted when an item is deleted`() = runBlockingTest {
            // Given
            setup(initialItems = setOf(newItem1, newItem2, newItem3))
            val observer = localStore.observeItems().test(this)

            // When
            localStore.deleteItem(newItem2.id)

            // Then
            observer.assertValues(
                listOf(newItem1, newItem2, newItem3), // Initial value
                listOf(newItem1, newItem3) // After deletion
            )
            observer.finish()
        }

        @Test
        fun `The observed state of the store is correct when initially empty`() = runBlockingTest {
            // Given
            setup(initialItems = setOf())

            // When
            val observer = localStore.observeItems().test(this)

            // Then
            observer.assertValues(emptyList())
            observer.finish()
        }

        @Test
        fun `The observed state of the store is correct when initially not empty`() = runBlockingTest {
            // Given
            setup(initialItems = setOf(newItem1, newItem2, newItem3))

            // When
            val observer = localStore.observeItems().test(this)

            // Then
            observer.assertValues(listOf(newItem1, newItem2, newItem3))
            observer.finish()
        }

        @Test
        fun `The observed state of the store is sorted on the creation date in an descending order`() = runBlockingTest {
            // Given
            setup(initialItems = setOf(newItem3, newItem2, newItem1))

            // When
            val observer = localStore.observeItems().test(this)

            // Then
            observer.assertValues(listOf(newItem1, newItem2, newItem3))
            observer.finish()
        }
    }
}
