package net.techandgraphics.hymn.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

/**
 * Brand seed. The hymnal's identity colour — a warm liturgical orange.
 * Every role below is a tone off this seed so the palette stays harmonised
 * instead of mixing an orange primary with Material's baseline purple.
 */
val BrandSeed = Color(0xFFD35500)

// Kept for source compatibility with older call sites.
val AccentColor = BrandSeed

private val Primary = Color(0xFF9C4300)
private val OnPrimary = Color(0xFFFFFFFF)
private val PrimaryContainer = Color(0xFFFFDBC9)
private val OnPrimaryContainer = Color(0xFF351000)

private val Secondary = Color(0xFF77574A)
private val OnSecondary = Color(0xFFFFFFFF)
private val SecondaryContainer = Color(0xFFFFDBC9)
private val OnSecondaryContainer = Color(0xFF2C160C)

private val Tertiary = Color(0xFF6B5E2F)
private val OnTertiary = Color(0xFFFFFFFF)
private val TertiaryContainer = Color(0xFFF5E2A7)
private val OnTertiaryContainer = Color(0xFF231B00)

private val ErrorLight = Color(0xFFBA1A1A)
private val OnErrorLight = Color(0xFFFFFFFF)
private val ErrorContainerLight = Color(0xFFFFDAD6)
private val OnErrorContainerLight = Color(0xFF410002)

private val SurfaceLight = Color(0xFFFFF8F6)
private val OnSurfaceLight = Color(0xFF221A16)
private val SurfaceVariantLight = Color(0xFFF5DED4)
private val OnSurfaceVariantLight = Color(0xFF53433D)
private val OutlineLight = Color(0xFF85736C)
private val OutlineVariantLight = Color(0xFFD8C2B9)
private val InverseSurfaceLight = Color(0xFF382E2A)
private val InverseOnSurfaceLight = Color(0xFFFFEDE7)
private val InversePrimaryLight = Color(0xFFFFB68F)

private val PrimaryDark = Color(0xFFFFB68F)
private val OnPrimaryDark = Color(0xFF552100)
private val PrimaryContainerDark = Color(0xFF782F00)
private val OnPrimaryContainerDark = Color(0xFFFFDBC9)

private val SecondaryDark = Color(0xFFE7BFAE)
private val OnSecondaryDark = Color(0xFF442B20)
private val SecondaryContainerDark = Color(0xFF5D4135)
private val OnSecondaryContainerDark = Color(0xFFFFDBC9)

private val TertiaryDark = Color(0xFFD8C68D)
private val OnTertiaryDark = Color(0xFF3A3005)
private val TertiaryContainerDark = Color(0xFF524619)
private val OnTertiaryContainerDark = Color(0xFFF5E2A7)

private val ErrorDark = Color(0xFFFFB4AB)
private val OnErrorDark = Color(0xFF690005)
private val ErrorContainerDark = Color(0xFF93000A)
private val OnErrorContainerDark = Color(0xFFFFDAD6)

private val SurfaceDark = Color(0xFF1A120E)
private val OnSurfaceDark = Color(0xFFF0E0D9)
private val SurfaceVariantDark = Color(0xFF53433D)
private val OnSurfaceVariantDark = Color(0xFFD8C2B9)
private val OutlineDark = Color(0xFFA08D85)
private val OutlineVariantDark = Color(0xFF53433D)
private val InverseSurfaceDark = Color(0xFFF0E0D9)
private val InverseOnSurfaceDark = Color(0xFF382E2A)
private val InversePrimaryDark = Color(0xFF9C4300)

val HymnLightColors = lightColorScheme(
  primary = Primary,
  onPrimary = OnPrimary,
  primaryContainer = PrimaryContainer,
  onPrimaryContainer = OnPrimaryContainer,
  inversePrimary = InversePrimaryLight,
  secondary = Secondary,
  onSecondary = OnSecondary,
  secondaryContainer = SecondaryContainer,
  onSecondaryContainer = OnSecondaryContainer,
  tertiary = Tertiary,
  onTertiary = OnTertiary,
  tertiaryContainer = TertiaryContainer,
  onTertiaryContainer = OnTertiaryContainer,
  error = ErrorLight,
  onError = OnErrorLight,
  errorContainer = ErrorContainerLight,
  onErrorContainer = OnErrorContainerLight,
  background = SurfaceLight,
  onBackground = OnSurfaceLight,
  surface = SurfaceLight,
  onSurface = OnSurfaceLight,
  surfaceVariant = SurfaceVariantLight,
  onSurfaceVariant = OnSurfaceVariantLight,
  outline = OutlineLight,
  outlineVariant = OutlineVariantLight,
  scrim = Color(0xFF000000),
  inverseSurface = InverseSurfaceLight,
  inverseOnSurface = InverseOnSurfaceLight,
  surfaceDim = Color(0xFFE8D7D0),
  surfaceBright = SurfaceLight,
  surfaceContainerLowest = Color(0xFFFFFFFF),
  surfaceContainerLow = Color(0xFFFFF1EC),
  surfaceContainer = Color(0xFFFCEBE4),
  surfaceContainerHigh = Color(0xFFF6E5DF),
  surfaceContainerHighest = Color(0xFFF0E0D9),
)

val HymnDarkColors = darkColorScheme(
  primary = PrimaryDark,
  onPrimary = OnPrimaryDark,
  primaryContainer = PrimaryContainerDark,
  onPrimaryContainer = OnPrimaryContainerDark,
  inversePrimary = InversePrimaryDark,
  secondary = SecondaryDark,
  onSecondary = OnSecondaryDark,
  secondaryContainer = SecondaryContainerDark,
  onSecondaryContainer = OnSecondaryContainerDark,
  tertiary = TertiaryDark,
  onTertiary = OnTertiaryDark,
  tertiaryContainer = TertiaryContainerDark,
  onTertiaryContainer = OnTertiaryContainerDark,
  error = ErrorDark,
  onError = OnErrorDark,
  errorContainer = ErrorContainerDark,
  onErrorContainer = OnErrorContainerDark,
  background = SurfaceDark,
  onBackground = OnSurfaceDark,
  surface = SurfaceDark,
  onSurface = OnSurfaceDark,
  surfaceVariant = SurfaceVariantDark,
  onSurfaceVariant = OnSurfaceVariantDark,
  outline = OutlineDark,
  outlineVariant = OutlineVariantDark,
  scrim = Color(0xFF000000),
  inverseSurface = InverseSurfaceDark,
  inverseOnSurface = InverseOnSurfaceDark,
  surfaceDim = SurfaceDark,
  surfaceBright = Color(0xFF423732),
  surfaceContainerLowest = Color(0xFF140D0A),
  surfaceContainerLow = Color(0xFF221A16),
  surfaceContainer = Color(0xFF271E1A),
  surfaceContainerHigh = Color(0xFF322824),
  surfaceContainerHighest = Color(0xFF3D332E),
)
