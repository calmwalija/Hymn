package net.techandgraphics.hymn.data.remote.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LyricDto(
  @SerializedName("category_id")
  val categoryId: Int,
  @SerializedName("category_name")
  val categoryName: String,
  @SerializedName("lyric_id")
  val lyricId: Int,
  val chorus: Int,
  val content: String,
  val lang: String,
  val number: Int
) : Parcelable
