package net.techandgraphics.hymn.ui.screen.main.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import net.techandgraphics.hymn.Constant
import net.techandgraphics.hymn.Faker
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.data.local.entities.LyricEntity
import net.techandgraphics.hymn.toTimeAgo
import net.techandgraphics.hymn.ui.theme.HymnTheme
import kotlin.random.Random

@Composable
fun HymnOfTheDayScreen(data: LyricEntity = Faker.lyricEntity) {
  val context = LocalContext.current
  val ofTheDay by remember {
    mutableStateOf(Constant.ofTheDay[Random.nextInt(5)])
  }

  Box(
    modifier = Modifier
      .padding(8.dp)
      .fillMaxWidth()
      .height(IntrinsicSize.Max)
      .clip(RoundedCornerShape(8)),
  ) {

    AsyncImage(
      model = ofTheDay,
      contentDescription = null,
      contentScale = ContentScale.Crop,
      modifier = Modifier
        .fillMaxWidth()
    )

    Box(
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight()
        .background(Color(0x99000000))
    )

    Row(
      modifier = Modifier.padding(16.dp)
    ) {
      Column(
        modifier = Modifier.weight(1f)
      ) {
        Text(
          text = "Hymn Of\nThe Day",
          fontSize = MaterialTheme.typography.headlineMedium.fontSize,
          fontWeight = FontWeight.Bold,
          lineHeight = 32.sp,
          color = Color.White,
        )
        Text(
          text = "#${data.number} - ${data.title}",
          fontWeight = FontWeight.Bold,
          modifier = Modifier.padding(bottom = 8.dp, top = 2.dp),
          color = Color.White,
        )
        Text(
          text = data.content,
          maxLines = 3,
          overflow = TextOverflow.Ellipsis,
          fontSize = MaterialTheme.typography.bodyMedium.fontSize,
          lineHeight = MaterialTheme.typography.bodySmall.lineHeight,
          color = Color.White,
        )

        AnimatedVisibility(visibleState = MutableTransitionState(data.timestamp != 0L)) {
          Row(
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              painter = painterResource(id = R.drawable.ic_access_time),
              contentDescription = null,
              tint = Color.White,
              modifier = Modifier.padding(end = 4.dp)
            )
            Text(
              text = data.timestamp.toTimeAgo(context),
              color = Color.White,
              overflow = TextOverflow.Ellipsis,
              fontSize = MaterialTheme.typography.bodySmall.fontSize,
              letterSpacing = 0.sp
            )
          }
        }
      }

      IconButton(
        onClick = { }, modifier = Modifier.align(Alignment.Bottom)
      ) {
        Icon(
          imageVector = Icons.Default.FavoriteBorder,
          contentDescription = null,
          tint = Color.White
        )
      }
    }
  }
}

@Preview(showBackground = true)
@Composable
fun HymnOfTheDayScreenPreview() {
  HymnTheme {
    HymnOfTheDayScreen()
  }
}
