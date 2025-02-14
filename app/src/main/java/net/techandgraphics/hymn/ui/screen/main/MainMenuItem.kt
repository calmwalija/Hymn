package net.techandgraphics.hymn.ui.screen.main

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import net.techandgraphics.hymn.R

data class MenuTranslation(val translation: String, @DrawableRes val icon: Int)

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
  val currentTranslation = menuTranslations.first {
    it.translation.contains(state.translation, ignoreCase = true)
  }

  IconButton(
    onClick = {
      translationExpanded = true
      onEvent(MainUiEvent.AnalyticEvent.ShowTranslationDialog)
    }
  ) {
    Card(
      shape = CircleShape,
      colors = CardDefaults.cardColors(containerColor = Color.White),
      elevation = CardDefaults.elevatedCardElevation(defaultElevation = 1.dp)
    ) {
      Image(
        painter = painterResource(currentTranslation.icon),
        contentDescription = null,
        modifier = Modifier
          .clip(RoundedCornerShape(50))
          .size(28.dp)
          .padding(6.dp),
        contentScale = ContentScale.Inside
      )
    }

    DropdownMenu(
      expanded = translationExpanded,
      onDismissRequest = { translationExpanded = false }
    ) {
      menuTranslations.forEach { translation ->
        DropdownMenuItem(
          colors = MenuDefaults.itemColors(
            disabledTextColor = if (currentTranslation == translation) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
          ),
          enabled = currentTranslation != translation,
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
                .clip(RoundedCornerShape(50))
                .background(Color.White)
                .size(24.dp)
                .padding(4.dp),
              contentScale = ContentScale.Inside
            )
          },
          trailingIcon = {
            if (currentTranslation == translation) Icon(
              imageVector = Icons.Default.CheckCircle,
              tint = MaterialTheme.colorScheme.onSurface,
              contentDescription = null
            )
          }
        )
      }
    }
  }

  IconButton(onClick = { expanded = true; onEvent(MainUiEvent.AnalyticEvent.ShowMenuDialog) }) {
    BadgedBox(badge = { if (state.favorites.isNotEmpty()) Badge() }) {
      Icon(Icons.Default.MoreVert, contentDescription = "MoreVert")
    }
    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {

      DropdownMenuItem(
        text = { Text("Favorite Hymns") },
        onClick = {
          expanded = false
          onEvent(MainUiEvent.AnalyticEvent.ShowFavoriteDialog)
          onEvent(MainUiEvent.MenuItem.Favorites)
        },
        leadingIcon = {
          BadgedBox(
            badge = {
              if (state.favorites.isNotEmpty()) Badge { Text(state.favorites.size.toString()) }
            }
          ) {
            Icon(Icons.Outlined.FavoriteBorder, contentDescription = null)
          }
        },
        trailingIcon = { }
      )

      HorizontalDivider()

      DropdownMenuItem(
        text = { Text("Apostles Creed") },
        onClick = {
          expanded = false
          onEvent(MainUiEvent.AnalyticEvent.ShowApostlesCreedDialog)
          onEvent(MainUiEvent.MenuItem.ApostlesCreed)
        },
        leadingIcon = {
          Icon(
            painter = painterResource(R.drawable.ic_creed),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
          )
        },
        trailingIcon = {}
      )

      DropdownMenuItem(
        text = { Text("Lords Prayer") },
        onClick = {
          expanded = false
          onEvent(MainUiEvent.AnalyticEvent.ShowLordsPrayerDialog)
          onEvent(MainUiEvent.MenuItem.LordsPrayer)
        },
        leadingIcon = {
          Icon(
            painter = painterResource(R.drawable.ic_prayer),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
          )
        },
        trailingIcon = {}
      )

      HorizontalDivider()

      DropdownMenuItem(
        text = { Text("Settings") },
        onClick = {
          expanded = false
          onEvent(MainUiEvent.AnalyticEvent.GotoSettingScreen)
          onEvent(MainUiEvent.MenuItem.Settings)
        },
        leadingIcon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
        trailingIcon = {}
      )
    }
  }
}

@Preview @Composable fun MainMenuItemPreview() {
  MainMenuItem(state = MainUiState(), onEvent = {})
}
