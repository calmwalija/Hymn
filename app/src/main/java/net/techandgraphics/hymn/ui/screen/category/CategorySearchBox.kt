package net.techandgraphics.hymn.ui.screen.category

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import net.techandgraphics.hymn.R

@Composable
fun CategorySearchBox(
  state: CategoryState,
  event: (CategoryEvent) -> Unit,
) {
  Card(
    modifier = Modifier
      .padding(horizontal = 8.dp),
    shape = RoundedCornerShape(50),
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.surface
    ),
    elevation = CardDefaults.cardElevation(
      defaultElevation = 1.dp
    ),
  ) {
    BasicTextField(
      value = TextFieldValue(state.searchQuery, selection = TextRange(state.searchQuery.length)),
      onValueChange = { if (it.text.length <= 20) event(CategoryEvent.OnSearchQuery(it.text)) },
      maxLines = 1,
      modifier = Modifier
        .fillMaxWidth(),
      textStyle = LocalTextStyle.current.copy(color = MaterialTheme.colorScheme.secondary),
      keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
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
            this@Row.AnimatedVisibility(
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

            this@Row.AnimatedVisibility(
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
                text = "Which category are you looking for?",
                color = LocalContentColor.current.copy(alpha = 0.5f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
          }

          AnimatedVisibility(visible = state.searchQuery.isNotEmpty()) {
            IconButton(
              onClick = { event(CategoryEvent.ClearSearchQuery) },
              modifier = Modifier
                .size(20.dp)
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
      },
      cursorBrush = SolidColor(MaterialTheme.colorScheme.secondary)
    )
  }
}
