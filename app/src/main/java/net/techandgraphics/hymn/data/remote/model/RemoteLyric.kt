package net.techandgraphics.hymn.data.remote.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RemoteLyric(
  @SerializedName("category_id")
  val categoryId: Int,
  @SerializedName("category_name")
  val categoryName: String,
  @SerializedName("chorus")
  val chorus: Int,
  @SerializedName("content")
  val content: String,
  @SerializedName("lang")
  val lang: String,
  @SerializedName("lyric_id")
  val lyricId: Int,
  @SerializedName("number")
  val number: Int
) : Parcelable
