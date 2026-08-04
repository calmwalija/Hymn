package net.techandgraphics.hymn.ui.screen.main

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.ui.theme.HymnTheme

data class MenuTranslation(val translation: String, @DrawableRes val icon: Int)

/**
 * Home app-bar actions: a flag button that switches translation and an overflow
 * for the creed, prayer and settings. Both dropdowns now hang off their own
 * button rather than being nested inside another `IconButton`'s content, which
 * made the flag's whole hit area open a menu unexpectedly.
 */
@Composable
fun MainMenuItem(state: MainUiState, onEvent: (MainUiEvent) -> Unit) {
  var expanded by remember { mutableStateOf(false) }
  var translationExpanded by remember { mutableStateOf(false) }

  val context = LocalContext.current
  val translationEntries = context.resources.getStringArray(R.array.translation_entries)
  val menuTranslations = listOf(
    MenuTranslation(translationEntries.first(), R.drawable.im_translation_english),
    MenuTranslation(translationEntries.last(), R.drawable.im_translation_chichewa),
  )
  val currentTranslation = menuTranslations.firstOrNull {
    it.translation.contains(state.translation, ignoreCase = true)
  } ?: menuTranslations.first()

  Box {
    IconButton(
      onClick = {
        translationExpanded = true
        onEvent(MainUiEvent.AnalyticEvent.ShowTranslationDialog)
      },
    ) {
      Image(
        painter = painterResource(currentTranslation.icon),
        contentDescription = stringResource(R.string.action_switch_translation),
        modifier = Modifier
          .size(28.dp)
          .clip(CircleShape)
          .background(Color.White)
          .padding(4.dp),
        contentScale = ContentScale.Inside,
      )
    }

    DropdownMenu(
      expanded = translationExpanded,
      onDismissRequest = { translationExpanded = false },
    ) {
      menuTranslations.forEach { translation ->
        val selected = currentTranslation == translation
        DropdownMenuItem(
          enabled = !selected,
          text = { Text(translation.translation) },
          onClick = {
            onEvent(MainUiEvent.ChangeTranslation(translation.translation.take(2).lowercase()))
            translationExpanded = false
          },
          leadingIcon = {
            Image(
              painter = painterResource(translation.icon),
              contentDescription = null,
              modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(Color.White)
                .padding(4.dp),
              contentScale = ContentScale.Inside,
            )
          },
          trailingIcon = {
            if (selected) {
              Icon(
                imageVector = Icons.Rounded.CheckCircle,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = null,
              )
            }
          },
        )
      }
    }
  }

  Box {
    IconButton(
      onClick = {
        expanded = true
        onEvent(MainUiEvent.AnalyticEvent.ShowMenuDialog)
      },
    ) {
      Icon(Icons.Rounded.MoreVert, contentDescription = stringResource(R.string.nav_settings))
    }

    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
      DropdownMenuItem(
        text = { Text(stringResource(R.string.favorites)) },
        onClick = {
          expanded = false
          onEvent(MainUiEvent.AnalyticEvent.ShowFavoriteDialog)
          onEvent(MainUiEvent.MenuItem.Favorites)
        },
        leadingIcon = {
          BadgedBox(
            badge = {
              if (state.favorites.isNotEmpty()) Badge { Text(state.favorites.size.toString()) }
            },
          ) {
            Icon(Icons.Outlined.FavoriteBorder, contentDescription = null)
          }
        },
      )

      HorizontalDivider()

      DropdownMenuItem(
        text = { Text(stringResource(R.string.library_creed)) },
        onClick = {
          expanded = false
          onEvent(MainUiEvent.MenuItem.ApostlesCreed)
        },
        leadingIcon = {
          Icon(
            painter = painterResource(R.drawable.ic_creed),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
          )
        },
      )

      DropdownMenuItem(
        text = { Text(stringResource(R.string.library_prayer)) },
        onClick = {
          expanded = false
          onEvent(MainUiEvent.MenuItem.LordsPrayer)
        },
        leadingIcon = {
          Icon(
            painter = painterResource(R.drawable.ic_prayer),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
          )
        },
      )

      HorizontalDivider()

      DropdownMenuItem(
        text = { Text(stringResource(R.string.nav_settings)) },
        onClick = {
          expanded = false
          onEvent(MainUiEvent.AnalyticEvent.GotoSettingScreen)
          onEvent(MainUiEvent.MenuItem.Settings)
        },
        leadingIcon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
      )
    }
  }
}

@PreviewLightDark
@Composable
private fun MainMenuItemPreview() {
  HymnTheme {
    Box(contentAlignment = Alignment.Center) {
      MainMenuItem(state = MainUiState(), onEvent = {})
    }
  }
}
