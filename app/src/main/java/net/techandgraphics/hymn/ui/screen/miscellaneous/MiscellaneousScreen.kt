package net.techandgraphics.hymn.ui.screen.miscellaneous

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import net.techandgraphics.hymn.R

@Composable
fun MiscellaneousScreen() {

  LazyColumn(
    modifier = Modifier.padding(8.dp)
  ) {

    item {
      Spacer(modifier = Modifier.height(16.dp))
      Text(
        text = "Miscellaneous",
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
      )
    }

    item {
      Card(
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier.padding(4.dp)
      ) {

        Row(
          modifier = Modifier.padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            painter = painterResource(id = R.drawable.ic_info),
            contentDescription = null,
            modifier = Modifier.size(28.dp)
          )
          Column(
            modifier = Modifier
              .padding(start = 24.dp)
          ) {
            Text(
              text = "About App",
              color = MaterialTheme.colorScheme.primary,
            )

            Text(
              text = stringResource(id = R.string.about),
              style = MaterialTheme.typography.bodyMedium
            )
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(32.dp))
      Text(
        text = "Contact",
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
      )
    }
    item {
      Card(
        elevation = CardDefaults.cardElevation(
          defaultElevation = 2.dp
        ),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier.padding(4.dp)
      ) {

        Row(
          modifier = Modifier.padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            painter = painterResource(id = R.drawable.ic_feedback),
            contentDescription = null,
            modifier = Modifier.size(28.dp),
          )
          Column(
            modifier = Modifier
              .padding(start = 24.dp)
          ) {
            Text(
              text = "Feedback",
              color = MaterialTheme.colorScheme.primary,
            )

            Text(
              text = stringResource(id = R.string.feedback),
              style = MaterialTheme.typography.bodyMedium
            )
          }
        }

        Divider()

        Row(
          modifier = Modifier.padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            painter = painterResource(id = R.drawable.ic_rate),
            contentDescription = null,
            modifier = Modifier.size(25.dp)
          )
          Column(
            modifier = Modifier
              .padding(start = 24.dp)
          ) {
            Text(
              text = "Rate & Review",
              color = MaterialTheme.colorScheme.primary,
            )

            Text(
              text = stringResource(id = R.string.rate),
              style = MaterialTheme.typography.bodyMedium
            )
          }
        }
      }
    }
  }

  /*
  * theme setting
  * feedback
  * rating
  * favorite
  * */
}
