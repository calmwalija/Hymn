package net.techandgraphics.hymn.ui.screen.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun SettingsTextExpComp(
  drawableRes: Int,
  title: String,
  description: String,
  maxLines: Int = 3,
  trailingIcon: ImageVector? = null,
  onEvent: (() -> Unit)? = null
) {
  Row(
    modifier = Modifier
      .fillMaxWidth()
      .clickable(onEvent != null) { onEvent?.invoke() }
      .padding(16.dp),
    verticalAlignment = Alignment.CenterVertically
  ) {
    Icon(
      painter = painterResource(id = drawableRes),
      contentDescription = description,
    )
    Column(
      modifier = Modifier
        .weight(1f)
        .padding(start = 24.dp, end = 16.dp)
    ) {

      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = title,
          color = MaterialTheme.colorScheme.primary,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.width(8.dp))
        Badge { Text("Experimental") }
      }
      Text(
        text = description,
        style = MaterialTheme.typography.bodySmall,
        maxLines = maxLines,
        overflow = TextOverflow.Ellipsis
      )
    }
    trailingIcon?.let {
      Icon(imageVector = it, null, modifier = Modifier.padding(8.dp))
    }
  }
}
