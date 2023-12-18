package net.techandgraphics.hymn.ui.screen.read

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import net.techandgraphics.hymn.Constant
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.ui.Route

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun ReadScreen(
  state: ReadState,
  navController: NavHostController,
  event: (ReadEvent) -> Unit
) {

  var indexPosition = 1

  Scaffold(
    topBar = {
      TopAppBar(
        title = {
          if (state.lyrics.isNotEmpty()) {
            val lyric = state.lyrics.first()
            event(ReadEvent.Tag(lyric))
            Row(
              verticalAlignment = Alignment.CenterVertically
            ) {

              AsyncImage(
                model = Constant.images[lyric.categoryId].drawableRes,
                contentDescription = lyric.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                  .padding(end = 8.dp)
                  .size(32.dp)
                  .clip(RoundedCornerShape(50))
              )

              Column {
                Text(
                  text = lyric.title,
                  maxLines = 1,
                  overflow = TextOverflow.Ellipsis,
                  fontWeight = FontWeight.Bold,
                  style = MaterialTheme.typography.titleMedium
                )

                Text(
                  text = "#${lyric.number}",
                  style = MaterialTheme.typography.bodySmall
                )
              }
            }
          }
        },
        navigationIcon = {
          IconButton(
            onClick = {
              navController.popBackStack(Route.Read.route, inclusive = true)
            }
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Go Back"
            )
          }
        },
        actions = {
          if (state.lyrics.isNotEmpty()) {
            IconButton(
              onClick = { event(ReadEvent.Favorite(state.lyrics.first())) },
            ) {
              Icon(
                imageVector = if (state.lyrics.first().favorite)
                  Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Favorite",
                modifier = Modifier.size(20.dp)
              )
            }
          }
          IconButton(
            onClick = { },
            modifier = Modifier
              .padding(end = 8.dp)
          ) {
            Icon(
              painter = painterResource(id = R.drawable.ic_font),
              contentDescription = "Font"
            )
          }
        },
        modifier = Modifier
          .shadow(elevation = 8.dp),
      )
    },
  ) { paddingValues ->
    LazyColumn(
      contentPadding = paddingValues
    ) {
      items(state.lyrics, key = { it.lyricId }) { lyric ->

        Column(
          modifier = Modifier
            .padding(top = 16.dp),
          horizontalAlignment = Alignment.CenterHorizontally,
        ) {

          Text(
            text = if (lyric.chorus == 1) "Chorus" else (indexPosition++).toString(),
            fontWeight = FontWeight.Bold,
            fontSize = MaterialTheme.typography.displaySmall.fontSize,
            color = MaterialTheme.colorScheme.primary
          )

          Text(
            text = lyric.content,
            fontStyle = if (lyric.chorus == 1) FontStyle.Italic else FontStyle.Normal,
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp, vertical = 8.dp),
            textAlign = TextAlign.Center
          )
        }
      }
    }
  }
}
