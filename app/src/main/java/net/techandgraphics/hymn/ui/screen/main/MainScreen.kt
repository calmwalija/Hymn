package net.techandgraphics.hymn.ui.screen.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.ui.screen.category.CategoryEvent
import net.techandgraphics.hymn.ui.screen.category.CategoryScreenItem
import net.techandgraphics.hymn.ui.screen.main.components.HymnItemScreen
import net.techandgraphics.hymn.ui.screen.main.components.HymnOfTheDayScreen
import net.techandgraphics.hymn.ui.screen.read.ReadEvent

@Composable
fun MainScreen(
  state: MainState,
  categoryEvent: (CategoryEvent) -> Unit,
  readEvent: (ReadEvent) -> Unit,
  mainEvent: (MainEvent) -> Unit,
  navigator: (MainNavigator) -> Unit,
  onLanguageChange: (String) -> Unit
) {
  val context = LocalContext.current
  val versionValue = context.resources.getStringArray(R.array.version_values)
  val versionEntries = context.resources.getStringArray(R.array.version_entries)
  var expanded by remember { mutableStateOf(false) }
  var onLangInvoke by remember { mutableStateOf(false) }
  val rotateDegree by animateFloatAsState(
    targetValue = if (expanded) 180f else 0f,
    label = "Rotate Icon",
    animationSpec = tween(durationMillis = 500)
  )

  LazyColumn {

    item {
      Spacer(modifier = Modifier.height(16.dp))
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .padding(8.dp)
      ) {
        Text(
          text = "Uniquely Crafted",
          style = MaterialTheme.typography.titleLarge,
          modifier = Modifier.weight(1f)
        )

        Card(
          shape = RoundedCornerShape(50),
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
              .clickable(enabled = onLangInvoke.not()) { expanded = true }
              .padding(10.dp),
          ) {
            Box {
              this@Card.AnimatedVisibility(visible = onLangInvoke.not()) {
                Icon(
                  painter = painterResource(id = R.drawable.ic_book),
                  contentDescription = null,
                  modifier = Modifier.size(14.dp)
                )
              }

              this@Card.AnimatedVisibility(visible = onLangInvoke) {
                CircularProgressIndicator(
                  strokeWidth = 2.dp,
                  modifier = Modifier.size(14.dp)
                )
              }
            }

            Text(
              text = versionEntries[versionValue.indexOf(state.lang)],
              style = MaterialTheme.typography.labelSmall,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 4.dp),
              color = MaterialTheme.colorScheme.primary
            )

            Icon(
              imageVector = Icons.Default.KeyboardArrowDown,
              contentDescription = null,
              modifier = Modifier
                .rotate(rotateDegree)
                .size(16.dp)
            )
          }
          DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            versionEntries.forEach {
              DropdownMenuItem(
                text = { Text(text = it) },
                enabled = versionValue[versionEntries.indexOf(it)] != state.lang,
                onClick = {
                  expanded = false
                  onLangInvoke = true
                  onLanguageChange(versionValue[versionEntries.indexOf(it)])
                }
              )
            }
          }
        }
      }

      if (state.uniquelyCrafted.isNotEmpty())
        HymnOfTheDayScreen(state.uniquelyCrafted.first(), mainEvent, readEvent)
    }

    item {
      Spacer(modifier = Modifier.height(8.dp))

      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .clickable { navigator(MainNavigator.NavigateToSearch) }
          .padding(horizontal = 8.dp, vertical = 16.dp)
      ) {
        Text(
          text = "Dive Into",
          style = MaterialTheme.typography.titleLarge,
          modifier = Modifier
            .weight(1f)
        )
        Row(
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Text(
            text = "Find More",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
          )
          Spacer(modifier = Modifier.width(4.dp))
          Icon(
            imageVector = Icons.Filled.KeyboardArrowRight,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
          )
        }
      }

      com.google.accompanist.flowlayout.FlowRow(
        mainAxisSize = SizeMode.Expand,
        mainAxisAlignment = FlowMainAxisAlignment.SpaceBetween,
        modifier = Modifier
          .padding(horizontal = 4.dp)
      ) {
        state.theHymn.forEach {
          HymnItemScreen(it, readEvent)
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(16.dp))
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .clickable { navigator(MainNavigator.NavigateToCategory) }
          .padding(horizontal = 8.dp, vertical = 16.dp)
      ) {
        Text(
          text = "Spotlighted",
          style = MaterialTheme.typography.titleLarge,
          modifier = Modifier
            .weight(1f)
        )
        Row(
          verticalAlignment = Alignment.CenterVertically,
        ) {
          Text(
            text = "See All",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
          )
          Spacer(modifier = Modifier.width(4.dp))
          Icon(
            imageVector = Icons.Filled.KeyboardArrowRight,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
          )
        }
      }

      com.google.accompanist.flowlayout.FlowRow(
        mainAxisSize = SizeMode.Expand,
        mainAxisAlignment = FlowMainAxisAlignment.SpaceBetween,
        modifier = Modifier
          .padding(horizontal = 4.dp)
      ) {
        state.spotlighted.forEach {
          CategoryScreenItem(it, categoryEvent)
        }
      }
    }
  }
}
