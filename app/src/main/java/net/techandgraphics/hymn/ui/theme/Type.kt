package net.techandgraphics.hymn.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

fun setTypography(fontFamily: FontFamily = FontFamily.Default) =
// Set of Material typography styles to start with
  Typography(
    bodySmall = TextStyle(
      fontFamily = fontFamily,
      fontWeight = FontWeight.Light,
      fontSize = 12.sp,
    ),
    bodyMedium = TextStyle(
      fontFamily = fontFamily,
      fontWeight = FontWeight.Normal,
      fontSize = 14.sp
    ),
    bodyLarge = TextStyle(
      fontFamily = fontFamily,
      fontWeight = FontWeight.Normal,
      fontSize = 16.sp,
      lineHeight = 24.sp,
      letterSpacing = 0.5.sp
    ),
    labelMedium = TextStyle(
      fontFamily = fontFamily,
      fontWeight = FontWeight.Normal,
    ),
    titleLarge = TextStyle(
      fontFamily = fontFamily,
      fontWeight = FontWeight.Normal,
      fontSize = 22.sp,
      lineHeight = 28.sp,
      letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
      fontFamily = fontFamily,
      fontWeight = FontWeight.Medium,
      fontSize = 11.sp,
      lineHeight = 16.sp,
      letterSpacing = 0.5.sp
    ),
    titleMedium = TextStyle(
      fontFamily = fontFamily,
      fontWeight = FontWeight.Bold,
      fontSize = 18.sp,
    ),
    displaySmall = TextStyle(
      fontFamily = fontFamily,
    ),
    titleSmall = TextStyle(
      fontFamily = fontFamily
    )
  )


