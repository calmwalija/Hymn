package net.techandgraphics.hymn.ui.screen.preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.data.local.Translation
import net.techandgraphics.hymn.ui.theme.PillShape
import net.techandgraphics.hymn.ui.theme.Space

/**
 * Compact EN/CH toggle. Two full-size `FilterChip`s used to sit in the reader's
 * app bar alongside three other actions and pushed the title out of the way; a
 * single two-segment pill does the same job in a third of the width.
 */
@Composable
fun TranslationChip(
  currentTranslation: String,
  enabled: Boolean,
  onChange: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Surface(
    shape = PillShape,
    color = MaterialTheme.colorScheme.surfaceContainerHighest,
    modifier = modifier,
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.padding(2.dp),
    ) {
      listOf(Translation.EN, Translation.CH).forEach { lang ->
        val selected = currentTranslation.equals(lang.lowercase(), ignoreCase = true)
        Surface(
          shape = PillShape,
          color = if (selected) MaterialTheme.colorScheme.primary
          else MaterialTheme.colorScheme.surfaceContainerHighest,
          contentColor = if (selected) MaterialTheme.colorScheme.onPrimary
          else MaterialTheme.colorScheme.onSurfaceVariant,
          onClick = { if (!selected && enabled) onChange() },
          enabled = enabled && !selected,
        ) {
          Text(
            text = lang.name,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = Space.xs, vertical = Space.xxs),
          )
        }
      }
    }
  }
}

/**
 * Reader bottom bar: previous / next hymn plus the reading controls that used
 * to crowd the top app bar.
 */
@Composable
fun VerseNavigationBar(
  hymnLabel: String,
  canGoPrev: Boolean,
  canGoNext: Boolean,
  currentTranslation: String,
  showTranslationToggle: Boolean,
  translationEnabled: Boolean,
  onTranslationChange: () -> Unit,
  onTextSize: () -> Unit,
  onPrev: () -> Unit,
  onNext: () -> Unit,
) {
  Surface(
    tonalElevation = 3.dp,
    color = MaterialTheme.colorScheme.surfaceContainer,
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = Space.xs, vertical = Space.xxs),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      IconButton(onClick = onPrev, enabled = canGoPrev) {
        Icon(
          imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
          contentDescription = stringResource(R.string.reader_previous),
        )
      }

      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          text = hymnLabel,
          style = MaterialTheme.typography.titleMedium,
        )
        Spacer(Modifier.width(Space.xs))
        if (showTranslationToggle) {
          TranslationChip(
            currentTranslation = currentTranslation,
            enabled = translationEnabled,
            onChange = onTranslationChange,
          )
          Spacer(Modifier.width(Space.xxs))
        }
        IconButton(onClick = onTextSize) {
          Icon(
            painter = painterResource(R.drawable.ic_font_size),
            contentDescription = stringResource(R.string.action_text_size),
            modifier = Modifier.size(20.dp),
          )
        }
      }

      IconButton(onClick = onNext, enabled = canGoNext) {
        Icon(
          imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
          contentDescription = stringResource(R.string.reader_next),
        )
      }
    }
  }
}
