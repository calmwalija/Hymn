package net.techandgraphics.hymn.ui.screen.main.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.techandgraphics.hymn.Faker
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.toNumber
import net.techandgraphics.hymn.ui.screen.component.ImageComponent
import net.techandgraphics.hymn.ui.screen.component.TimestampComponent
import net.techandgraphics.hymn.ui.screen.component.TimestampFormat
import net.techandgraphics.hymn.ui.screen.main.MainUiEvent
import net.techandgraphics.hymn.ui.theme.HymnTheme

@Composable
fun LyricScreenItem(
  lyric: Lyric,
  modifier: Modifier = Modifier,
  showDivider: Boolean = true,
  onEvent: (MainUiEvent) -> Unit,
) {
  val context = LocalContext.current
  Column(
    modifier = modifier
      .clickable {
        onEvent(MainUiEvent.Event(MainUiEvent.OfType.Preview, lyric.number))
      }
      .fillMaxWidth(),
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)
    ) {
      ImageComponent(lyric)
      Column(
        modifier = Modifier
          .padding(horizontal = 16.dp)
          .weight(1f)
      ) {
        Text(
          text = lyric.toNumber(),
          fontWeight = FontWeight.Bold,
          style = MaterialTheme.typography.bodyLarge,
        )
        Text(
          text = lyric.title,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.primary,
        )
        Text(
          text = lyric.categoryName,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
          style = MaterialTheme.typography.bodySmall,
        )
        TimestampComponent(context, lyric, TimestampFormat.Short)
      }
    }
  }
  if (showDivider)
    HorizontalDivider(
      modifier = Modifier
        .padding(horizontal = 16.dp)
        .padding(start = 84.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun SearchScreenItemPreview() {
  HymnTheme {
    LyricScreenItem(
      lyric = Faker.lyric,
      modifier = Modifier
    ) {
    }
  }
}
