package net.techandgraphics.hymn.ui.screen.preview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.ui.theme.HymnTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FontSizeDialog(
  state: PreviewUiState,
  onEvent: (PreviewUiEvent) -> Unit,
  onDismissRequest: () -> Unit
) {
  Dialog(onDismissRequest = onDismissRequest) {
    Card(
      modifier = Modifier.padding(16.dp),
      colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface
      ),
      elevation = CardDefaults.cardElevation(
        defaultElevation = 1.dp
      ),
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Text(
          text = "Change lyrics font size",
          modifier = Modifier.padding(start = 16.dp, top = 4.dp),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
          Slider(
            value = state.fontSize.toFloat(),
            onValueChange = { onEvent(PreviewUiEvent.FontSize(it.toInt())) },
            valueRange = 1f..24f,
            modifier = Modifier
              .weight(1f)
              .padding(end = 16.dp),
            colors = SliderDefaults.colors(
              thumbColor = MaterialTheme.colorScheme.primary,
              activeTrackColor = MaterialTheme.colorScheme.primary,
            ),
            thumb = {
              Icon(
                painter = painterResource(id = R.drawable.ic_filled_circle),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
              )
            },
          )
          Text(
            text = state.fontSize.toString(),
            modifier = Modifier.padding(end = 8.dp)
          )
        }
      }
    }
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
