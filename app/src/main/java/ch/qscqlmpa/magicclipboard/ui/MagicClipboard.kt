package ch.qscqlmpa.magicclipboard.ui

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import ch.qscqlmpa.magicclipboard.ui.common.findActivity
import ch.qscqlmpa.magicclipboard.ui.magicclipboard.ClipboardScreen
import ch.qscqlmpa.magicclipboard.ui.magicclipboard.ClipboardViewModel
import ch.qscqlmpa.magicclipboard.ui.signin.SignInScreen
import ch.qscqlmpa.magicclipboard.ui.signin.SignInViewModel
import ch.qscqlmpa.magicclipboard.viewmodel.BaseViewModel
import org.koin.androidx.compose.viewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MagicClipboard(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    screenNavigator: ScreenNavigator
) {
    val navController = rememberNavController()
    DisposableEffect(navController) {
        screenNavigator.setNavController(navController)
        onDispose { screenNavigator.clearNavController() }
    }

    NavHost(
        navController = navController,
        startDestination = Destination.SignIn.routeName
    ) {
        composable(
            route = Destination.SignIn.routeName,
            deepLinks = listOf(
                navDeepLink {
                    action = Intent.ACTION_SEND
                    mimeType = "plain/text"
                }
            )
        ) {
            val viewModel by viewModel<SignInViewModel>()
            HookViewModelToLifecycle(viewModel, lifecycleOwner)
            SignInScreen(viewModel)
        }
        composable(
            route = Destination.Clipboard.routeName
        ) {
            val newClipboardValue = getNewClipboardValueFromDeepLink()
            val viewModel by viewModel<ClipboardViewModel> { parametersOf(newClipboardValue) }
            HookViewModelToLifecycle(viewModel, lifecycleOwner)
            ClipboardScreen(viewModel)
        }
    }
}

@Composable
private fun getNewClipboardValueFromDeepLink(): String? {
    val context = LocalContext.current
    val activity = context.findActivity()
    val intent = activity?.intent
    return if (intent != null && intent.action == Intent.ACTION_SEND) {
        intent.getStringExtra(Intent.EXTRA_TEXT)
    } else null
}

sealed class Destination(val routeName: String) {
    object SignIn : Destination("signIn")
    object Clipboard : Destination("clipboard")
}

@Composable
private fun HookViewModelToLifecycle(
    viewModel: BaseViewModel,
    lifecycleOwner: LifecycleOwner
) {
    // See https://developer.android.com/jetpack/compose/side-effects#disposableeffect
    // Safely update the current lambdas when a new one is provided (TODO: Understand. Not sure why it's needed)
    val currentOnStart by rememberUpdatedState(viewModel::onStart)
    val currentOnStop by rememberUpdatedState(viewModel::onStop)

    DisposableEffect(lifecycleOwner) {
        // Create an observer that triggers our remembered callbacks
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) currentOnStart()
            else if (event == Lifecycle.Event.ON_STOP) currentOnStop()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
}
