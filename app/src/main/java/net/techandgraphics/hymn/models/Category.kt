package net.techandgraphics.hymn.models

import androidx.annotation.DrawableRes

data class Category(
    val categoryId: Int,
    @DrawableRes val drawableRes: Int
)