package net.techandgraphics.hymn.ui.screen.component

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

enum class SwipeDirection { RIGHT_TO_LEFT, LEFT_TO_RIGHT, BOTH }

@Composable
fun SwipeBothDir4Action(
  isRevealed: Boolean,
  leftActions: @Composable (RowScope.() -> Unit)? = null,
  rightActions: @Composable (RowScope.() -> Unit)? = null,
  modifier: Modifier = Modifier,
  swipeDirection: SwipeDirection = SwipeDirection.BOTH,
  onLeftExpanded: () -> Unit = {},
  onRightExpanded: () -> Unit = {},
  onCollapsed: () -> Unit = {},
  content: @Composable () -> Unit,
) {
  var contextMenuWidth by remember { mutableFloatStateOf(0f) }
  val offsetX = remember { Animatable(0f) }
  val scope = rememberCoroutineScope()

  val canSwipeLeft = swipeDirection != SwipeDirection.RIGHT_TO_LEFT
  val canSwipeRight = swipeDirection != SwipeDirection.LEFT_TO_RIGHT

  val minOffset = if (canSwipeLeft) -contextMenuWidth else 0f
  val maxOffset = if (canSwipeRight) contextMenuWidth else 0f

  LaunchedEffect(isRevealed) {
    if (isRevealed) {
      scope.launch {
        offsetX.animateTo(if (offsetX.value >= 0) maxOffset else minOffset)
      }
    } else {
      scope.launch {
        offsetX.animateTo(0f)
      }
    }
  }

  Box(
    modifier = modifier
      .fillMaxWidth()
      .height(IntrinsicSize.Max)
  ) {

    if (canSwipeLeft) {
      Row(
        modifier = Modifier
          .onSizeChanged { contextMenuWidth = it.width.toFloat() }
          .fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        leftActions?.invoke(this)
      }
    }

    if (canSwipeRight) {
      Row(
        modifier = Modifier
          .align(Alignment.CenterEnd)
          .onSizeChanged { contextMenuWidth = it.width.toFloat() }
          .fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically
      ) {
        rightActions?.invoke(this)
      }
    }

    Surface(
      modifier = Modifier
        .fillMaxSize()
        .offset { IntOffset(offsetX.value.roundToInt(), 0) }
        .pointerInput(contextMenuWidth) {
          detectHorizontalDragGestures(
            onHorizontalDrag = { _, dragAmount ->
              scope.launch {
                val newOffset = (offsetX.value + dragAmount).coerceIn(minOffset, maxOffset)
                offsetX.snapTo(newOffset)
              }
            },
            onDragEnd = {
              when {
                offsetX.value >= maxOffset / 2f -> {
                  scope.launch {
                    offsetX.animateTo(maxOffset)
                    onRightExpanded()
                  }
                }

                offsetX.value <= minOffset / 2f -> {
                  scope.launch {
                    offsetX.animateTo(minOffset)
                    onLeftExpanded()
                  }
                }

                else -> {
                  scope.launch {
                    offsetX.animateTo(0f)
                    onCollapsed()
                  }
                }
              }
            }
          )
        }
    ) {
      content()
    }
  }
}
