package net.techandgraphics.hymn.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import net.techandgraphics.hymn.data.local.Translation

@Entity(tableName = "timestamp")
data class TimestampEntity(
  val number: Int,
  val lang: String = Translation.EN.name.lowercase(),
  val timestamp: Long = System.currentTimeMillis(),
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
)
