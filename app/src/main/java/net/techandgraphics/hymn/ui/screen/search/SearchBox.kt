package net.techandgraphics.hymn.ui.screen.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import net.techandgraphics.hymn.R

@Composable
fun ChatBoxScreen(
  inputText: String = "",
) {

  val lottieComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lf30_qhwxkcno))

  Card(
    modifier = Modifier.padding(horizontal = 8.dp),
    shape = RoundedCornerShape(50)
  ) {
    BasicTextField(
      value = inputText,
      onValueChange = { },
      modifier = Modifier
        .fillMaxWidth(),
      decorationBox = { innerTextField ->
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {

          Box(
            modifier = Modifier
              .padding(end = 8.dp),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              painter = painterResource(id = R.drawable.ic_search),
              contentDescription = null,
              modifier = Modifier
                .size(22.dp)
                .padding(1.dp)
            )
            LottieAnimation(
              composition = lottieComposition,
              iterations = LottieConstants.IterateForever,
              modifier = Modifier
                .size(22.dp)
            )
          }

          Box(
            modifier = Modifier
              .weight(1f)
              .padding(horizontal = 8.dp),
            contentAlignment = Alignment.CenterStart
          ) {
            innerTextField()
            Text(text = "Which hymn are you looking for?", color = Color.Gray)
          }
        }
      }
    )
  }
}
