package net.techandgraphics.hymn.ui.screen.component

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.toShort
import net.techandgraphics.hymn.toTimeAgo

enum class TimestampFormat { Long, Short }

@Composable
fun TimestampComponent(
  context: Context,
  lyric: Lyric,
  format: TimestampFormat = TimestampFormat.Long
) {
  AnimatedVisibility(visible = lyric.timestamp > 0L) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      Icon(
        painter = painterResource(id = R.drawable.ic_time),
        contentDescription = null,
        modifier = Modifier.padding(end = 4.dp)
      )
      Text(
        text = if (TimestampFormat.Long == format) lyric.timestamp.toTimeAgo(context) else
          lyric.timestamp.toTimeAgo(
            context
          ).toShort(),
        overflow = TextOverflow.Ellipsis,
        fontSize = MaterialTheme.typography.bodySmall.fontSize,
        letterSpacing = 0.sp
      )
    }
  }
}
