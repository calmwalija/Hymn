package net.techandgraphics.hymn.ui.screen.about

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri.parse
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import net.techandgraphics.hymn.R
import net.techandgraphics.hymn.getAppVersion
import net.techandgraphics.hymn.ui.components.HymnTopAppBar
import net.techandgraphics.hymn.ui.components.NavigationRow
import net.techandgraphics.hymn.ui.components.NoAppBarInsets
import net.techandgraphics.hymn.ui.theme.Sizes
import net.techandgraphics.hymn.ui.theme.Space

/**
 * App identity, credits and version. The credit and support copy previously
 * only existed as strings with no screen rendering them.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
  hymnCount: Int,
  onBack: () -> Unit,
) {
  val context = LocalContext.current
  val version = getAppVersion(context)?.name ?: "—"
  val playStoreUrl = "https://play.google.com/store/apps/details?id=net.techandgraphics.hymn"
  val whatsAppUrl = "https://api.whatsapp.com/send?phone=+265993563408"

  Scaffold(
    contentWindowInsets = NoAppBarInsets,
    topBar = {
      HymnTopAppBar(
        title = stringResource(R.string.about_title),
        onBack = onBack,
      )
    },
  ) { padding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding)
        .verticalScroll(rememberScrollState()),
    ) {
      Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = Space.xl),
      ) {
        Surface(
          shape = MaterialTheme.shapes.extraLarge,
          color = MaterialTheme.colorScheme.primaryContainer,
          modifier = Modifier.size(88.dp),
        ) {
          Image(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier
              .fillMaxSize()
              .clip(MaterialTheme.shapes.extraLarge),
          )
        }
        Spacer(Modifier.height(Space.md))
        Text(
          text = stringResource(R.string.app_name),
          style = MaterialTheme.typography.headlineSmall,
        )
        Text(
          text = stringResource(R.string.about_tagline),
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          textAlign = TextAlign.Center,
        )
        if (hymnCount > 0) {
          Spacer(Modifier.height(Space.xxs))
          Text(
            text = stringResource(R.string.about_hymn_count, hymnCount),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
          )
        }
      }

      HorizontalDivider()

      Text(
        text = stringResource(R.string.about_credits).uppercase(),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = Space.md, top = Space.md, bottom = Space.xxs),
      )
      Text(
        text = stringResource(R.string.about),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(horizontal = Space.md, vertical = Space.xs),
      )

      Spacer(Modifier.height(Space.sm))
      HorizontalDivider()
      Spacer(Modifier.height(Space.xs))

      NavigationRow(
        title = stringResource(R.string.settings_rate),
        subtitle = stringResource(R.string.rate),
        leading = {
          Icon(
            painter = painterResource(R.drawable.ic_rate),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(Sizes.icon),
          )
        },
        onClick = { context.startActivity(Intent(ACTION_VIEW).setData(parse(playStoreUrl))) },
      )
      NavigationRow(
        title = stringResource(R.string.settings_feedback),
        subtitle = stringResource(R.string.feedback),
        leading = {
          Icon(
            painter = painterResource(R.drawable.ic_whatsapp),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(Sizes.icon),
          )
        },
        onClick = { context.startActivity(Intent(ACTION_VIEW).setData(parse(whatsAppUrl))) },
      )

      Text(
        text = stringResource(R.string.about_version, version),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = Space.lg),
      )
    }
  }
}
