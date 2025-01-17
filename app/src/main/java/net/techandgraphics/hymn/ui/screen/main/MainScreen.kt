package net.techandgraphics.hymn.ui.screen.main

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import net.techandgraphics.hymn.Faker
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.onTranslationChange
import net.techandgraphics.hymn.ui.screen.category.CategoryItem
import net.techandgraphics.hymn.ui.screen.category.CategoryViewModel
import net.techandgraphics.hymn.ui.screen.component.ToggleSwitchItem
import net.techandgraphics.hymn.ui.screen.main.components.DiveIntoItemScreen
import net.techandgraphics.hymn.ui.screen.main.components.FeaturedCategoryItem
import net.techandgraphics.hymn.ui.screen.main.components.UniquelyCraftedScreen
import net.techandgraphics.hymn.ui.screen.search.LyricScreenItem
import net.techandgraphics.hymn.ui.screen.search.SearchBox
import net.techandgraphics.hymn.ui.theme.HymnTheme

open class ToggleSwitchHomeItem : ToggleSwitchItem {
  data object English : ToggleSwitchHomeItem()
  data object Chichewa : ToggleSwitchHomeItem()
}

@OptIn(
  ExperimentalLayoutApi::class, ExperimentalFoundationApi::class,
  ExperimentalMaterial3Api::class
)
@Composable
fun MainScreen(
  state: MainUiState,
  onEvent: (MainUiEvent) -> Unit,
) {

  val context = LocalContext.current
  val versionValue = context.resources.getStringArray(R.array.version_values)
  val versionEntries = context.resources.getStringArray(R.array.version_entries)
  val colorScheme = MaterialTheme.colorScheme
  var showSheet by remember { mutableStateOf(false) }

  val viewModel = hiltViewModel<CategoryViewModel>()
  val categoryViewModelState = viewModel.state.collectAsState().value

  var isFocused by remember { mutableStateOf(false) }

  LaunchedEffect(state.onLangInvoke) {
    if (state.onLangInvoke) context.onTranslationChange(state.lang)
  }

  BackHandler(enabled = state.searchQuery.trim().isNotEmpty()) {
    onEvent(MainUiEvent.LyricUiEvent.ClearLyricUiQuery)
  }

  val scrollState = rememberLazyListState()
  Scaffold(
    topBar = {
      Column {
        Spacer(modifier = Modifier.height(20.dp))
        Column(
          modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 16.dp)
            .fillMaxWidth()
        ) {
          Text(
            text = "Discover",
            style = MaterialTheme.typography.displaySmall
              .copy(fontSize = 30.sp, fontWeight = FontWeight.Bold),
          )
          Text(text = "Which hymn are you looking for?")
        }

        SearchBox(
          state = state,
          onFocusRequester = { isFocused = it },
          event = onEvent
        )

        AnimatedVisibility(visible = state.searchQuery.trim().isEmpty()) {
          Column {
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow {
              items(state.search, key = { it.query }) {
                ElevatedCard(
                  onClick = {
                    onEvent(MainUiEvent.LyricUiEvent.LyricUiQueryTag(it.query))
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

    AnimatedVisibility(isFocused && state.searchQuery.trim().isNotEmpty()) {
      LazyColumn(modifier = Modifier.padding(it)) {
        items(state.lyrics, key = { it.lyricId }) {
          LyricScreenItem(
            lyric = it,
            onEvent = { event ->
              onEvent(MainUiEvent.LyricUiEvent.InsertLyricUiTag)
              onEvent(event)
            },
            modifier = Modifier.animateItem()
          )
        }
      }
    }

    AnimatedVisibility(state.searchQuery.trim().isEmpty()) {
      Column(
        modifier = Modifier
          .padding(it)
          .fillMaxSize()
          .verticalScroll(rememberScrollState())
      ) {

        Text(
          text = "Uniquely Crafted",
          style = MaterialTheme.typography.titleMedium,
          modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 26.dp)
            .fillMaxWidth()
        )

        if (state.uniquelyCrafted.isNotEmpty()) {
          LazyRow(state = scrollState) {
            items(state.uniquelyCrafted) {
              UniquelyCraftedScreen(it, onEvent)
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

          state.diveInto.forEach {
            DiveIntoItemScreen(it, onEvent)
          }

          if (showSheet) {
            ModalBottomSheet(onDismissRequest = { showSheet = false }) {
              LazyVerticalGrid(columns = GridCells.Fixed(1)) {
                items(categoryViewModelState.categories) {
                  CategoryItem(it) { event ->
                    showSheet = false
                    onEvent(event)
                  }
                }
              }
            }
          }

          if (state.uniquelyCrafted.isNotEmpty()) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier
                .clickable {
                  onEvent(MainUiEvent.CategoryUiEvent.OnViewCategories)
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
              items(state.uniquelyCrafted) {
                FeaturedCategoryItem(it, onEvent)
              }
            }

            Spacer(modifier = Modifier.height(20.dp))
          }
        }
      }
    }
  }

  // @Preview(showBackground = true)
  @Composable
  fun MainScreenPreview() {
    HymnTheme {
      MainScreen(
        state = MainUiState(
          uniquelyCrafted = listOf(Faker.lyric, Faker.lyric, Faker.lyric, Faker.lyric),
          diveInto = listOf(Faker.lyric, Faker.lyric, Faker.lyric, Faker.lyric)
        )
      ) {
      }
    }
  }
}
