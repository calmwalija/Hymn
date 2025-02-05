package net.techandgraphics.hymn.ui.activity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun StartupFailureDialog(onDismissRequest: () -> Unit) {
  Dialog(onDismissRequest = onDismissRequest) {
    Card {
      Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(32.dp)
      ) {
        Text(
          text = "The app couldn't start because some files are missing!",
          textAlign = TextAlign.Center,
          style = MaterialTheme.typography.titleMedium,
          modifier = Modifier.padding(bottom = 16.dp)
        )
        SmallFloatingActionButton(
          containerColor = MaterialTheme.colorScheme.primary,
          shape = RoundedCornerShape(8),
          onClick = { onDismissRequest() },
          modifier = Modifier.fillMaxWidth()
        ) { Text("Close App") }
      }
    }
  }
}
