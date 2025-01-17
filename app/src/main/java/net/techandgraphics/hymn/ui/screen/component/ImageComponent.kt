package net.techandgraphics.hymn.ui.screen.component

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import net.techandgraphics.hymn.Constant
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.domain.model.Lyric

@Composable
fun ImageComponent(lyric: Lyric) {
  AsyncImage(
    model = Constant.images[lyric.categoryId].drawableRes,
    contentDescription = null,
    contentScale = ContentScale.Crop,
    modifier = Modifier
      .clip(RoundedCornerShape(20))
      .size(82.dp),
    placeholder = painterResource(id = R.drawable.im_help)
  )
}
