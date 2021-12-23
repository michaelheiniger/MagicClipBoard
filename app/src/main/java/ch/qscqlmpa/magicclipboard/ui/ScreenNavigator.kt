package ch.qscqlmpa.magicclipboard.ui

import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import org.tinylog.kotlin.Logger

class ScreenNavigator {

    private var navController: NavController? = null

    fun setNavController(navController: NavController) {
        this.navController = navController
    }

    fun clearNavController() {
        this.navController = null
    }

    fun navigate(
        destination: Destination,
        navOptions: NavOptions? = null
    ) {
        Logger.debug { "Order navigation to ${destination.routeName}" }
        navController!!.navigate(
            route = destination.routeName,
            navOptions = navOptions
        )
    }
}

fun navOptionsPopUpToInclusive(routeName: String): NavOptions {
    return navOptions {
        popUpTo(routeName) {
            inclusive = true
        }
    }
}
