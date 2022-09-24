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
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import ch.qscqlmpa.magicclipboard.ui.common.findActivity
import ch.qscqlmpa.magicclipboard.ui.magicclipboard.BottomNavItem
import ch.qscqlmpa.magicclipboard.ui.magicclipboard.allitems.AllItemsClipboardScreen
import ch.qscqlmpa.magicclipboard.ui.magicclipboard.allitems.AllItemsClipboardViewModel
import ch.qscqlmpa.magicclipboard.ui.magicclipboard.favoriteitems.FavoriteItemsClipboardViewModel
import ch.qscqlmpa.magicclipboard.ui.magicclipboard.favoriteitems.FavoritesScreen
import ch.qscqlmpa.magicclipboard.ui.signin.SignInScreen
import ch.qscqlmpa.magicclipboard.ui.signin.SignInViewModel
import ch.qscqlmpa.magicclipboard.viewmodel.BaseViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MagicClipboard(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    screenNavigator: ScreenNavigator,
    onToggleDarkTheme: () -> Unit,
) {
    val navController = rememberNavController()
    DisposableEffect(navController) {
        screenNavigator.setNavController(navController)
        onDispose { screenNavigator.clearNavController() }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

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
            val viewModel = koinViewModel<SignInViewModel>()
            HookViewModelToLifecycle(viewModel, lifecycleOwner)
            SignInScreen(
                viewModel = viewModel,
                onToggleDarkTheme = onToggleDarkTheme
            )
        }
        composable(route = Destination.Clipboard.routeName) {
            val newClipboardValue = getNewClipboardValueFromDeepLink()
            val viewModel = koinViewModel<AllItemsClipboardViewModel> { parametersOf(newClipboardValue) }
            HookViewModelToLifecycle(viewModel, lifecycleOwner)
            AllItemsClipboardScreen(
                viewModel = viewModel,
                currentRoute = currentRoute,
                onBottomBarItemClick = { item -> onItemClick(navController, item) },
                onToggleDarkTheme = onToggleDarkTheme
            )
        }
        composable(route = Destination.FavoriteItems.routeName) {
            val viewModel = koinViewModel<FavoriteItemsClipboardViewModel>()
            HookViewModelToLifecycle(viewModel, lifecycleOwner)
            FavoritesScreen(
                viewModel = viewModel,
                currentRoute = currentRoute,
                onItemClick = { item -> onItemClick(navController, item) },
                onToggleDarkTheme = onToggleDarkTheme
            )
        }
    }
}

private fun onItemClick(
    navController: NavController,
    item: BottomNavItem
) {
    navController.navigate(item.screenRoute) {
        navController.graph.startDestinationRoute?.let { screenRoute ->
            popUpTo(screenRoute) {
                saveState = true
            }
        }
        launchSingleTop = true
        restoreState = true
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

sealed class Destination(open val routeName: String) {
    object SignIn : Destination("signIn")

    sealed class BottomNavDestination(override val routeName: String) : Destination(routeName)
    object Clipboard : BottomNavDestination("clipboard")
    object FavoriteItems : BottomNavDestination("favoriteItems")
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
