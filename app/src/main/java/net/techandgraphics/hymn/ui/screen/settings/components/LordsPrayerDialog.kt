package net.techandgraphics.hymn.ui.screen.settings.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.techandgraphics.hymn.ui.screen.preview.READ_FONT_SIZE_THRESH_HOLD
import net.techandgraphics.hymn.ui.screen.preview.READ_LINE_HEIGHT_THRESH_HOLD
import net.techandgraphics.hymn.ui.screen.settings.SettingsUiState

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun LordsPrayerDialog(
  state: SettingsUiState,
  onDismissRequest: () -> Unit
) {
  ModalBottomSheet(onDismissRequest = onDismissRequest) {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier.padding(horizontal = 16.dp)
    ) {
      Text(
        text = state.complementary.first().groupName,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.titleLarge
      )
      Spacer(modifier = Modifier.height(16.dp))
      Text(
        text = state.complementary.first().content,
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
        lineHeight = state.fontSize.plus(READ_LINE_HEIGHT_THRESH_HOLD).sp,
        fontSize = (state.fontSize.plus(READ_FONT_SIZE_THRESH_HOLD)).sp
      )
      Spacer(modifier = Modifier.height(32.dp))
    }
  }
}
