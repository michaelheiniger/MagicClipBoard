package ch.qscqlmpa.magicclipboard.ui.components.multifab

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ch.qscqlmpa.magicclipboard.ui.common.UiTags

interface MultiFabItem {
    val id: String
    val content: @Composable () -> Unit
    val label: Int
}

enum class MultiFabState {
    COLLAPSED, EXPANDED
}

private const val animationDurationMs = 200

/**
 * Credits: https://github.com/ComposeAcademy/ComposeCompanion
 */
@Composable
fun MultiFloatingActionButton(
    @StringRes mainFabText: Int,
    @DrawableRes mainFabIcon: Int,
    items: List<MultiFabItem>,
    targetState: MultiFabState,
    stateChanged: (fabState: MultiFabState) -> Unit,
    onFabItemClicked: (item: MultiFabItem) -> Unit
) {
    val transition: Transition<MultiFabState> = updateTransition(targetState = targetState, label = "transition")
    val rotation: Float by transition.animateFloat(
        transitionSpec = { tween(durationMillis = animationDurationMs, easing = LinearEasing) },
        label = "rotation"
    ) { state -> if (state == MultiFabState.EXPANDED) 90f else 0f }

    Column(
        horizontalAlignment = Alignment.End,
        modifier = Modifier.fillMaxWidth()
    ) {
        items.forEach { item ->
            AnimatedVisibility(
                visible = targetState == MultiFabState.EXPANDED,
                enter = slideInVertically(
                    initialOffsetY = { height -> height },
                    animationSpec = TweenSpec(durationMillis = animationDurationMs, easing = LinearEasing)
                ) + fadeIn(animationSpec = TweenSpec(durationMillis = animationDurationMs, easing = LinearEasing)),
                exit = slideOutVertically(
                    targetOffsetY = { height -> height },
                    animationSpec = TweenSpec(durationMillis = animationDurationMs, easing = LinearEasing)
                ) + fadeOut(animationSpec = TweenSpec(durationMillis = animationDurationMs, easing = LinearEasing))
            ) {
                MiniFabItem(item, onFabItemClicked)
            }
            Spacer(modifier = Modifier.height(10.dp))
        }
        ExtendedFloatingActionButton(
            modifier = Modifier.testTag(UiTags.mainFab),
            onClick = {
                stateChanged(
                    if (transition.currentState == MultiFabState.EXPANDED) MultiFabState.COLLAPSED else MultiFabState.EXPANDED
                )
            },
            icon = {
                Icon(
                    modifier = Modifier.rotate(rotation),
                    painter = painterResource(mainFabIcon),
                    tint = Color.White,
                    contentDescription = stringResource(mainFabText)
                )
            },
            text = {
                Text(
                    text = stringResource(mainFabText),
                    color = Color.White
                )
            }
        )
    }
}

@Composable
private fun MiniFabItem(
    item: MultiFabItem,
    onFabItemClicked: (item: MultiFabItem) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = stringResource(item.label),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colors.secondary,
                    shape = RoundedCornerShape(5.dp)
                )
                .padding(horizontal = 6.dp, vertical = 4.dp)
                .align(Alignment.CenterVertically)
        )
        Spacer(modifier = Modifier.width(8.dp))
        FloatingActionButton(
            modifier = Modifier.testTag(item.id),
            onClick = { onFabItemClicked(item) }
        ) {
            item.content()
        }
    }
}
