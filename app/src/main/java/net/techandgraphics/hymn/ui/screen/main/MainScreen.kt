package net.techandgraphics.hymn.ui.screen.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.techandgraphics.hymn.Faker
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.onTranslationChange
import net.techandgraphics.hymn.ui.components.CategoryArtwork
import net.techandgraphics.hymn.ui.components.EmptyState
import net.techandgraphics.hymn.ui.components.HymnListItem
import net.techandgraphics.hymn.ui.components.NoAppBarInsets
import net.techandgraphics.hymn.ui.components.SectionHeader
import net.techandgraphics.hymn.ui.screen.main.components.ApostleCreedDialog
import net.techandgraphics.hymn.ui.screen.main.components.FeaturedCategoryItem
import net.techandgraphics.hymn.ui.screen.main.components.LordsPrayerDialog
import net.techandgraphics.hymn.ui.screen.main.components.UniquelyCraftedScreen
import net.techandgraphics.hymn.ui.theme.HymnTheme
import net.techandgraphics.hymn.ui.theme.PillShape
import net.techandgraphics.hymn.ui.theme.Space
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MainScreen(
  channelFlow: Flow<MainChannelEvent>,
  state: MainUiState,
  onEvent: (MainUiEvent) -> Unit,
) {
  val context = LocalContext.current
  val isImeVisible = WindowInsets.isImeVisible
  val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
  var isFocused by remember { mutableStateOf(false) }
  var apostleCreedShow by remember { mutableStateOf(false) }
  var lordsPrayerShow by remember { mutableStateOf(false) }

  val query = state.searchQuery.trim()
  val searching = isFocused && query.isNotEmpty()

  if (lordsPrayerShow) LordsPrayerDialog(state, onEvent) { lordsPrayerShow = false }
  if (apostleCreedShow) ApostleCreedDialog(state, onEvent) { apostleCreedShow = false }

  LaunchedEffect(channelFlow) {
    lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
      channelFlow.collect { event ->
        when (event) {
          MainChannelEvent.Language -> context.onTranslationChange(state.translation)
        }
      }
    }
  }

  LaunchedEffect(state.searchQuery) { if (query.isNotEmpty()) isFocused = true }

  LaunchedEffect(Unit) {
    if (query.isNotEmpty()) onEvent(MainUiEvent.LyricEvent.LyricSearch(state.searchQuery))
  }

  BackHandler(enabled = query.isNotEmpty() && !isImeVisible) {
    onEvent(MainUiEvent.LyricEvent.ClearSearchQuery)
  }

  Scaffold(
    contentWindowInsets = NoAppBarInsets,
    topBar = {
      Column(modifier = Modifier.padding(bottom = Space.xs)) {
        HomeHeader(
          state = state,
          onEvent = { event ->
            when (event) {
              MainUiEvent.MenuItem.ApostlesCreed -> apostleCreedShow = true
              MainUiEvent.MenuItem.LordsPrayer -> lordsPrayerShow = true
              else -> onEvent(event)
            }
          }
        )
        SearchBox(
          state = state,
          onFocusRequester = { isFocused = it },
          event = onEvent,
        )
      }
    },
  ) { padding ->
    when {
      searching && state.lyrics.isEmpty() -> SearchEmptyState(
        paddingValues = padding,
        state = state,
        onEvent = onEvent,
      )

      searching -> SearchResults(
        lyrics = state.lyrics,
        paddingValues = padding,
        onEvent = onEvent,
      )

      else -> HomeContent(
        state = state,
        paddingValues = padding,
        onEvent = onEvent,
      )
    }
  }
}

