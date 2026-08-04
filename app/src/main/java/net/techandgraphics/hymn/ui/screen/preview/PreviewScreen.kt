package net.techandgraphics.hymn.ui.screen.preview

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.addRemoveFavoriteToast
import net.techandgraphics.hymn.toNumber
import net.techandgraphics.hymn.ui.components.BackButton
import net.techandgraphics.hymn.ui.components.CategoryArtwork
import net.techandgraphics.hymn.ui.components.NoAppBarInsets
import net.techandgraphics.hymn.ui.screen.component.SwipeBothDir4Action
import net.techandgraphics.hymn.ui.theme.Space

const val READ_FONT_SIZE_THRESH_HOLD = 15
const val READ_LINE_HEIGHT_THRESH_HOLD = 20

/**
 * The reader.
 *
 * The top app bar carries only identity and the favourite toggle. Text size and
 * the translation switch moved down to the reader bar — previously five actions
 * competed for the bar and squeezed the title to a few characters on a phone.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreviewScreen(
  state: PreviewUiState,
  onEvent: (PreviewUiEvent) -> Unit,
) {
  val context = LocalContext.current
  val currentLyric = state.currentLyric ?: return
  var fontSizeShow by remember { mutableStateOf(false) }
  val sameTranslation = state.currentTranslation == state.defaultTranslation

  Scaffold(
    contentWindowInsets = NoAppBarInsets,
    topBar = {
      TopAppBar(
        title = {
          Crossfade(currentLyric, label = "readerTitle") { lyric ->
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier
                .fillMaxWidth()
                .let { base ->
                  if (sameTranslation) {
                    base.clickable { onEvent(PreviewUiEvent.GoToTheCategory) }
                  } else {
                    base
                  }
                },
            ) {
              CategoryArtwork(
                categoryId = lyric.categoryId,
                modifier = Modifier.size(36.dp),
              )
              Column(modifier = Modifier.padding(start = Space.xs)) {
                Text(
                  text = lyric.title,
                  style = MaterialTheme.typography.titleSmall,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis,
                )
                Text(
                  text = lyric.categoryName,
                  style = MaterialTheme.typography.bodySmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis,
                )
              }
            }
          }
        },
        navigationIcon = {
          BackButton(onBack = { onEvent(PreviewUiEvent.PopBackStack) })
        },
        actions = {
          Crossfade(currentLyric.favorite, label = "readerFavorite") { favorite ->
            IconButton(
              enabled = sameTranslation,
              onClick = {
                context addRemoveFavoriteToast currentLyric
                onEvent(PreviewUiEvent.Favorite(currentLyric))
              },
            ) {
              Icon(
                imageVector = if (favorite) Icons.Rounded.Favorite
                else Icons.Rounded.FavoriteBorder,
                contentDescription = stringResource(
                  if (favorite) R.string.action_remove_favorite
                  else R.string.action_add_favorite,
                ),
                tint = if (favorite) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant,
              )
            }
          }
        },
        windowInsets = NoAppBarInsets,
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = MaterialTheme.colorScheme.surface,
        ),
      )
    },
    bottomBar = {
      VerseNavigationBar(
        hymnLabel = currentLyric.toNumber(),
        canGoPrev = state.gotToPrevHymn != -1,
        canGoNext = state.gotToNextHymn != -1,
        currentTranslation = state.currentTranslation,
        showTranslationToggle = state.translations.size == 2,
        translationEnabled = true,
        onTranslationChange = { onEvent(PreviewUiEvent.ChangeTranslation) },
        onTextSize = { fontSizeShow = true },
        onPrev = {
          onEvent(PreviewUiEvent.Invoke(state.gotToPrevHymn))
          onEvent(PreviewUiEvent.Analytics.GotoPreviousHymn(state.gotToPrevHymn))
        },
        onNext = {
          onEvent(PreviewUiEvent.Invoke(state.gotToNextHymn))
          onEvent(PreviewUiEvent.Analytics.GotoNextHymn(state.gotToNextHymn))
        },
      )
    },
  ) { paddingValues ->
    if (fontSizeShow) FontSizeDialog(state = state, onEvent = onEvent) { fontSizeShow = false }

    var isRevealed by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    SwipeBothDir4Action(
      isRevealed = isRevealed,
      leftActions = {
        SwipeAction(
          enabled = state.gotToPrevHymn != -1,
          iconRes = R.drawable.ic_double_arrow_left,
          contentDescription = stringResource(R.string.reader_previous),
          onClick = {
            scope.launch {
              isRevealed = false
              delay(300)
              onEvent(PreviewUiEvent.Invoke(state.gotToPrevHymn))
              onEvent(PreviewUiEvent.Analytics.GotoPreviousHymn(state.gotToPrevHymn))
            }
          },
        )
      },
      rightActions = {
        SwipeAction(
          enabled = state.gotToNextHymn != -1,
          iconRes = R.drawable.ic_double_arrow_right,
          contentDescription = stringResource(R.string.reader_next),
          onClick = {
            scope.launch {
              isRevealed = false
              delay(300)
              onEvent(PreviewUiEvent.Invoke(state.gotToNextHymn))
              onEvent(PreviewUiEvent.Analytics.GotoNextHymn(state.gotToNextHymn))
            }
          },
        )
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
      AnimatedContent(targetState = state.lyricsWithIndex, label = "readerVerses") { verses ->
        Column(
          modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
              detectTapGestures(
                onDoubleTap = {
                  fontSize = if (fontSize == MAX_FONT_SIZE) 1
                  else (fontSize + 4).coerceIn(1, MAX_FONT_SIZE)
                  onEvent(PreviewUiEvent.FontSize(fontSize))
                },
              )
            }
            .verticalScroll(rememberScrollState())
            .padding(paddingValues)
            .padding(bottom = Space.lg),
        ) {
          verses.forEach { verse ->
            Column(
              horizontalAlignment = Alignment.CenterHorizontally,
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Space.md, vertical = Space.sm),
            ) {
              Text(
                text = verse.index,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
              )
              Text(
                text = verse.lyric.content,
                fontStyle = if (verse.lyric.chorus == 1) FontStyle.Italic else FontStyle.Normal,
                color = if (verse.lyric.chorus == 1) MaterialTheme.colorScheme.onSurfaceVariant
                else MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                lineHeight = state.fontSize.plus(READ_LINE_HEIGHT_THRESH_HOLD).sp,
                fontSize = state.fontSize.plus(READ_FONT_SIZE_THRESH_HOLD).sp,
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = Space.md, vertical = Space.xs),
              )
            }
          }
        }
      }
    }
  }
}

@Composable
private fun SwipeAction(
  enabled: Boolean,
  iconRes: Int,
  contentDescription: String,
  onClick: () -> Unit,
) {
  Box(modifier = Modifier.padding(Space.lg)) {
    IconButton(enabled = enabled, onClick = onClick) {
      Icon(
        painter = painterResource(iconRes),
        contentDescription = contentDescription,
        modifier = Modifier.size(40.dp),
        tint = if (enabled) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
      )
    }
  }
}
