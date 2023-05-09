package net.techandgraphics.hymn.data.local.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Other")
data class EssentialEntity(
  @PrimaryKey(autoGenerate = false)
  val resourceId: Int,
  val groupName: String,
  val content: String,
  val lang: String = "en",
) : Parcelable
