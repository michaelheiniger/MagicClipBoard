package ch.qscqlmpa.magicclipboard

import android.content.res.Resources
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import ch.qscqlmpa.magicclipboard.data.store.local.InMemoryLocalStore
import ch.qscqlmpa.magicclipboard.data.store.local.LocalStore
import ch.qscqlmpa.magicclipboard.idlingresource.McbIdlingResource
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.koin.android.ext.android.get

@RunWith(AndroidJUnit4::class)
abstract class BaseE2eTest {

    @get:Rule
    val testRule = createAndroidComposeRule<MainActivity>()

    private lateinit var res: Resources

    lateinit var app: TestApp

    private lateinit var inMemoryLocalStore: InMemoryLocalStore

    @Before
    fun setup() {
        app = ApplicationProvider.getApplicationContext()
        res = InstrumentationRegistry.getInstrumentation().targetContext.resources

        inMemoryLocalStore = app.get<LocalStore>() as InMemoryLocalStore
        app.get<McbIdlingResource>().increment("Setup LocalStore with test data")
        runBlocking { inMemoryLocalStore.initializeWith(clipBoardItems.toSet()) }
    }

    protected fun getString(resource: Int): String {
        return res.getString(resource)
    }
}
