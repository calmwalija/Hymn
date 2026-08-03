package net.techandgraphics.hymn.ui.screen.library

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.toNumber

@Composable
fun LibraryScreen(
  onFavorites: () -> Unit,
  onHistory: () -> Unit,
  onSettings: () -> Unit,
  onCreed: () -> Unit,
  onPrayer: () -> Unit,
) {
  Column(modifier = Modifier.fillMaxSize()) {
    Text(
      text = "Library",
      style = MaterialTheme.typography.headlineSmall,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.padding(16.dp),
    )
    LibraryRow("Favorites", "Saved hymns", onFavorites)
    LibraryRow("History", "Recently opened", onHistory)
    LibraryRow("Apostles' Creed", "Read the creed", onCreed)
    LibraryRow("Lord's Prayer", "Read the prayer", onPrayer)
    LibraryRow("Settings", "Appearance and backup", onSettings)
  }
}

@Composable
private fun LibraryRow(title: String, subtitle: String, onClick: () -> Unit) {
  Column(modifier = Modifier.clickable(onClick = onClick)) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 14.dp),
    ) {
      Column(modifier = Modifier.weight(1f)) {
        Text(text = title, fontWeight = FontWeight.SemiBold)
        Text(
          text = subtitle,
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
      Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
    }
    HorizontalDivider(modifier = Modifier.padding(start = 16.dp))
  }
}

@Composable
fun HymnSimpleList(
  title: String,
  hymns: List<Lyric>,
  emptyMessage: String,
  onOpen: (Int) -> Unit,
) {
  Column(modifier = Modifier.fillMaxSize()) {
    Text(
      text = title,
      style = MaterialTheme.typography.headlineSmall,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.padding(16.dp),
    )
    if (hymns.isEmpty()) {
      Text(
        text = emptyMessage,
        modifier = Modifier.padding(16.dp),
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    } else {
      LazyColumn {
        items(hymns, key = { it.number }) { lyric ->
          Column(modifier = Modifier.clickable { onOpen(lyric.number) }) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            ) {
              Text(
                text = lyric.toNumber(),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 12.dp),
              )
              Column {
                Text(
                  text = lyric.title,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis,
                  color = MaterialTheme.colorScheme.primary,
                )
                Text(
                  text = lyric.categoryName,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis,
                  style = MaterialTheme.typography.bodySmall,
                )
              }
            }
            HorizontalDivider(modifier = Modifier.padding(start = 16.dp))
          }
        }
      }
    }
  }
}
