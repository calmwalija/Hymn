package net.techandgraphics.hymn.ui.screen.wrapped

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import net.techandgraphics.hymn.domain.model.YearInHymnsReport
import java.util.concurrent.TimeUnit

@Composable
fun YearInHymnsScreen(
  state: WrappedUiState,
  onClose: () -> Unit,
) {
  val report = state.report
  if (state.loading) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
      Text("Gathering your year…")
    }
    return
  }
  if (report == null || report.summary.totalVisits == 0L) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(24.dp),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      Text("Not enough reading yet for this year.", textAlign = TextAlign.Center)
      TextButton(onClick = onClose) { Text("Close") }
    }
    return
  }

  val slides = buildSlides(report)
  val pagerState = rememberPagerState { slides.size }
  val context = LocalContext.current

  Column(modifier = Modifier.fillMaxSize()) {
    TextButton(onClick = onClose, modifier = Modifier.padding(8.dp)) { Text("Close") }
    HorizontalPager(
      state = pagerState,
      modifier = Modifier.weight(1f),
    ) { page ->
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(MaterialTheme.colorScheme.surface)
          .padding(24.dp),
        contentAlignment = Alignment.Center,
      ) {
        Text(
          text = slides[page],
          style = MaterialTheme.typography.headlineSmall,
          textAlign = TextAlign.Center,
          fontWeight = FontWeight.Medium,
        )
      }
    }
    Button(
      onClick = {
        val shareText = buildShareText(report)
        context.startActivity(
          Intent.createChooser(
            Intent(Intent.ACTION_SEND).apply {
              type = "text/plain"
              putExtra(Intent.EXTRA_TEXT, shareText)
            },
            "Share your Year in Hymns",
          )
        )
      },
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
    ) {
      Text("Share")
    }
  }
}

private fun buildSlides(report: YearInHymnsReport): List<String> {
  val minutes = TimeUnit.MILLISECONDS.toMinutes(report.summary.totalTimeMs)
  val topVisit = report.topVisited.firstOrNull()
  val topTime = report.topByTime.firstOrNull()
  val theme = report.topCategories.firstOrNull()
  val lang = report.languageSplit.entries.joinToString(" · ") { "${it.key.uppercase()}: ${it.value}" }
  return listOf(
    "Your Year in Hymns\n${report.year}",
    "You opened ${report.summary.totalVisits} hymns\nacross ${report.summary.activeDays} days",
    "You spent about $minutes minutes reading",
    topVisit?.let { "Most visited\n#${it.number} ${it.title}" } ?: "Keep exploring hymns",
    topTime?.let { "You lingered with\n#${it.number} ${it.title}" } ?: "More time with hymns awaits",
    theme?.let { "Your theme\n${it.categoryName}" } ?: "Your themes will appear here",
    if (lang.isNotBlank()) "Languages\n$lang" else "Sing in every tongue you love",
    "Thank you for reading with Hymn Book\nSwipe back or share your year",
  )
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
