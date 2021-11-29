package ch.qscqlmpa.magicclipboard

import ch.qscqlmpa.magicclipboard.clipboard.McbItem
import ch.qscqlmpa.magicclipboard.data.store.local.InMemoryLocalStore
import ch.qscqlmpa.magicclipboard.data.store.local.LocalStore
import ch.qscqlmpa.magicclipboard.di.ioDispatcherName
import ch.qscqlmpa.magicclipboard.idlingresource.McbIdlingResource
import ch.qscqlmpa.magicclipboard.idlingresource.TestIdlingResource
import org.koin.dsl.module

val testIdlingResourceModule = module {
    single<McbIdlingResource> { TestIdlingResource("resource name") }
}

val testLocalStoreModule = module {
    single { emptySet<McbItem>() }
    single<LocalStore> { InMemoryLocalStore(get(ioDispatcherName), get()) }
}
