package net.techandgraphics.hymn.models

import androidx.room.Entity

@Entity(primaryKeys = ["query"])
data class Search(
    val id: Long = System.currentTimeMillis(),
    val query: String,
    val tag: String
)