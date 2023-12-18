package net.techandgraphics.hymn.ui.screen.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay
import net.techandgraphics.hymn.R

@Composable
fun ChatBoxScreen(
  state: SearchState,
  event: (SearchEvent) -> Unit
) {

  val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lf30_qhwxkcno))

  val showKeyboard = remember { mutableStateOf(true) }
  val focusRequester = remember { FocusRequester() }
  val keyboard = LocalSoftwareKeyboardController.current

  LaunchedEffect(focusRequester) {
    if (showKeyboard.equals(true)) {
      focusRequester.requestFocus()
      delay(100)
      keyboard?.show()
    }
  }

  Card(
    modifier = Modifier
      .padding(horizontal = 8.dp)
      .clickable { showKeyboard.value = true },
    shape = RoundedCornerShape(50)
  ) {
    BasicTextField(
      value = TextFieldValue(state.searchQuery, selection = TextRange(state.searchQuery.length)),
      onValueChange = { if (it.text.length <= 20) event(SearchEvent.OnSearchQuery(it.text)) },
      maxLines = 1,
      modifier = Modifier
        .focusRequester(focusRequester)
        .fillMaxWidth(),
      textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.secondary),
      decorationBox = { innerTextField ->
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {

          Box(
            modifier = Modifier
              .padding(end = 8.dp),
            contentAlignment = Alignment.Center
          ) {
            this@Row.AnimatedVisibility(
              visible = state.isSearching.not(),
              exit = fadeOut()
            ) {
              Icon(
                painter = painterResource(id = R.drawable.ic_search),
                contentDescription = null,
                modifier = Modifier
                  .size(23.dp)
                  .padding(1.dp)
              )
            }

            this@Row.AnimatedVisibility(visible = state.isSearching, exit = fadeOut()) {
              LottieAnimation(
                composition = lottieComposition,
                iterations = LottieConstants.IterateForever,
                modifier = Modifier
                  .size(23.dp)
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
                text = "Which hymn are you looking for?",
                color = LocalContentColor.current.copy(alpha = 0.5f)
              )
          }

          AnimatedVisibility(visible = state.searchQuery.isNotEmpty()) {
            IconButton(
              onClick = { event(SearchEvent.ClearSearchQuery) },
              modifier = Modifier
                .size(23.dp)
            ) {
              Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = null,
                modifier = Modifier.padding(1.dp),
                tint = MaterialTheme.colorScheme.primary
              )
            }
          }
        }
      }
    )
  }
}
