package net.techandgraphics.hymn.ui.screen.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import net.techandgraphics.hymn.Faker
import net.techandgraphics.hymn.onTranslationChange
import net.techandgraphics.hymn.toast
import net.techandgraphics.hymn.ui.screen.category.CategoryItem
import net.techandgraphics.hymn.ui.screen.main.components.ApostleCreedDialog
import net.techandgraphics.hymn.ui.screen.main.components.FeaturedCategoryItem
import net.techandgraphics.hymn.ui.screen.main.components.LordsPrayerDialog
import net.techandgraphics.hymn.ui.screen.main.components.LyricScreenItem
import net.techandgraphics.hymn.ui.screen.main.components.UniquelyCraftedScreen
import net.techandgraphics.hymn.ui.theme.HymnTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun MainScreen(
  channelFlow: Flow<MainChannelEvent>,
  state: MainUiState,
  onEvent: (MainUiEvent) -> Unit,
) {

  val context = LocalContext.current
  val colorScheme = MaterialTheme.colorScheme
  var showSheet by remember { mutableStateOf(false) }
  val isImeVisible = WindowInsets.isImeVisible
  val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
  var isFocused by remember { mutableStateOf(false) }
  var showFavDialog by remember { mutableStateOf(false) }
  val scrollState = rememberLazyListState()
  var apostleCreedShow by remember { mutableStateOf(false) }
  var lordsPrayerShow by remember { mutableStateOf(false) }

  if (lordsPrayerShow) LordsPrayerDialog(state, onEvent) { lordsPrayerShow = false }
  if (apostleCreedShow) ApostleCreedDialog(state, onEvent) { apostleCreedShow = false }

  LaunchedEffect(key1 = channelFlow) {
    lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
      channelFlow.collect { event ->
        when (event) {
          MainChannelEvent.Language -> context.onTranslationChange(state.translation)
        }
      }
    }
  }

  LaunchedEffect(state.searchQuery) { if (state.searchQuery.trim().isNotEmpty()) isFocused = true }

  BackHandler(enabled = state.searchQuery.trim().isNotEmpty() && !isImeVisible) {
    onEvent(MainUiEvent.LyricEvent.ClearSearchQuery)
  }

  LaunchedEffect(Unit) {
    if (state.searchQuery.trim().isNotEmpty())
      onEvent(MainUiEvent.LyricEvent.LyricSearch(state.searchQuery))
  }

  Scaffold(
    topBar = {
      Column {
        Row(
          modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(
            modifier = Modifier
              .padding(end = 8.dp)
              .weight(1f),
          ) {
            Text(
              text = "Discover",
              style = MaterialTheme.typography.displaySmall,
              fontWeight = FontWeight.Bold
            )
            Text(
              text = "Which hymn are you looking for?", maxLines = 1,
              overflow = TextOverflow.Ellipsis
            )
          }
          MainMenuItem(state) { event ->
            when (event) {
              MainUiEvent.MenuItem.Favorites -> {
                if (state.favorites.isEmpty()) {
                  context.toast("You do not have any favorite hymns yet.")
                  return@MainMenuItem
                }
                showFavDialog = true
              }

              MainUiEvent.MenuItem.ApostlesCreed -> apostleCreedShow = true
              MainUiEvent.MenuItem.LordsPrayer -> lordsPrayerShow = true

              else -> onEvent(event)
            }
          }
        }

        if (showFavDialog) FavoriteDialog(
          favorites = state.favorites,
          onEvent = {
            if (it is MainUiEvent.GotoPreview) {
              onEvent(MainUiEvent.AnalyticEvent.GotoPreviewFromFavorite(it.lyric))
              showFavDialog = false
            }

            onEvent(it)
          }
        ) { showFavDialog = false }

        SearchBox(
          state = state,
          onFocusRequester = { isFocused = it },
          event = onEvent
        )

        if (state.searchQuery.trim().isEmpty()) {
          Column {
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow {
              items(state.search, key = { it.query }) {
                ElevatedCard(
                  onClick = {
                    onEvent(MainUiEvent.LyricEvent.QueryTag(it.query))
                    isFocused = true
                  },
                  elevation = CardDefaults.cardElevation(
                    defaultElevation = 1.dp
                  ),
                  shape = RoundedCornerShape(20),
                  modifier = Modifier.padding(horizontal = 4.dp)
                ) {
                  Text(
                    text = "#${it.tag}",
                    fontSize = MaterialTheme.typography.bodySmall.fontSize,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.primary,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                  )
                }
              }
            }
          }
        }
      }
    },
  ) {

    if (isFocused && state.searchQuery.trim().isNotEmpty()) {
      LazyColumn(modifier = Modifier.padding(it)) {
        itemsIndexed(state.lyrics, key = { _, lyric -> lyric.lyricId }) { index, lyric ->
          LyricScreenItem(
            lyric = lyric,
            onEvent = { event ->
              onEvent(MainUiEvent.LyricEvent.InsertSearchTag)
              onEvent(MainUiEvent.AnalyticEvent.GotoPreviewFromSearch(lyric))
              onEvent(event)
            },
            modifier = Modifier.animateItem(),
            showDivider = index.plus(1) < state.lyrics.size
          )
        }
      }
    }

    if (isFocused && state.searchQuery.trim().isNotEmpty() && state.lyrics.isEmpty()) {
      SearchEmptyState(state = state, onEvent = onEvent, paddingValues = it)
      onEvent(MainUiEvent.AnalyticEvent.SearchEmptyState(state.searchQuery.trim()))
    }

    if (state.searchQuery.trim().isEmpty()) {
      Column(
        modifier = Modifier
          .padding(it)
          .fillMaxSize()
          .verticalScroll(rememberScrollState())
      ) {

        if (state.uniquelyCrafted.isNotEmpty()) {
          Text(
            text = "Uniquely Crafted",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
              .padding(horizontal = 8.dp, vertical = 26.dp)
              .fillMaxWidth()
          )
          LazyRow(state = scrollState) {
            items(state.uniquelyCrafted) { lyric ->
              UniquelyCraftedScreen(lyric, onEvent)
            }
          }
        }

        if (state.diveInto.isNotEmpty()) {
          Spacer(modifier = Modifier.height(8.dp))
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .padding(horizontal = 8.dp, vertical = 26.dp)
          ) {
            Text(
              text = "Dive Into",
              style = MaterialTheme.typography.titleMedium,
              modifier = Modifier.weight(1f)
            )
          }

          state.diveInto.forEachIndexed { index, lyric ->
            LyricScreenItem(
              lyric = lyric,
              onEvent = { event ->
                onEvent(MainUiEvent.AnalyticEvent.GotoPreviewFromDiveInto(lyric.number))
                onEvent(event)
              },
              modifier = Modifier,
              showDivider = index.plus(1) < state.diveInto.size
            )
          }

          if (showSheet) {
            ModalBottomSheet(onDismissRequest = { showSheet = false }) {
              LazyVerticalGrid(columns = GridCells.Fixed(1)) {
                items(state.categories) { category ->
                  CategoryItem(category) { event ->
                    onEvent(MainUiEvent.AnalyticEvent.GotoTheCategory(category))
                    onEvent(event)
                    showSheet = false
                    onEvent(event)
                  }
                }
              }
            }
          }

          if (state.categories.isNotEmpty()) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier
                .clickable {
                  onEvent(MainUiEvent.FeaturedCategories)
                  onEvent(MainUiEvent.AnalyticEvent.ShowFeaturedCategoriesDialog)
                  showSheet = true
                }
                .padding(horizontal = 8.dp, vertical = 26.dp)
            ) {
              Text(
                text = "Featured Categories",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.weight(1f)
              )
              Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                  text = "See All",
                  style = MaterialTheme.typography.labelMedium,
                  fontWeight = FontWeight.Bold,
                  color = colorScheme.primary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                  imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                  contentDescription = null,
                  modifier = Modifier.size(20.dp),
                  tint = colorScheme.primary
                )
              }
            }

            LazyRow {
              items(state.categories.shuffled().subList(2, 7)) { category ->
                FeaturedCategoryItem(category, onEvent)
              }
            }

            Spacer(modifier = Modifier.height(20.dp))
          }
        }
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
  HymnTheme {
    MainScreen(
      channelFlow = flow { },
      state = MainUiState(
        uniquelyCrafted = listOf(Faker.lyric, Faker.lyric, Faker.lyric, Faker.lyric),
        diveInto = listOf(Faker.lyric, Faker.lyric, Faker.lyric, Faker.lyric)
      )
    ) {
    }
  }
}
