package net.techandgraphics.hymn.ui.screen.preview

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import net.techandgraphics.hymn.data.local.Translation

@Composable
fun TranslationChip(
  currentTranslation: String,
  enabled: Boolean,
  onChange: () -> Unit,
) {
  Row(verticalAlignment = Alignment.CenterVertically) {
    listOf(Translation.EN, Translation.CH).forEach { lang ->
      val selected = currentTranslation.equals(lang.lowercase(), ignoreCase = true)
      FilterChip(
        selected = selected,
        onClick = { if (!selected && enabled) onChange() },
        enabled = enabled,
        label = { Text(lang.name) },
        modifier = Modifier.padding(end = 4.dp),
      )
    }
  }
}

@Composable
fun VerseNavigationBar(
  hymnLabel: String,
  canGoPrev: Boolean,
  canGoNext: Boolean,
  onPrev: () -> Unit,
  onNext: () -> Unit,
) {
  Surface(tonalElevation = 2.dp) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp, vertical = 4.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      IconButton(onClick = onPrev, enabled = canGoPrev) {
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Previous hymn")
      }
      Text(
        text = hymnLabel,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
      )
      IconButton(onClick = onNext, enabled = canGoNext) {
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next hymn")
      }
    }
  }
}
