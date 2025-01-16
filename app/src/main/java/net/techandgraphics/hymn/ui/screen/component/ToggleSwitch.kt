package net.techandgraphics.hymn.ui.screen.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.techandgraphics.hymn.ui.theme.HymnTheme
import kotlin.reflect.KClass

interface ToggleSwitchItem

@Composable
fun ToggleSwitch(
  title: String,
  tabSelected: Int,
  toggleSwitchItems: Collection<KClass<*>>,
  onEvent: (Int) -> Unit,
) {
  Row(
    modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Spacer(modifier = Modifier.height(8.dp))
    Text(
      text = title,
      maxLines = 1,
      overflow = TextOverflow.Ellipsis,
      style = MaterialTheme.typography.titleMedium,
      modifier = Modifier
        .padding(end = 4.dp)
        .weight(1f)
    )
    Card(
      shape = RoundedCornerShape(50),
      colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
      elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
      Row(modifier = Modifier.padding(vertical = 2.dp)) {
        toggleSwitchItems.forEachIndexed { indexOf, klass ->
          val activeTab = tabSelected == indexOf
          ElevatedCard(
            modifier = Modifier.padding(horizontal = 4.dp),
            shape = RoundedCornerShape(50),
            colors = CardDefaults.cardColors(
              containerColor = if (activeTab.not()) MaterialTheme.colorScheme.surface else
                MaterialTheme.colorScheme.primary.copy(alpha = .8f),
              contentColor = if (activeTab) Color.White else
                MaterialTheme.colorScheme.primary.copy(alpha = .8f),
            ),
            elevation = CardDefaults.cardElevation()
          ) {
            Row(
              modifier = Modifier
                .clickable(enabled = indexOf != tabSelected) { onEvent(indexOf) }
                .padding(12.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = klass.simpleName!!,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = 8.dp)
              )
            }
          }
        }
      }
    }
  }
}

@Composable
@Preview(showBackground = true)
fun ToggleSwitchPreview() {
  HymnTheme {
//    ToggleSwitch("Quick Search", 0) {
//
//    }
  }
}
