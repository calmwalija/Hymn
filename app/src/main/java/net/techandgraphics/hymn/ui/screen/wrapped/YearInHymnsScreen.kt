package net.techandgraphics.hymn.ui.screen.wrapped

import android.content.Intent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.domain.model.YearInHymnsReport
import net.techandgraphics.hymn.ui.components.EmptyState
import net.techandgraphics.hymn.ui.theme.PillShape
import net.techandgraphics.hymn.ui.theme.Space
import java.util.concurrent.TimeUnit

private data class WrappedSlide(
  val kicker: String,
  val headline: String,
  val detail: String,
)

/**
 * The year-in-review story. Slides are typed rather than raw newline-delimited
 * strings, which lets each one set a real typographic hierarchy — kicker,
 * headline, detail — instead of one centred paragraph.
 */
@Composable
fun YearInHymnsScreen(
  state: WrappedUiState,
  onClose: () -> Unit,
) {
  val report = state.report

  if (state.loading) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      Column(horizontalAlignment = Alignment.CenterHorizontally) {
        CircularProgressIndicator()
        Spacer(Modifier.height(Space.md))
        Text(
          text = stringResource(R.string.wrapped_loading),
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }
    return
  }

  if (report == null || report.summary.totalVisits == 0L) {
    Box(Modifier.fillMaxSize()) {
      EmptyState(
        icon = Icons.Rounded.Star,
        title = stringResource(R.string.wrapped_empty_title),
        message = stringResource(R.string.wrapped_empty_message),
        action = {
          Button(onClick = onClose, shape = PillShape) {
            Text(stringResource(R.string.action_close))
          }
        },
      )
    }
    return
  }

  val slides = remember(report) { buildSlides(report) }
  val pagerState = rememberPagerState { slides.size }
  val context = LocalContext.current

  // The gradient is painted from `primary`/`tertiary`, which are light tones in
  // dark mode. Hard-coding white text on top left the story nearly unreadable
  // there, so all content colours come off `onPrimary` and flip with the theme.
  val onStory = MaterialTheme.colorScheme.onPrimary

  Box(modifier = Modifier.fillMaxSize()) {
    HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(
            Brush.linearGradient(
              listOf(
                MaterialTheme.colorScheme.primary,
                MaterialTheme.colorScheme.tertiary,
              ),
            ),
          )
          .padding(Space.xl),
        contentAlignment = Alignment.Center,
      ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = slides[page].kicker.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            color = onStory.copy(alpha = 0.8f),
            textAlign = TextAlign.Center,
          )
          Spacer(Modifier.height(Space.sm))
          Text(
            text = slides[page].headline,
            style = MaterialTheme.typography.displaySmall,
            color = onStory,
            textAlign = TextAlign.Center,
          )
          if (slides[page].detail.isNotBlank()) {
            Spacer(Modifier.height(Space.sm))
            Text(
              text = slides[page].detail,
              style = MaterialTheme.typography.titleMedium,
              color = onStory.copy(alpha = 0.9f),
              textAlign = TextAlign.Center,
            )
          }
        }
      }
    }

    IconButton(
      onClick = onClose,
      modifier = Modifier
        .align(Alignment.TopEnd)
        .padding(Space.xs),
    ) {
      Icon(
        imageVector = Icons.Rounded.Close,
        contentDescription = stringResource(R.string.action_close),
        tint = onStory,
      )
    }

    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .fillMaxWidth()
        .padding(Space.md),
    ) {
      Row(horizontalArrangement = Arrangement.spacedBy(Space.xxs)) {
        repeat(slides.size) { index ->
          val color by animateColorAsState(
            if (index == pagerState.currentPage) onStory
            else onStory.copy(alpha = 0.35f),
            label = "wrappedDot",
          )
          Box(
            modifier = Modifier
              .size(8.dp)
              .clip(PillShape)
              .background(color),
          )
        }
      }
      Spacer(Modifier.height(Space.md))
      Button(
        onClick = {
          context.startActivity(
            Intent.createChooser(
              Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, buildShareText(report))
              },
              context.getString(R.string.wrapped_share_title),
            ),
          )
        },
        shape = PillShape,
        colors = ButtonDefaults.buttonColors(
          containerColor = onStory,
          contentColor = MaterialTheme.colorScheme.primary,
        ),
        modifier = Modifier.fillMaxWidth(),
      ) {
        Icon(Icons.Rounded.Share, contentDescription = null, modifier = Modifier.size(18.dp))
        Spacer(Modifier.size(Space.xs))
        Text(stringResource(R.string.action_share))
      }
    }
  }
}

private fun buildSlides(report: YearInHymnsReport): List<WrappedSlide> {
  val minutes = TimeUnit.MILLISECONDS.toMinutes(report.summary.totalTimeMs)
  val topVisit = report.topVisited.firstOrNull()
  val topTime = report.topByTime.firstOrNull()
  val theme = report.topCategories.firstOrNull()
  val lang = report.languageSplit.entries.joinToString(" · ") {
    "${it.key.uppercase()} ${it.value}"
  }

  return buildList {
    add(WrappedSlide("Your Year in Hymns", report.year.toString(), "Swipe to begin"))
    add(
      WrappedSlide(
        "You opened",
        "${report.summary.totalVisits} hymns",
        "across ${report.summary.activeDays} days",
      ),
    )
    add(WrappedSlide("Time spent reading", "$minutes min", ""))
    if (topVisit != null) {
      add(WrappedSlide("Most visited", topVisit.title, "#${topVisit.number}"))
    }
    if (topTime != null) {
      add(WrappedSlide("You lingered with", topTime.title, "#${topTime.number}"))
    }
    if (theme != null) {
      add(WrappedSlide("Your theme", theme.categoryName, "${theme.visitCount} opens"))
    }
    if (lang.isNotBlank()) {
      add(WrappedSlide("Languages", lang, ""))
    }
    add(
      WrappedSlide(
        "Thank you for reading",
        "See you next year",
        "Share your year below",
      ),
    )
  }
}

private fun buildShareText(report: YearInHymnsReport): String {
  val top = report.topVisited.firstOrNull()
  return buildString {
    appendLine("My Year in Hymns ${report.year}")
    appendLine("${report.summary.totalVisits} opens · ${report.summary.activeDays} days")
    if (top != null) appendLine("Top hymn: #${top.number} ${top.title}")
    append("via Hymn Book")
  }
}
