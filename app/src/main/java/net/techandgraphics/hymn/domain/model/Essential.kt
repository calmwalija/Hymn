package net.techandgraphics.hymn.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Essential(
  val resourceId: Int,
  val groupName: String,
  val content: String,
  val lang: String
) : Parcelable
