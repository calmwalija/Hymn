package net.techandgraphics.hymn.ui.screen.main

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.SizeMode
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.ui.screen.category.CategoryEvent
import net.techandgraphics.hymn.ui.screen.category.CategoryScreenItem
import net.techandgraphics.hymn.ui.screen.main.components.DiveIntoItemScreen
import net.techandgraphics.hymn.ui.screen.main.components.UniquelyCraftedScreen
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
  var onLangInvoke by remember { mutableStateOf(false) }
  val colorScheme = MaterialTheme.colorScheme

  AnimatedVisibility(visible = state.uniquelyCrafted.isNotEmpty()) {
    LazyColumn {

      item {
        Spacer(modifier = Modifier.height(16.dp))
        Row(
          verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)
        ) {
          Text(
            text = "Uniquely Crafted",
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
            modifier = Modifier.weight(1f)
          )

          Card(
            shape = RoundedCornerShape(50),
            colors = CardDefaults.cardColors(
              containerColor = colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(
              defaultElevation = 1.dp
            ),
          ) {
            Row(
              modifier = Modifier.padding(vertical = 2.dp)
            ) {
              versionEntries
                .forEach {
                  val ifLang = versionValue[versionEntries.indexOf(it)] == state.lang
                  ElevatedCard(
                    modifier = Modifier
                      .padding(horizontal = 2.dp),
                    shape = RoundedCornerShape(50),
                    colors = CardDefaults.cardColors(
                      containerColor = if (ifLang.not()) colorScheme.surface else colorScheme.primary.copy(
                        alpha = .8f
                      ),
                      contentColor = if (ifLang) Color.White else colorScheme.primary.copy(
                        alpha = .8f
                      ),
                    ),
                    elevation = CardDefaults.cardElevation(),
                  ) {
                    Row(
                      modifier = Modifier
                        .clickable(enabled = onLangInvoke.not()) {
                          if (versionValue[versionEntries.indexOf(it)] != state.lang) {
                            onLangInvoke = true
                            onLanguageChange(versionValue[versionEntries.indexOf(it)])
                          }
                        }
                        .padding(10.dp),
                      verticalAlignment = Alignment.CenterVertically
                    ) {
                      Box {
                        this@Card.AnimatedVisibility(
                          visible = versionValue[
                            versionEntries.indexOf(
                              it
                            )
                          ] == state.lang
                        ) {
                          this@Card.AnimatedVisibility(visible = onLangInvoke.not()) {
                            Icon(
                              painter = painterResource(id = R.drawable.ic_book),
                              contentDescription = null,
                              modifier = Modifier.size(12.dp)
                            )
                          }

                          this@Card.AnimatedVisibility(visible = onLangInvoke) {
                            CircularProgressIndicator(
                              strokeWidth = 2.dp,
                              color = colorScheme.secondary,
                              modifier = Modifier.size(12.dp)
                            )
                          }
                        }
                      }
                      Text(
                        text = it,
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(start = 4.dp)
                      )
                    }
                  }
                }
            }
          }
        }

        if (state.uniquelyCrafted.isNotEmpty())
          UniquelyCraftedScreen(state.uniquelyCrafted.first(), mainEvent, readEvent)
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
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
            modifier = Modifier.weight(1f)
          )
          Row(
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(
              text = "Find More",
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.Bold,
              color = colorScheme.primary
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
              imageVector = Icons.Filled.KeyboardArrowRight,
              contentDescription = null,
              modifier = Modifier.size(20.dp),
              tint = colorScheme.primary
            )
          }
        }

        com.google.accompanist.flowlayout.FlowRow(
          mainAxisSize = SizeMode.Expand,
          mainAxisAlignment = FlowMainAxisAlignment.SpaceBetween,
          modifier = Modifier.padding(horizontal = 4.dp)
        ) {
          state.diveInto.forEach {
            DiveIntoItemScreen(it, readEvent)
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
            text = "Spotlight",
            style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
            modifier = Modifier.weight(1f)
          )
          Row(
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Text(
              text = "See All",
              style = MaterialTheme.typography.labelMedium,
              fontWeight = FontWeight.Bold,
              color = colorScheme.primary
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
              imageVector = Icons.Filled.KeyboardArrowRight,
              contentDescription = null,
              modifier = Modifier.size(20.dp),
              tint = colorScheme.primary
            )
          }
        }

        com.google.accompanist.flowlayout.FlowRow(
          mainAxisSize = SizeMode.Expand,
          mainAxisAlignment = FlowMainAxisAlignment.SpaceBetween,
          modifier = Modifier.padding(horizontal = 4.dp)
        ) {
          state.spotlight.forEach {
            CategoryScreenItem(it, categoryEvent)
          }
        }
      }
    }
  }
}
