package net.techandgraphics.hymn.data.remote.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RemoteResource(
  @SerializedName("lyric_id")
  val lyricId: Int,
  @SerializedName("category_id")
  val categoryId: Int,
  @SerializedName("category_name")
  val categoryName: String,
  val number: Int,
  val chorus: Int,
  val content: String,
  val timestamp: Long = System.currentTimeMillis(),
  val title: String,
  val lang: String
) : Parcelable
