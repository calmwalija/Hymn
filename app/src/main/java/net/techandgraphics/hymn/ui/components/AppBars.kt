package net.techandgraphics.hymn.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import net.techandgraphics.hymn.R

/**
 * App bars take zero window insets by design: `AppScreen`'s root scaffold
 * already consumes `safeDrawing` for the whole graph. Letting each bar add the
 * status-bar inset again padded every screen twice.
 */
val NoAppBarInsets: WindowInsets = WindowInsets(0, 0, 0, 0)

/**
 * The single small app bar used by every detail screen, so back affordance,
 * title truncation and scroll behaviour are identical everywhere.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HymnTopAppBar(
  title: String,
  modifier: Modifier = Modifier,
  onBack: (() -> Unit)? = null,
  scrollBehavior: TopAppBarScrollBehavior? = null,
  windowInsets: WindowInsets = NoAppBarInsets,
  actions: @Composable RowScope.() -> Unit = {},
) {
  TopAppBar(
    modifier = modifier,
    title = {
      Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
      )
    },
    navigationIcon = { if (onBack != null) BackButton(onBack) },
    actions = actions,
    scrollBehavior = scrollBehavior,
    windowInsets = windowInsets,
    colors = TopAppBarDefaults.topAppBarColors(
      containerColor = MaterialTheme.colorScheme.surface,
      scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
    ),
  )
}

@Composable
fun BackButton(onBack: () -> Unit, modifier: Modifier = Modifier) {
  IconButton(onClick = onBack, modifier = modifier) {
    Icon(
      imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
      contentDescription = stringResource(R.string.action_back),
    )
  }
}
