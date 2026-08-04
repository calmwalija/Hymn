package net.techandgraphics.hymn.ui.screen.insights

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.domain.model.HymnStat
import net.techandgraphics.hymn.ui.components.EmptyState
import net.techandgraphics.hymn.ui.components.HymnTopAppBar
import net.techandgraphics.hymn.ui.components.InfoPill
import net.techandgraphics.hymn.ui.components.NoAppBarInsets
import net.techandgraphics.hymn.ui.components.RankedBarRow
import net.techandgraphics.hymn.ui.components.SectionHeader
import net.techandgraphics.hymn.ui.components.StatTile
import net.techandgraphics.hymn.ui.theme.PillShape
import net.techandgraphics.hymn.ui.theme.Space
import java.util.concurrent.TimeUnit

/**
 * Reading stats. The previous version stacked bare `Text` rows; numbers are now
 * tiles you can scan and rankings are proportional bars you can compare.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightsScreen(
  state: InsightsUiState,
  onToggleYear: () -> Unit,
  onOpenHymn: (Int) -> Unit,
  onYearInHymns: () -> Unit,
) {
  val scrollBehavior =
    TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

  Scaffold(
    contentWindowInsets = NoAppBarInsets,
    modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    topBar = {
      HymnTopAppBar(
        title = stringResource(R.string.nav_insights),
        scrollBehavior = scrollBehavior,
      )
    },
  ) { padding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding),
      contentPadding = PaddingValues(bottom = Space.lg),
    ) {
      item {
        Row(
          horizontalArrangement = Arrangement.spacedBy(Space.xs),
          modifier = Modifier.padding(horizontal = Space.md, vertical = Space.xs),
        ) {
          FilterChip(
            selected = !state.thisYearOnly,
            onClick = { if (state.thisYearOnly) onToggleYear() },
            label = { Text(stringResource(R.string.insights_all_time)) },
            shape = PillShape,
          )
          FilterChip(
            selected = state.thisYearOnly,
            onClick = { if (!state.thisYearOnly) onToggleYear() },
            label = { Text(stringResource(R.string.insights_this_year)) },
            shape = PillShape,
          )
        }
      }

      if (state.summary.totalVisits == 0L) {
        item {
          EmptyState(
            icon = Icons.Rounded.Star,
            title = stringResource(R.string.insights_empty_title),
            message = stringResource(R.string.insights_empty),
          )
        }
        return@LazyColumn
      }

      item {
        Column(modifier = Modifier.padding(horizontal = Space.md)) {
          Row(horizontalArrangement = Arrangement.spacedBy(Space.xs)) {
            StatTile(
              value = state.summary.totalVisits.toString(),
              label = stringResource(R.string.insights_opens),
              icon = Icons.Rounded.PlayArrow,
              modifier = Modifier.weight(1f),
            )
            StatTile(
              value = state.summary.uniqueHymns.toString(),
              label = stringResource(R.string.insights_hymns),
              icon = Icons.Rounded.Favorite,
              modifier = Modifier.weight(1f),
            )
          }
          Row(
            horizontalArrangement = Arrangement.spacedBy(Space.xs),
            modifier = Modifier.padding(top = Space.xs),
          ) {
            StatTile(
              value = formatDuration(state.summary.totalTimeMs),
              label = stringResource(R.string.insights_time),
              icon = Icons.Rounded.DateRange,
              modifier = Modifier.weight(1f),
            )
            StatTile(
              value = state.summary.currentStreak.toString(),
              label = stringResource(R.string.insights_streak),
              icon = Icons.Rounded.Star,
              modifier = Modifier.weight(1f),
            )
          }
        }
      }

      if (state.mostVisited.isNotEmpty()) {
        item { SectionHeader(title = stringResource(R.string.insights_most_visited)) }
        val max = state.mostVisited.maxOf { it.visitCount }.coerceAtLeast(1)
        itemsIndexed(state.mostVisited, key = { _, it -> "visit-${it.number}-${it.lang}" }) { i, s ->
          StatBar(
            rank = i + 1,
            stat = s,
            metric = "${s.visitCount}",
            fraction = s.visitCount.toFloat() / max,
            onOpenHymn = onOpenHymn,
          )
        }
      }

      if (state.topByTime.isNotEmpty()) {
        item { SectionHeader(title = stringResource(R.string.insights_longest)) }
        val max = state.topByTime.maxOf { it.totalTimeMs }.coerceAtLeast(1)
        itemsIndexed(state.topByTime, key = { _, it -> "time-${it.number}-${it.lang}" }) { i, s ->
          StatBar(
            rank = i + 1,
            stat = s,
            metric = formatDuration(s.totalTimeMs),
            fraction = s.totalTimeMs.toFloat() / max,
            onOpenHymn = onOpenHymn,
          )
        }
      }

      if (state.themes.isNotEmpty()) {
        item {
          SectionHeader(title = stringResource(R.string.insights_themes))
          Row(
            horizontalArrangement = Arrangement.spacedBy(Space.xs),
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = Space.md),
          ) {
            state.themes.take(3).forEach { theme ->
              InfoPill(text = "${theme.categoryName} · ${theme.visitCount}")
            }
          }
        }
      }

      item {
        Button(
          onClick = onYearInHymns,
          shape = PillShape,
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = Space.md, vertical = Space.lg),
        ) {
          Text(stringResource(R.string.insights_wrapped_cta))
        }
      }
    }
  }
}

@Composable
private fun StatBar(
  rank: Int,
  stat: HymnStat,
  metric: String,
  fraction: Float,
  onOpenHymn: (Int) -> Unit,
) {
  RankedBarRow(
    rank = rank,
    title = stat.title,
    supporting = "#${stat.number} · ${stat.categoryName}",
    metric = metric,
    fraction = fraction,
    onClick = { onOpenHymn(stat.number) },
    modifier = Modifier.padding(horizontal = Space.md, vertical = Space.xxs),
  )
}

internal fun formatDuration(ms: Long): String {
  val minutes = TimeUnit.MILLISECONDS.toMinutes(ms)
  val hours = minutes / 60
  val rem = minutes % 60
  return if (hours > 0) "${hours}h ${rem}m" else "${minutes}m"
}
