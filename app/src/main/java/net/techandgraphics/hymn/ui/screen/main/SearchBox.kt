package net.techandgraphics.hymn.ui.screen.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import net.techandgraphics.hymn.R

@Composable
fun SearchBox(
  state: MainUiState,
  onFocusRequester: (Boolean) -> Unit,
  event: (MainUiEvent) -> Unit,
) {

  var keyboardText by remember { mutableStateOf(false) }
  val focusRequester = remember { FocusRequester() }

  Row(verticalAlignment = Alignment.CenterVertically) {
    Card(
      modifier = Modifier
        .weight(1f)
        .focusRequester(focusRequester)
        .onFocusChanged { onFocusRequester(it.isFocused) }
        .padding(horizontal = 8.dp),
      shape = RoundedCornerShape(50),
      colors = CardDefaults.elevatedCardColors(),
      elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
    ) {
      BasicTextField(
        value = TextFieldValue(state.searchQuery, selection = TextRange(state.searchQuery.length)),
        onValueChange = {
          if (it.text.length <= 20) event(
            MainUiEvent.LyricUiEvent.OnLyricUiQuery(it.text)
          )
        },
        maxLines = 1,
        modifier = Modifier
          .fillMaxWidth(),
        textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.secondary),
        keyboardOptions = KeyboardOptions(keyboardType = if (keyboardText) KeyboardType.Number else KeyboardType.Text),
        decorationBox = { innerTextField ->
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .padding(horizontal = 16.dp, vertical = 12.dp)
          ) {

            Box(
              modifier = Modifier.size(20.dp),
              contentAlignment = Alignment.Center
            ) {
              this@Card.AnimatedVisibility(
                visible = state.isSearching.not(),
                enter = fadeIn(),
                exit = fadeOut()
              ) {
                Icon(
                  painter = painterResource(id = R.drawable.ic_outline_search),
                  contentDescription = null,
                  modifier = Modifier
                    .size(20.dp)
                    .padding(1.dp)
                )
              }

              this@Card.AnimatedVisibility(
                visible = state.isSearching,
                exit = fadeOut(),
                enter = fadeIn(),
              ) {
                CircularProgressIndicator(
                  modifier = Modifier
                    .size(18.dp),
                  strokeWidth = 3.dp,
                  color = MaterialTheme.colorScheme.secondary
                )
              }
            }

            Box(
              modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp),
              contentAlignment = Alignment.CenterStart
            ) {
              innerTextField()
              if (state.searchQuery.isEmpty())
                Text(
                  text = "Search by hymn number or keyword",
                  color = LocalContentColor.current.copy(alpha = 0.5f),
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis
                )
            }
            AnimatedVisibility(visible = state.searchQuery.isNotEmpty()) {
              IconButton(
                onClick = { event(MainUiEvent.LyricUiEvent.ClearLyricUiQuery) },
                modifier = Modifier.size(24.dp)
              ) {
                Icon(
                  imageVector = Icons.Default.Clear,
                  contentDescription = null,
                  modifier = Modifier.padding(1.dp),
                  tint = MaterialTheme.colorScheme.primary
                )
              }
            }

            Spacer(modifier = Modifier.width(2.dp))

            IconButton(
              onClick = {
                event(
                  MainUiEvent.KeyboardType(
                    if (!keyboardText) KeyboardType.Number.toString() else
                      KeyboardType.Text.toString()
                  )
                )
                keyboardText = !keyboardText
              },
              modifier = Modifier.size(24.dp)
            ) {
              Icon(
                painter = painterResource(id = if (!keyboardText) R.drawable.ic_dialpad else R.drawable.ic_keyboard),
                contentDescription = null,
                modifier = Modifier.padding(2.dp),
              )
            }
          }
        },
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
      )
    }

    Spacer(modifier = Modifier.width(8.dp))
  }
}
