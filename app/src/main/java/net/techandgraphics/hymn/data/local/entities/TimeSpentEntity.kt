package net.techandgraphics.hymn.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import net.techandgraphics.hymn.data.local.Lang

@Entity(tableName = "time_spent")
data class TimeSpentEntity(
  val number: Int,
  val lang: String = Lang.EN.lowercase(),
  val timeSpent: Long = System.currentTimeMillis(),
  @PrimaryKey(autoGenerate = true) val id: Int = 0,
)
