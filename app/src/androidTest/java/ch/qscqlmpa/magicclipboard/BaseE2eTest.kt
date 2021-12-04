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
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.koin.android.ext.android.get

@RunWith(AndroidJUnit4::class)
abstract class BaseE2eTest {

    @get:Rule
    val testRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val animationsRule = DisableAnimationsRule()

    private lateinit var res: Resources

    private lateinit var app: TestApp

    private lateinit var inMemoryLocalStore: InMemoryLocalStore

    private lateinit var idlingResource: IdlingResourceAdapter

    @Before
    fun setup() {
        app = ApplicationProvider.getApplicationContext()
        res = InstrumentationRegistry.getInstrumentation().targetContext.resources

        inMemoryLocalStore = app.get<LocalStore>() as InMemoryLocalStore
        idlingResource = IdlingResourceAdapter(app.get())
        testRule.registerIdlingResource(idlingResource)
        runBlocking { inMemoryLocalStore.initializeWith(clipBoardItems.toSet()) }
    }

    @After
    fun tearDown() {
        testRule.unregisterIdlingResource(idlingResource)
    }

    protected fun getString(resource: Int): String {
        return res.getString(resource)
    }
}

class IdlingResourceAdapter(private val idlingResource: McbIdlingResource) : androidx.compose.ui.test.IdlingResource {
    override val isIdleNow: Boolean
        get() = idlingResource.isIdleNow()

    fun increment(reason: String) {
        idlingResource.increment(reason)
    }

    fun decrement(reason: String) {
        idlingResource.decrement(reason)
    }
}
