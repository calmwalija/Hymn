package net.techandgraphics.hymn.ui.screen.miscellaneous

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import net.techandgraphics.hymn.R

data class Configs(val name: String, val icon: Int)

@Composable
fun MiscellaneousScreen() {

  val listOfConfigs = listOf(
    Configs("Theme", R.drawable.ic_theme),
    Configs("Feedback", R.drawable.ic_whatsapp),
    Configs("Rate", R.drawable.ic_rate)
  )

  LazyColumn {

    item {
      Spacer(modifier = Modifier.height(16.dp))
    }

    item {
      Column(
        modifier = Modifier
          .padding(16.dp)
      ) {
        Text(
          text = "About Hymn Book App",
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.primary,
          modifier = Modifier.padding(bottom = 4.dp),
        )

        Text(
          text = stringResource(id = R.string.about) + " " + stringResource(id = R.string.developer),
          style = MaterialTheme.typography.bodyMedium
        )
      }

      HorizontalDivider(
        modifier = Modifier
          .padding(vertical = 4.dp)
      )
    }
  }

  /*
  * theme setting
  * feedback
  * rating
  * favorite
  * */
}
