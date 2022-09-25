package ch.qscqlmpa.magicclipboard

import ch.qscqlmpa.magicclipboard.idlingresource.McbIdlingResource
import ch.qscqlmpa.magicclipboard.idlingresource.TestIdlingResource
import org.koin.dsl.module

val testIdlingResourceModule = module {
    single<McbIdlingResource> { TestIdlingResource("resource name") }
}
