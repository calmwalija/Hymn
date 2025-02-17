package net.techandgraphics.hymn.ui.screen.preview

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.Constant
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.addRemoveFavoriteToast
import net.techandgraphics.hymn.toNumber
import net.techandgraphics.hymn.ui.screen.component.SwipeBothDir4Action

const val READ_FONT_SIZE_THRESH_HOLD = 15
const val READ_LINE_HEIGHT_THRESH_HOLD = 20

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewScreen(
  state: PreviewUiState,
  onEvent: (PreviewUiEvent) -> Unit
) {

  val context = LocalContext.current
  var fontSizeShow by remember { mutableStateOf(false) }
  val rotateDegree by animateFloatAsState(
    targetValue = if (state.currentTranslation == state.defaultTranslation) 0f else 180f,
    label = "Default Translation",
    animationSpec = tween(durationMillis = 1000, delayMillis = 400),
  )

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          Crossfade(state.currentLyric!!) { currentLyric ->
            ElevatedCard(
              enabled = state.currentTranslation == state.defaultTranslation,
              shape = CircleShape,
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
              onClick = { onEvent(PreviewUiEvent.GoToTheCategory) }
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
              ) {
                AsyncImage(
                  model = Constant.images[currentLyric.categoryId].drawableRes,
                  contentDescription = state.currentLyric.title,
                  contentScale = ContentScale.Crop,
                  modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(32.dp)
                    .clip(RoundedCornerShape(50))
                )
                Column {
                  Text(
                    text = currentLyric.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier.padding(end = 8.dp)
                  )
                  Text(
                    text = currentLyric.toNumber(),
                    style = MaterialTheme.typography.bodySmall
                  )
                }
              }
            }
          }
        },
        navigationIcon = {
          IconButton(onClick = { onEvent(PreviewUiEvent.PopBackStack) }) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Go Back"
            )
          }
        },
        actions = {
          Crossfade(
            targetState = if (state.currentLyric!!.favorite)
              Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
          ) { imageVector ->
            IconButton(
              enabled = state.currentTranslation == state.defaultTranslation,
              onClick = {
                context addRemoveFavoriteToast state.currentLyric
                onEvent(PreviewUiEvent.Favorite(state.currentLyric))
              },
            ) {
              Icon(
                imageVector = imageVector,
                contentDescription = "Favorite",
                modifier = Modifier.size(22.dp)
              )
            }
          }

          if (state.translations.size == 2) {
            IconButton(onClick = { onEvent(PreviewUiEvent.ChangeTranslation) }) {
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
              contentDescription = "Font",
              modifier = Modifier.size(22.dp)
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
        Box(modifier = Modifier.padding(24.dp)) {
          IconButton(
            enabled = state.gotToPrevHymn != -1,
            onClick = {
              scope.launch {
                isRevealed = false
                delay(300)
                onEvent(PreviewUiEvent.Invoke(state.gotToPrevHymn))
                onEvent(PreviewUiEvent.Analytics.GotoPreviousHymn(state.gotToPrevHymn))
              }
            }
          ) {
            Icon(
              painter = painterResource(R.drawable.ic_double_arrow_left),
              contentDescription = null,
              modifier = Modifier.size(42.dp),
              tint = tint(state.gotToPrevHymn != -1)
            )
          }
        }
      },
      rightActions = {
        Box(modifier = Modifier.padding(24.dp)) {
          IconButton(
            enabled = state.gotToNextHymn != -1,
            onClick = {
              scope.launch {
                isRevealed = false
                delay(300)
                onEvent(PreviewUiEvent.Invoke(state.gotToNextHymn))
                onEvent(PreviewUiEvent.Analytics.GotoNextHymn(state.gotToNextHymn))
              }
            }
          ) {
            Icon(
              painter = painterResource(R.drawable.ic_double_arrow_right),
              contentDescription = null,
              modifier = Modifier.size(42.dp),
              tint = tint(state.gotToNextHymn != -1)
            )
          }
        }
      },
      onRightExpanded = {
        isRevealed = true
        onEvent(PreviewUiEvent.Analytics.SwipeToRight)
      },
      onLeftExpanded = {
        isRevealed = true
        onEvent(PreviewUiEvent.Analytics.SwipeToLeft)
      },
    ) {

      var fontSize by remember { mutableIntStateOf(state.fontSize) }
      AnimatedContent(targetState = state.lyricsWithIndex) { lyricsWithIndex ->
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
              detectTapGestures(
                onDoubleTap = {
                  fontSize =
                    if (fontSize == MAX_FONT_SIZE) 1 else (fontSize + 4).coerceIn(1, MAX_FONT_SIZE)
                  onEvent(PreviewUiEvent.FontSize(fontSize))
                }
              )
            }
            .verticalScroll(rememberScrollState())
            .padding(paddingValues)
        ) {
          lyricsWithIndex.forEach { lyric ->
            Column(
              modifier = Modifier
                .padding(top = 16.dp)
                .padding(horizontal = 16.dp),
              horizontalAlignment = Alignment.CenterHorizontally,
            ) {
              Text(
                text = lyric.index,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.displaySmall.copy(
                  fontSize = state.fontSize.plus(READ_FONT_SIZE_THRESH_HOLD).times(2).sp
                ),
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
}

@Composable
private fun tint(goTo: Boolean) =
  if (!goTo) Color.LightGray.copy(alpha = .5f) else MaterialTheme.colorScheme.primary
