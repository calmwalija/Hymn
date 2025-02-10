package net.techandgraphics.hymn.ui.screen.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun SettingContentComp(
  title: @Composable () -> Unit,
  description: String,
  maxLines: Int = 3,
  content: @Composable () -> Unit,
  onEvent: (() -> Unit)? = null
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onEvent != null) { onEvent?.invoke() }
      .padding(16.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    content()
    Column(
      modifier = Modifier
        .weight(1f)
        .padding(start = 24.dp, end = 16.dp)
    ) {
      title()
      Text(
        text = description,
        style = MaterialTheme.typography.bodySmall,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis
      )
    }
  }
}
