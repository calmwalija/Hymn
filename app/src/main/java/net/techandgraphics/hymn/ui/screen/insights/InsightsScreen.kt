package net.techandgraphics.hymn.ui.screen.insights

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import net.techandgraphics.hymn.domain.model.HymnStat
import java.util.concurrent.TimeUnit

@Composable
fun InsightsScreen(
  state: InsightsUiState,
  onToggleYear: () -> Unit,
  onOpenHymn: (Int) -> Unit,
  onYearInHymns: () -> Unit,
) {
  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
      .padding(16.dp),
  ) {
    Text(
      text = "Insights",
      style = MaterialTheme.typography.headlineSmall,
      fontWeight = FontWeight.Bold,
    )
    Row(modifier = Modifier.padding(vertical = 8.dp)) {
      FilterChip(
        selected = !state.thisYearOnly,
        onClick = { if (state.thisYearOnly) onToggleYear() },
        label = { Text("All time") },
        modifier = Modifier.padding(end = 8.dp),
      )
      FilterChip(
        selected = state.thisYearOnly,
        onClick = { if (!state.thisYearOnly) onToggleYear() },
        label = { Text("This year") },
      )
    }

    if (state.summary.totalVisits == 0L) {
      Text(
        text = "Open a few hymns to unlock your insights.",
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(vertical = 24.dp),
      )
      return
    }

    Card(modifier = Modifier.fillMaxWidth()) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text("${state.summary.totalVisits} opens", fontWeight = FontWeight.Bold)
        Text("${state.summary.uniqueHymns} hymns · ${formatDuration(state.summary.totalTimeMs)}")
        Text("${state.summary.activeDays} active days · ${state.summary.currentStreak}-day streak")
      }
    }

    SectionTitle("Most visited")
    state.mostVisited.forEach { StatRow(it, "${it.visitCount} opens", onOpenHymn) }

    SectionTitle("Longest time with")
    state.topByTime.forEach { StatRow(it, formatDuration(it.totalTimeMs), onOpenHymn) }

    SectionTitle("Your themes")
    state.themes.forEach { theme ->
      Text(
        text = "${theme.categoryName} · ${theme.visitCount}",
        modifier = Modifier.padding(vertical = 4.dp),
      )
    }

    Button(
      onClick = onYearInHymns,
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 24.dp),
    ) {
      Text("Your Year in Hymns")
    }
  }
}

@Composable
private fun SectionTitle(text: String) {
  Text(
    text = text,
    style = MaterialTheme.typography.titleMedium,
    fontWeight = FontWeight.SemiBold,
    modifier = Modifier.padding(top = 20.dp, bottom = 8.dp),
  )
}

@Composable
private fun StatRow(stat: HymnStat, metric: String, onOpen: (Int) -> Unit) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .clickable { onOpen(stat.number) }
      .padding(vertical = 6.dp),
  ) {
    Text("#${stat.number}  ${stat.title}", fontWeight = FontWeight.Medium)
    Text(metric, style = MaterialTheme.typography.bodySmall)
  }
}

private fun formatDuration(ms: Long): String {
  val minutes = TimeUnit.MILLISECONDS.toMinutes(ms)
  val hours = minutes / 60
  val rem = minutes % 60
  return if (hours > 0) "${hours}h ${rem}m" else "${minutes}m"
}
