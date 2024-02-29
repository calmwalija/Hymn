package net.techandgraphics.hymn.ui.screen.miscellaneous

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri.parse
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.getAppVersion
import net.techandgraphics.hymn.toTimeAgo
import net.techandgraphics.hymn.toast
import net.techandgraphics.hymn.ui.screen.read.READ_FONT_SIZE_THRESH_HOLD
import net.techandgraphics.hymn.ui.screen.read.READ_LINE_HEIGHT_THRESH_HOLD
import net.techandgraphics.hymn.ui.screen.read.ReadEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiscScreen(
  state: MiscState,
  readEvent: (ReadEvent) -> Unit,
  event: (MiscEvent) -> Unit,
) {

  val context = LocalContext.current
  val whatsAppUrl = "https://api.whatsapp.com/send?phone=+265993563408"
  val playStoreUrl = "https://play.google.com/store/apps/details?id=net.techandgraphics.hymn"

  var apostleCreedShow by remember { mutableStateOf(false) }
  var lordsPrayerShow by remember { mutableStateOf(false) }
  var favoriteShow by remember { mutableStateOf(false) }

  LazyColumn(
    modifier = Modifier.padding(horizontal = 8.dp)
  ) {

    item {
      Spacer(modifier = Modifier.height(24.dp))
      Text(
        text = "About Hymn Book App",
        style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
        modifier = Modifier.padding(bottom = 8.dp, start = 8.dp)
      )

      Card(
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier.padding(4.dp)
      ) {

        Row(
          modifier = Modifier.padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            painter = painterResource(id = R.drawable.ic_developer),
            contentDescription = null,
            modifier = Modifier.size(28.dp)
          )
          Column(
            modifier = Modifier
              .padding(start = 24.dp)
          ) {
            Text(
              text = "Developers",
              color = MaterialTheme.colorScheme.primary,
            )

            Text(
              text = stringResource(id = R.string.about),
              style = MaterialTheme.typography.bodyMedium
            )
          }
        }

        Divider(modifier = Modifier.padding(horizontal = 16.dp))

        getAppVersion(context)?.let {
          Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              painter = painterResource(id = R.drawable.ic_info),
              contentDescription = null,
              modifier = Modifier.size(28.dp)
            )
            Column(
              modifier = Modifier
                .padding(start = 24.dp)
            ) {
              Text(
                text = "App Info",
                color = MaterialTheme.colorScheme.primary,
              )
              Text(
                text = "Version ${it.name}.3725",
                style = MaterialTheme.typography.bodyMedium
              )
            }
          }
        }
      }
    }

    item {
      Spacer(modifier = Modifier.height(16.dp))
      Text(
        text = "Contact",
        style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
        modifier = Modifier.padding(bottom = 8.dp, start = 8.dp)
      )
      Card(
        elevation = CardDefaults.cardElevation(
          defaultElevation = 1.dp
        ),
        colors = CardDefaults.cardColors(
          containerColor = MaterialTheme.colorScheme.surface
        ),
        modifier = Modifier.padding(4.dp)
      ) {

        Row(
          modifier = Modifier
            .clickable { context.startActivity(Intent(ACTION_VIEW).setData(parse(whatsAppUrl))) }
            .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            painter = painterResource(id = R.drawable.ic_feedback),
            contentDescription = null,
            modifier = Modifier.size(28.dp),
          )
          Column(
            modifier = Modifier
              .padding(start = 24.dp)
          ) {
            Text(
              text = "Feedback",
              color = MaterialTheme.colorScheme.primary,
            )

            Text(
              text = stringResource(id = R.string.feedback),
              style = MaterialTheme.typography.bodyMedium
            )
          }
        }

        Divider()

        Row(
          modifier = Modifier
            .clickable { context.startActivity(Intent(ACTION_VIEW).setData(parse(playStoreUrl))) }
            .padding(16.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            painter = painterResource(id = R.drawable.ic_rate),
            contentDescription = null,
            modifier = Modifier.size(25.dp)
          )
          Column(
            modifier = Modifier
              .padding(start = 24.dp)
          ) {
            Text(
              text = "Rate & Review",
              color = MaterialTheme.colorScheme.primary,
            )

            Text(
              text = stringResource(id = R.string.rate),
              style = MaterialTheme.typography.bodyMedium
            )
          }
        }
      }
    }

    if (state.complementary.isNotEmpty()) {
      item {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
          text = "Complementary",
          style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
          modifier = Modifier.padding(bottom = 8.dp, start = 8.dp)
        )
        Card(
          elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
          ),
          colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
          ),
          modifier = Modifier.padding(4.dp)
        ) {

          if (favoriteShow && state.favorites.isNotEmpty()) {
            ModalBottomSheet(onDismissRequest = { favoriteShow = false }) {

              Text(
                text = "Favorite Hymns",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
              )

              Spacer(modifier = Modifier.height(16.dp))
              LazyColumn(
                state = rememberLazyListState()
              ) {
                items(state.favorites, key = { it.lyricId }) {
                  val dismissState = rememberDismissState(
                    confirmValueChange = { dismissValue ->
                      if (dismissValue == DismissValue.DismissedToEnd)
                        event(MiscEvent.RemoveFav(it))
                      true
                    },
                    positionalThreshold = { 150.dp.toPx() },
                  )
                  SwipeToDismiss(
                    state = dismissState,
                    directions = setOf(DismissDirection.StartToEnd),
                    background = {
                      Row(
                        modifier = Modifier
                          .fillMaxSize()
                          .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                      ) {
                        Icon(imageVector = Icons.Outlined.Delete, contentDescription = "Delete")
                        Text(text = "Delete", style = MaterialTheme.typography.labelMedium)
                      }
                    },
                    dismissContent = {
                      Card(
                        modifier = Modifier
                          .clickable {
                            readEvent(ReadEvent.Click(it.number))
                            favoriteShow = false
                          }
                          .fillMaxWidth()
                          .padding(8.dp),
                        shape = RoundedCornerShape(0),
                        colors = CardDefaults.cardColors(
                          containerColor = MaterialTheme.colorScheme.surface
                        )
                      ) {
                        Column(
                          modifier = Modifier
                            .padding(horizontal = 8.dp)
                        ) {
                          Text(
                            text = "#${it.number}",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary,
                          )
                          Text(
                            text = it.content.replace("\n", ""),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodyMedium,
                          )
                          Text(
                            text = it.categoryName.trimIndent(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.bodySmall,
                            textDecoration = TextDecoration.Underline,
                          )
                          Spacer(modifier = Modifier.height(2.dp))
                          AnimatedVisibility(visible = it.timestamp != 0L) {
                            Row(
                              verticalAlignment = Alignment.CenterVertically,
                            ) {
                              Icon(
                                painter = painterResource(id = R.drawable.ic_access_time),
                                contentDescription = null,
                                modifier = Modifier.padding(end = 4.dp)
                              )
                              Text(
                                text = it.timestamp.toTimeAgo(context),
                                overflow = TextOverflow.Ellipsis,
                                fontSize = MaterialTheme.typography.bodySmall.fontSize,
                                letterSpacing = 0.sp
                              )
                            }
                          }
                        }
                      }
                    }
                  )
                }
              }
              Spacer(modifier = Modifier.height(32.dp))
            }
          }

          Row(
            modifier = Modifier
              .clickable {
                if (state.favorites.isEmpty()) {
                  context toast context.getString(R.string.no_fav_hymn)
                  return@clickable
                }
                favoriteShow = true
              }
              .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            BadgedBox(
              badge = {
                if (state.favorites.isNotEmpty())
                  Badge(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                  ) {
                    Text(text = state.favorites.size.toString())
                  }
              }
            ) {
              Icon(
                painter = painterResource(id = R.drawable.ic_outline_favorite),
                contentDescription = null,
                modifier = Modifier.size(28.dp),
              )
            }
            Column(
              modifier = Modifier
                .padding(start = 24.dp)
            ) {
              Text(
                text = "Favorite Hymns",
                color = MaterialTheme.colorScheme.primary,
              )

              Text(
                text = stringResource(id = R.string.favorite),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
            }
          }

          Divider()

          Row(
            modifier = Modifier
              .clickable { lordsPrayerShow = true }
              .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {

            if (lordsPrayerShow) {
              ModalBottomSheet(onDismissRequest = { lordsPrayerShow = false }) {
                Column(
                  horizontalAlignment = Alignment.CenterHorizontally,
                  modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                  Text(
                    text = state.complementary.first().groupName,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge
                  )
                  Spacer(modifier = Modifier.height(16.dp))
                  Text(
                    text = state.complementary.first().content,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    lineHeight = state.fontSize.plus(READ_LINE_HEIGHT_THRESH_HOLD).sp,
                    fontSize = (state.fontSize.plus(READ_FONT_SIZE_THRESH_HOLD)).sp
                  )
                  Spacer(modifier = Modifier.height(32.dp))
                }
              }
            }

            Icon(
              painter = painterResource(id = R.drawable.ic_prayer),
              contentDescription = null,
              modifier = Modifier.size(25.dp)
            )
            Column(
              modifier = Modifier
                .padding(start = 24.dp)
            ) {
              Text(
                text = state.complementary.first().groupName,
                color = MaterialTheme.colorScheme.primary,
              )

              Text(
                text = state.complementary.first().content.replace("\n", ""),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
            }
          }

          Divider()

          Row(
            modifier = Modifier
              .clickable { apostleCreedShow = true }
              .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {

            if (apostleCreedShow) {
              ModalBottomSheet(onDismissRequest = { apostleCreedShow = false }) {
                Column(
                  horizontalAlignment = Alignment.CenterHorizontally,
                  modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                  Text(
                    text = state.complementary.last().groupName,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleLarge
                  )
                  Spacer(modifier = Modifier.height(16.dp))
                  Text(
                    text = state.complementary.last().content,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    lineHeight = state.fontSize.plus(READ_LINE_HEIGHT_THRESH_HOLD).sp,
                    fontSize = (state.fontSize.plus(READ_FONT_SIZE_THRESH_HOLD)).sp
                  )
                  Spacer(modifier = Modifier.height(32.dp))
                }
              }
            }

            Icon(
              painter = painterResource(id = R.drawable.ic_creed),
              contentDescription = null,
              modifier = Modifier.size(25.dp)
            )
            Column(
              modifier = Modifier
                .padding(start = 24.dp)
            ) {
              Text(
                text = state.complementary.last().groupName,
                color = MaterialTheme.colorScheme.primary,
              )
              Text(
                text = state.complementary.last().content.replace("\n", ""),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
              )
            }
          }
        }
      }
    }
  }
}
