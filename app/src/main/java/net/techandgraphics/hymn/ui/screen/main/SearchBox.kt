package net.techandgraphics.hymn.ui.screen.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.ui.theme.PillShape
import net.techandgraphics.hymn.ui.theme.Sizes
import net.techandgraphics.hymn.ui.theme.Space

private const val MAX_QUERY_LENGTH = 20

/**
 * The home search field. One pill-shaped surface holds the state icon, the
 * field, the clear affordance and the numeric-keypad toggle. The toggle used to
 * be a separate floating card beside the field that shoved it sideways whenever
 * the keyboard opened.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchBox(
  state: MainUiState,
  onFocusRequester: (Boolean) -> Unit,
  modifier: Modifier = Modifier,
  event: (MainUiEvent) -> Unit,
) {
  var numericKeyboard by remember { mutableStateOf(false) }
  val isImeVisible = WindowInsets.isImeVisible

  Surface(
    shape = PillShape,
    color = MaterialTheme.colorScheme.surfaceContainerHigh,
    tonalElevation = 1.dp,
    modifier = modifier
      .fillMaxWidth()
      .padding(horizontal = Space.md),
  ) {
    BasicTextField(
      value = TextFieldValue(state.searchQuery, selection = TextRange(state.searchQuery.length)),
      onValueChange = {
        if (it.text.length <= MAX_QUERY_LENGTH) {
          event(MainUiEvent.LyricEvent.LyricSearch(it.text))
        }
      },
      maxLines = 1,
      modifier = Modifier
        .fillMaxWidth()
        .onFocusChanged { onFocusRequester(it.isFocused) },
      textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.onSurface),
      keyboardOptions = KeyboardOptions(
        keyboardType = if (numericKeyboard) KeyboardType.Number else KeyboardType.Text,
      ),
      cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
      decorationBox = { innerTextField ->
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .heightIn(min = Sizes.minTouchTarget)
            .padding(start = Space.md, end = Space.xxs),
        ) {
          // Crossfade rather than two overlapping AnimatedVisibility blocks:
          // the leading slot only ever shows one of the two states.
          Crossfade(targetState = state.isSearching, label = "searchState") { searching ->
            Box(modifier = Modifier.size(20.dp), contentAlignment = Alignment.Center) {
              if (searching) {
                CircularProgressIndicator(
                  modifier = Modifier.size(18.dp),
                  strokeWidth = 2.dp,
                  color = MaterialTheme.colorScheme.primary,
                )
              } else {
                Icon(
                  imageVector = Icons.Rounded.Search,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.onSurfaceVariant,
                  modifier = Modifier.size(20.dp),
                )
              }
            }
          }

          Box(
            modifier = Modifier
              .weight(1f)
              .padding(horizontal = Space.sm),
            contentAlignment = Alignment.CenterStart,
          ) {
            innerTextField()
            if (state.searchQuery.isEmpty()) {
              Text(
                text = stringResource(R.string.home_search_hint),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
              )
            }
          }

          AnimatedVisibility(state.searchQuery.isNotEmpty()) {
            IconButton(onClick = { event(MainUiEvent.LyricEvent.ClearSearchQuery) }) {
              Icon(
                imageVector = Icons.Rounded.Close,
                contentDescription = stringResource(R.string.action_clear),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
              )
            }
          }

          AnimatedVisibility(isImeVisible) {
            IconButton(
              onClick = {
                event(
                  MainUiEvent.AnalyticEvent.KeyboardType(
                    if (numericKeyboard) KeyboardType.Text.toString()
                    else KeyboardType.Number.toString(),
                  ),
                )
                numericKeyboard = !numericKeyboard
              },
            ) {
              Icon(
                painter = painterResource(
                  if (numericKeyboard) R.drawable.ic_keyboard else R.drawable.ic_dialpad,
                ),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp),
              )
            }
          }
        }
      },
    )
  }
}
