package net.techandgraphics.hymn.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Other(
    @PrimaryKey(autoGenerate = false)
    val resourceId: Int,
    val groupName: String,
    val content: String,
) : Parcelable
