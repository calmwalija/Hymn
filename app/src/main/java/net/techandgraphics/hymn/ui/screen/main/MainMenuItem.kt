package net.techandgraphics.hymn.ui.screen.main

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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

data class Translation(val translation: String, @DrawableRes val icon: Int)

@Composable
fun MainMenuItem(onEvent: (MainUiEvent) -> Unit) {
  var expanded by remember { mutableStateOf(false) }
  var translationExpanded by remember { mutableStateOf(false) }

  val context = LocalContext.current
  val translationEntries = context.resources.getStringArray(R.array.translation_entries)
  val translations = listOf(
    Translation(translationEntries.first(), R.drawable.im_translation_english),
    Translation(translationEntries.last(), R.drawable.im_translation_chichewa),
  )

  IconButton(onClick = { expanded = true }) {
    Icon(Icons.Default.MoreVert, contentDescription = "Localized description")

    DropdownMenu(
      expanded = translationExpanded,
      onDismissRequest = { translationExpanded = false }
    ) {
      translations.forEach { translation ->
        DropdownMenuItem(
          text = { Text(translation.translation) },
          onClick = { onEvent(MainUiEvent.MenuItem.Settings) },
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
          trailingIcon = { }
        )
      }
    }

    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
      DropdownMenuItem(
        text = { Text("Translation") },
        onClick = { translationExpanded = true; expanded = false },
        leadingIcon = { Icon(painter = painterResource(R.drawable.ic_toggle_translation), null) },
        trailingIcon = { }
      )

      DropdownMenuItem(
        text = { Text("Favorites") },
        onClick = { expanded = false; onEvent(MainUiEvent.MenuItem.Settings) },
        leadingIcon = { Icon(Icons.Outlined.FavoriteBorder, contentDescription = null) }
      )

      DropdownMenuItem(
        text = { Text("Settings") },
        onClick = { expanded = false; onEvent(MainUiEvent.MenuItem.Settings) },
        leadingIcon = { Icon(Icons.Outlined.Settings, contentDescription = null) }
      )
    }
  }
}

@Preview
@Composable
fun MainMenuItemPreview() {
  MainMenuItem(onEvent = {})
}
