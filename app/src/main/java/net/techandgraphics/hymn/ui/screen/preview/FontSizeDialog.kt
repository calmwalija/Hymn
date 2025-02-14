package net.techandgraphics.hymn.ui.screen.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import net.techandgraphics.hymn.ui.screen.preview.PreviewUiEvent.FontSize
import net.techandgraphics.hymn.ui.theme.HymnTheme

private const val MAX_FONT_SIZE = 32

@Composable
fun FontSizeDialog(
  state: PreviewUiState,
  onEvent: (PreviewUiEvent) -> Unit,
  onDismissRequest: () -> Unit
) {

  LaunchedEffect(key1 = Unit) { onEvent(PreviewUiEvent.Analytics.FontDialog) }

  Dialog(onDismissRequest = onDismissRequest) {
    Card {
      Column(
        modifier = Modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Text(
          text = "Change lyrics font size",
          modifier = Modifier.padding(horizontal = 32.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        ElevatedCard(shape = RoundedCornerShape(50)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            FontButton(
              imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
              enableWhen = state.fontSize > 1,
              onEvent = { onEvent(FontSize(state.fontSize.minus(1))) }
            )
            Text(
              text = "${state.fontSize}",
              style = MaterialTheme.typography.titleMedium,
              textAlign = TextAlign.Center,
              modifier = Modifier.padding(horizontal = 42.dp)
            )
            FontButton(
              imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
              enableWhen = state.fontSize < MAX_FONT_SIZE,
              onEvent = { onEvent(FontSize(state.fontSize.plus(1))) }
            )
          }
        }
      }
    }
  }
}

@Composable
private fun FontButton(
  imageVector: ImageVector,
  enableWhen: Boolean,
  onEvent: () -> Unit,
) {
  Card(
    shape = CircleShape,
    colors = CardDefaults.cardColors(
      containerColor = MaterialTheme.colorScheme.primary,
      contentColor = MaterialTheme.colorScheme.surface
    ),
    onClick = onEvent,
    enabled = enableWhen
  ) {
    Icon(
      imageVector = imageVector,
      contentDescription = imageVector.name,
      modifier = Modifier.size(52.dp)
    )
  }
}

@Preview
@Composable
fun FontSizeDialogPreview() {
  HymnTheme {
    FontSizeDialog(
      state = PreviewUiState(
        fontSize = 10
      ),
      onEvent = {
      }
    ) {
    }
  }
}
