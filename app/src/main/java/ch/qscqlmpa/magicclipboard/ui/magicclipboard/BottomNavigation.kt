package ch.qscqlmpa.magicclipboard.ui.magicclipboard

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import ch.qscqlmpa.magicclipboard.R
import ch.qscqlmpa.magicclipboard.ui.Destination

sealed class BottomNavItem(var title: String, var icon: Int, var screenRoute: String) {
    object Clipboard : BottomNavItem("Clipboard", R.drawable.ic_baseline_content_paste_24, Destination.Clipboard.routeName)
    object FavoriteItems : BottomNavItem("Favorites", R.drawable.ic_baseline_favorite_24, Destination.FavoriteItems.routeName)
}

@Composable
fun ClipboardBottomBar(
    currentRoute: String?,
    onItemClick: (BottomNavItem) -> Unit,
) {
    val items = listOf(
        BottomNavItem.Clipboard,
        BottomNavItem.FavoriteItems
    )
    BottomNavigation {
        items.forEach { item ->
            BottomNavigationItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
                label = { Text(text = item.title, fontSize = 9.sp) },
                selectedContentColor = MaterialTheme.colors.secondary,
                unselectedContentColor = Color.White,
                alwaysShowLabel = true,
                selected = currentRoute == item.screenRoute,
                onClick = { onItemClick(item) }
            )
        }
    }
}
