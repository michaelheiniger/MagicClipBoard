package ch.qscqlmpa.magicclipboard.viewmodel

import androidx.annotation.CallSuper
import androidx.lifecycle.ViewModel
import org.tinylog.Logger

abstract class BaseViewModel : ViewModel() {

    init {
        Logger.debug { "Create $this" }
    }

    @CallSuper
    open fun onStart() {
        Logger.debug { "$this.onStart()" }
    }

    @CallSuper
    open fun onStop() {
        Logger.debug { "$this.onStop()" }
    }
}