@Composable
private fun HomeHeader(
  state: MainUiState,
  onEvent: (MainUiEvent) -> Unit,
) {
  Row(
    verticalAlignment = Alignment.CenterVertically,
    modifier = Modifier
      .fillMaxWidth()
      .padding(start = Space.md, end = Space.xs, top = Space.xs, bottom = Space.sm),
  ) {
    Column(modifier = Modifier.weight(1f)) {
      Text(
        text = stringResource(greetingRes()),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
      Text(
        text = stringResource(R.string.app_name),
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.primary,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
    }
    MainMenuItem(state, onEvent)
  }
}

@Composable
private fun SearchResults(
  lyrics: List<Lyric>,
  paddingValues: PaddingValues,
  onEvent: (MainUiEvent) -> Unit,
) {
  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(paddingValues),
  ) {
    item {
      Text(
        text = stringResource(R.string.search_results_count, lyrics.size),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(horizontal = Space.md, vertical = Space.xs),
      )
    }
    items(lyrics, key = { it.lyricId }) { lyric ->
      HymnListItem(
        lyric = lyric,
        modifier = Modifier.animateItem(),
        onClick = {
          onEvent(MainUiEvent.LyricEvent.InsertSearchTag)
          onEvent(MainUiEvent.AnalyticEvent.GotoPreviewFromSearch(lyric))
          onEvent(MainUiEvent.GotoPreview(lyric))
        },
      )
    }
  }
}

@Composable
private fun HomeContent(
  state: MainUiState,
  paddingValues: PaddingValues,
  onEvent: (MainUiEvent) -> Unit,
) {
  // Deterministic per-day pick so "hymn of the day" is stable while you use the
  // app, and the theme rail no longer reshuffles on every recomposition.
  val hymnOfTheDay = remember(state.uniquelyCrafted) {
    state.uniquelyCrafted.takeIf { it.isNotEmpty() }?.let {
      it[Calendar.getInstance().get(Calendar.DAY_OF_YEAR) % it.size]
    }
  }
  val continueHymn = state.diveInto.firstOrNull()
  val themeRail = remember(state.categories) { state.categories.shuffled().take(8) }

  if (state.uniquelyCrafted.isEmpty() && state.diveInto.isEmpty() && state.categories.isEmpty()) {
    EmptyState(
      icon = Icons.Rounded.Search,
      title = stringResource(R.string.app_name),
      message = stringResource(R.string.home_subtitle),
      modifier = Modifier.padding(paddingValues),
    )
    return
  }

  LazyColumn(
    modifier = Modifier
      .fillMaxSize()
      .padding(paddingValues),
    contentPadding = PaddingValues(bottom = Space.lg),
  ) {
    if (state.search.isNotEmpty()) {
      item {
        LazyRow(
          contentPadding = PaddingValues(horizontal = Space.md),
          horizontalArrangement = Arrangement.spacedBy(Space.xs),
          modifier = Modifier.padding(vertical = Space.xs),
        ) {
          items(state.search, key = { it.query }) { search ->
            Surface(
              shape = PillShape,
              color = MaterialTheme.colorScheme.secondaryContainer,
              onClick = { onEvent(MainUiEvent.LyricEvent.QueryTag(search.query)) },
            ) {
              Text(
                text = "#${search.tag}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = Space.sm, vertical = Space.xs),
              )
            }
          }
        }
      }
    }

    if (hymnOfTheDay != null) {
      item {
        HymnOfTheDayCard(
          lyric = hymnOfTheDay,
          onClick = {
            onEvent(MainUiEvent.AnalyticEvent.GotoPreviewFromUniquelyCrafted(hymnOfTheDay))
            onEvent(MainUiEvent.GotoPreview(hymnOfTheDay))
          },
        )
      }
    }

    if (continueHymn != null) {
      item {
        SectionHeader(title = stringResource(R.string.home_continue))
        ContinueCard(
          lyric = continueHymn,
          onClick = {
            onEvent(MainUiEvent.AnalyticEvent.GotoPreviewFromDiveInto(continueHymn.number))
            onEvent(MainUiEvent.GotoPreview(continueHymn))
          },
        )
      }
    }

    if (state.uniquelyCrafted.isNotEmpty()) {
      item {
        SectionHeader(title = stringResource(R.string.home_featured))
        LazyRow(
          contentPadding = PaddingValues(horizontal = Space.md),
          horizontalArrangement = Arrangement.spacedBy(Space.sm),
        ) {
          items(state.uniquelyCrafted, key = { it.lyricId }) { lyric ->
            UniquelyCraftedScreen(lyric, onEvent)
          }
        }
      }
    }

    if (themeRail.isNotEmpty()) {
      item {
        SectionHeader(
          title = stringResource(R.string.home_categories),
          actionLabel = stringResource(R.string.action_see_all),
          onAction = {
            onEvent(MainUiEvent.FeaturedCategories)
            onEvent(MainUiEvent.AnalyticEvent.ShowFeaturedCategoriesDialog)
            onEvent(MainUiEvent.GotoCategories)
          },
        )
        LazyRow(
          contentPadding = PaddingValues(horizontal = Space.md),
          horizontalArrangement = Arrangement.spacedBy(Space.sm),
        ) {
          items(themeRail, key = { it.lyric.categoryId }) { category ->
            FeaturedCategoryItem(category, onEvent)
          }
        }
      }
    }

    if (state.diveInto.isNotEmpty()) {
      item { SectionHeader(title = stringResource(R.string.home_recent)) }
      items(state.diveInto, key = { it.lyricId }) { lyric ->
        HymnListItem(
          lyric = lyric,
          onClick = {
            onEvent(MainUiEvent.AnalyticEvent.GotoPreviewFromDiveInto(lyric.number))
            onEvent(MainUiEvent.GotoPreview(lyric))
          },
        )
      }
    }
  }
}

/** The one hero on the home screen: today's hymn, over its theme artwork. */
@Composable
private fun HymnOfTheDayCard(
  lyric: Lyric,
  onClick: () -> Unit,
) {
  Surface(
    shape = MaterialTheme.shapes.extraLarge,
    color = MaterialTheme.colorScheme.primaryContainer,
    onClick = onClick,
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = Space.md, vertical = Space.sm),
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(Space.md),
    ) {
      CategoryArtwork(
        categoryId = lyric.categoryId,
        modifier = Modifier.size(72.dp),
      )
      Column(
        modifier = Modifier
          .weight(1f)
          .padding(horizontal = Space.md),
      ) {
        Text(
          text = stringResource(R.string.hymn_of_the_day).uppercase(),
          style = MaterialTheme.typography.labelSmall,
          color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.75f),
        )
        Text(
          text = lyric.title,
          style = MaterialTheme.typography.titleLarge,
          color = MaterialTheme.colorScheme.onPrimaryContainer,
          maxLines = 2,
          overflow = TextOverflow.Ellipsis,
        )
        Text(
          text = "#${lyric.number} · ${lyric.categoryName}",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
      }
      Box(
        modifier = Modifier
          .size(40.dp)
          .clip(PillShape)
          .padding(Space.xxs),
        contentAlignment = Alignment.Center,
      ) {
        Icon(
          imageVector = Icons.Rounded.PlayArrow,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.onPrimaryContainer,
        )
      }
    }
  }
}

