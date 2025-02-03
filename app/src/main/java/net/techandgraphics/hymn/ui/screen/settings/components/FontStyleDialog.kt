package net.techandgraphics.hymn.ui.screen.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import net.techandgraphics.hymn.ui.screen.settings.SettingsUiEvent
import net.techandgraphics.hymn.ui.screen.settings.SettingsUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FontStyleDialog(
  state: SettingsUiState,
  onEvent: (SettingsUiEvent) -> Unit,
  onDismissRequest: () -> Unit
) {
  ModalBottomSheet(onDismissRequest = onDismissRequest) {
    Column {

      Text(
        text = "App Font Style",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.padding(start = 32.dp, bottom = 8.dp),
      )

      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onEvent(SettingsUiEvent.Font.Default) }
          .padding(horizontal = 16.dp, vertical = 8.dp)
      ) {
        RadioButton(
          selected = state.fontFamily.isNullOrEmpty(),
          onClick = { onEvent(SettingsUiEvent.Font.Default) },
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = "Default")
      }

      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .fillMaxWidth()
          .clickable { onEvent(SettingsUiEvent.Font.Choose) }
          .padding(horizontal = 16.dp, vertical = 8.dp)
      ) {
        RadioButton(
          selected = state.fontFamily.isNullOrBlank().not(),
          onClick = { onEvent(SettingsUiEvent.Font.Choose) },
        )
        Spacer(modifier = Modifier.width(4.dp))
        Column(verticalArrangement = Arrangement.Center) {
          Text(text = "Choose font")
          state.fontFamily?.let {
            Text(
              text = it,
              maxLines = 1,
              overflow = TextOverflow.Ellipsis,
              style = MaterialTheme.typography.labelSmall,
              color = MaterialTheme.colorScheme.primary,
              fontWeight = FontWeight.Bold
            )
          }
        }
      }
    }
  }
}
