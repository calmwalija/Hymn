package net.techandgraphics.hymn.ui.screen.theCategory

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import net.techandgraphics.hymn.Constant
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.hymnCount
import net.techandgraphics.hymn.ui.components.EmptyState
import net.techandgraphics.hymn.ui.components.HymnTopAppBar
import net.techandgraphics.hymn.ui.components.NoAppBarInsets
import net.techandgraphics.hymn.ui.theme.Sizes
import net.techandgraphics.hymn.ui.theme.Space

/**
 * A single theme and the hymns in it.
 *
 * This screen previously had no app bar at all, so the only way out was the
 * system back gesture. It now carries a standard back affordance and a titled
 * artwork header.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TheCategoryScreen(
  state: TheCategoryState,
  onBack: () -> Unit,
  onEvent: (TheCategoryUiEvent) -> Unit,
) {
  val context = LocalContext.current
  val category = state.category.firstOrNull()
  val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

  Scaffold(
    contentWindowInsets = NoAppBarInsets,
    topBar = {
      HymnTopAppBar(
        title = category?.lyric?.categoryName.orEmpty(),
        onBack = onBack,
        scrollBehavior = scrollBehavior,
      )
    },
  ) { padding ->
    if (state.lyric.isEmpty()) {
      EmptyState(
        icon = Icons.AutoMirrored.Rounded.List,
        title = stringResource(R.string.categories_empty_title),
        message = stringResource(R.string.categories_empty_message),
        modifier = Modifier.padding(padding),
      )
      return@Scaffold
    }

    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding),
      contentPadding = PaddingValues(bottom = Space.lg),
    ) {
      if (category != null) {
        item {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .height(Sizes.categoryHeader),
          ) {
            AsyncImage(
              model = Constant.imageFor(category.lyric.categoryId),
              contentDescription = null,
              contentScale = ContentScale.Crop,
              modifier = Modifier.fillMaxSize(),
            )
            Box(
              modifier = Modifier
                .fillMaxSize()
                .background(
                  Brush.verticalGradient(
                    0f to Color.Transparent,
                    1f to Color.Black.copy(alpha = 0.8f),
                  ),
                ),
            )
            Column(
              modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(Space.md),
            ) {
              Text(
                text = category.lyric.categoryName,
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
              )
              Text(
                text = category.count.hymnCount(context),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.85f),
              )
            }
          }
        }
      }

      items(state.lyric, key = { it.lyricId }) { lyric ->
        CategorisationScreenItem(lyric, onEvent)
      }
    }
  }
}