@Composable
private fun ContinueCard(
  lyric: Lyric,
  onClick: () -> Unit,
) {
  Surface(
    shape = MaterialTheme.shapes.large,
    color = MaterialTheme.colorScheme.surfaceContainer,
    onClick = onClick,
    modifier = Modifier
      .fillMaxWidth()
      .padding(horizontal = Space.md),
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(Space.sm),
    ) {
      CategoryArtwork(categoryId = lyric.categoryId, modifier = Modifier.size(48.dp))
      Column(
        modifier = Modifier
          .weight(1f)
          .padding(horizontal = Space.sm),
      ) {
        Text(
          text = lyric.title,
          style = MaterialTheme.typography.titleMedium,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
        Text(
          text = "#${lyric.number} · ${lyric.categoryName}",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          maxLines = 1,
          overflow = TextOverflow.Ellipsis,
        )
      }
      Icon(
        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
      )
      Spacer(Modifier.width(Space.xxs))
    }
  }
}

private fun greetingRes(): Int = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
  in 0..11 -> R.string.home_greeting_morning
  in 12..16 -> R.string.home_greeting_afternoon
  else -> R.string.home_greeting_evening
}

@PreviewLightDark
@Composable
private fun MainScreenPreview() {
  HymnTheme {
    MainScreen(
      channelFlow = flow { },
      state = MainUiState(
        uniquelyCrafted = listOf(Faker.lyric, Faker.lyric.copy(lyricId = 2)),
        diveInto = listOf(Faker.lyric.copy(lyricId = 3), Faker.lyric.copy(lyricId = 4)),
      ),
      onEvent = {},
    )
  }
}
