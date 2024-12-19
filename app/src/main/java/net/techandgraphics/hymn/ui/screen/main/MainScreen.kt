package net.techandgraphics.hymn.ui.screen.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.techandgraphics.hymn.Faker
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.data.local.Lang
import net.techandgraphics.hymn.onTranslationChange
import net.techandgraphics.hymn.ui.screen.component.ToggleSwitch
import net.techandgraphics.hymn.ui.screen.component.ToggleSwitchItem
import net.techandgraphics.hymn.ui.screen.main.components.DiveIntoItemScreen
import net.techandgraphics.hymn.ui.screen.main.components.SpotlightItem
import net.techandgraphics.hymn.ui.screen.main.components.UniquelyCraftedScreen
import net.techandgraphics.hymn.ui.theme.HymnTheme
import net.techandgraphics.hymn.ui.theme.Typography

open class ToggleSwitchHomeItem : ToggleSwitchItem {
  data object English : ToggleSwitchHomeItem()
  data object Chichewa : ToggleSwitchHomeItem()
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MainScreen(
  state: MainState,
  onEvent: (MainEvent) -> Unit,
) {

  val context = LocalContext.current
  val versionValue = context.resources.getStringArray(R.array.version_values)
  val versionEntries = context.resources.getStringArray(R.array.version_entries)
  val colorScheme = MaterialTheme.colorScheme
  LaunchedEffect(state.onLangInvoke) {
    if (state.onLangInvoke) context.onTranslationChange(state.lang)
  }

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
  ) {
    var tabSelected by remember { mutableStateOf(state.lang) }
    ToggleSwitch(
      title = "Uniquely Crafted",
      toggleSwitchItems = ToggleSwitchHomeItem::class.nestedClasses.sortedByDescending { it.simpleName },
      tabSelected = if (tabSelected.contains(Lang.EN.lowercase(), ignoreCase = true)) 0 else 1
    ) {
      tabSelected = versionEntries[it]
      onEvent(MainEvent.Language(versionValue[versionEntries.indexOf(tabSelected)]))
    }

    if (state.uniquelyCrafted.isNotEmpty())

      LazyRow {
        items(state.uniquelyCrafted) {
          UniquelyCraftedScreen(it, onEvent)
        }
      }

    if (state.diveInto.isNotEmpty()) {
      Spacer(modifier = Modifier.height(8.dp))

      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .clickable { onEvent(MainEvent.Goto(MainEvent.Navigate.Search)) }
          .padding(horizontal = 8.dp, vertical = 16.dp)
      ) {
        Text(
          text = "Dive Into",
          style = Typography.titleMedium,
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
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = colorScheme.primary
          )
        }
      }

      FlowRow {
        state.diveInto.forEach {
          DiveIntoItemScreen(it) {
          }
        }
      }
    }

    if (state.uniquelyCrafted.isNotEmpty()) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
          .clickable { onEvent(MainEvent.Goto(MainEvent.Navigate.Category)) }
          .padding(horizontal = 8.dp, vertical = 16.dp)
      ) {
        Text(
          text = "Featured Categories",
          style = Typography.titleMedium,
          modifier = Modifier.weight(1f)
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
          Text(
            text = "See All",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = colorScheme.primary
          )
          Spacer(modifier = Modifier.width(4.dp))
          Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = colorScheme.primary
          )
        }
      }

      LazyRow {
        items(state.uniquelyCrafted) {
          SpotlightItem(it, onEvent)
        }
      }
    }
  }
}

@Composable
@Preview(showBackground = true)
fun MainScreenPreview() {
  HymnTheme {
    MainScreen(
      state = MainState(
        uniquelyCrafted = listOf(Faker.lyric, Faker.lyric, Faker.lyric, Faker.lyric),
        diveInto = listOf(Faker.lyric, Faker.lyric)
      )
    ) {
    }
  }
}
