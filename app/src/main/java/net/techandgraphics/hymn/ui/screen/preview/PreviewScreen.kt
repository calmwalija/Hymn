package net.techandgraphics.hymn.ui.screen.preview

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.Constant
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.addRemoveFavoriteToast
import net.techandgraphics.hymn.ui.Route
import net.techandgraphics.hymn.ui.screen.component.SwipeBothDir4Action

const val READ_FONT_SIZE_THRESH_HOLD = 15
const val READ_LINE_HEIGHT_THRESH_HOLD = 20

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewScreen(
  state: PreviewUiState,
  navController: NavHostController,
  onEvent: (PreviewUiEvent) -> Unit
) {

  val context = LocalContext.current
  var fontSizeShow by remember { mutableStateOf(false) }
  val rotateDegree by animateFloatAsState(
    targetValue = if (state.translationInverse) 180f else 0f,
    label = "Rotate Icon",
    animationSpec = tween(durationMillis = 1000, delayMillis = 400)
  )

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          if (state.previewLyricKey.isNotEmpty()) {
            val lyric = state.previewLyricKey.first().lyric
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier
                .fillMaxSize()
                .clickable {
                  navController.navigate(Route.TheCategory(state.lyrics.first().lyric.categoryId))
                }
            ) {
              AsyncImage(
                model = Constant.images[lyric.categoryId].drawableRes,
                contentDescription = lyric.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                  .padding(horizontal = 4.dp)
                  .size(32.dp)
                  .clip(RoundedCornerShape(50))
              )
              Column {
                Text(
                  text = lyric.title,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis,
                  fontWeight = FontWeight.Bold,
                  style = MaterialTheme.typography.titleSmall
                )

                Text(
                  text = "#${lyric.number}",
                  style = MaterialTheme.typography.bodySmall
                )
              }
            }
          }
        },
        navigationIcon = {
          IconButton(onClick = { navController.popBackStack() }) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Go Back"
            )
          }
        },
        actions = {
          if (state.previewLyricKey.isNotEmpty()) {
            IconButton(
              onClick = {
                context addRemoveFavoriteToast state.previewLyricKey.first().lyric
                onEvent(PreviewUiEvent.Favorite(state.previewLyricKey.first().lyric))
              },
            ) {
              Icon(
                imageVector = if (state.previewLyricKey.first().lyric.favorite)
                  Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Favorite",
                modifier = Modifier.size(20.dp)
              )
            }
          }
          if (state.previewLyricKeyInverse.isNotEmpty()) {
            IconButton(
              onClick = { onEvent(PreviewUiEvent.TranslationInverse) },
              modifier = Modifier
                .padding(end = 8.dp)
            ) {
              Icon(
                painter = painterResource(id = R.drawable.ic_toggle_translation),
                contentDescription = "Translation",
                modifier = Modifier.rotate(rotateDegree)
              )
            }
          }
          IconButton(
            onClick = { fontSizeShow = true },
            modifier = Modifier
              .padding(end = 8.dp)
          ) {
            Icon(
              painter = painterResource(id = R.drawable.ic_font_size),
              contentDescription = "Font"
            )
          }
        },
        windowInsets = WindowInsets(top = 0.dp),
        modifier = Modifier.shadow(elevation = 8.dp),
      )
    },
  ) { paddingValues ->
    if (fontSizeShow) FontSizeDialog(state = state, onEvent = onEvent) { fontSizeShow = false }
    var isRevealed by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    SwipeBothDir4Action(
      isRevealed = isRevealed,
      leftActions = {
        Box(modifier = Modifier.padding(16.dp)) {
          ElevatedButton(
            enabled = state.gotToPrevHymn != -1,
            onClick = {
              scope.launch {
                isRevealed = false
                delay(300)
                onEvent(PreviewUiEvent.Invoke(state.gotToPrevHymn))
              }
            }
          ) {
            Icon(
              painter = painterResource(R.drawable.ic_double_arrow_left),
              contentDescription = "Edit"
            )
          }
        }
      },
      rightActions = {
        Box(modifier = Modifier.padding(16.dp)) {
          ElevatedButton(
            enabled = state.gotToNextHymn != -1,
            onClick = {
              scope.launch {
                isRevealed = false
                delay(300)
                onEvent(PreviewUiEvent.Invoke(state.gotToNextHymn))
              }
            }
          ) {
            Icon(
              painter = painterResource(R.drawable.icdouble_arrow_right),
              contentDescription = null,
            )
          }
        }
      },
      onRightExpanded = { isRevealed = true },
      onLeftExpanded = { isRevealed = true },
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .verticalScroll(rememberScrollState())
          .padding(paddingValues)
      ) {
        state.lyrics.forEach { lyric ->
          Column(
            modifier = Modifier.padding(top = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
          ) {

            Text(
              text = lyric.key,
              fontWeight = FontWeight.Bold,
              style = MaterialTheme.typography.displaySmall,
              color = MaterialTheme.colorScheme.primary
            )
            Text(
              text = lyric.lyric.content,
              fontStyle = if (lyric.lyric.chorus == 1) FontStyle.Italic else FontStyle.Normal,
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
              textAlign = TextAlign.Center,
              lineHeight = state.fontSize.plus(READ_LINE_HEIGHT_THRESH_HOLD).sp,
              fontSize = (state.fontSize.plus(READ_FONT_SIZE_THRESH_HOLD)).sp
            )
          }
        }
      }
    }
  }
}
