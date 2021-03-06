package net.techandgraphics.hymn.presentation.diffs

import androidx.recyclerview.widget.DiffUtil
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.domain.model.Other
import net.techandgraphics.hymn.domain.model.Search

object DiffUtils {


    val HYMN_DIFF_UTIL = object : DiffUtil.ItemCallback<Lyric>() {
        override fun areContentsTheSame(
            oldItem: Lyric,
            newItem: Lyric
        ) = oldItem.lyricId == newItem.lyricId

        override fun areItemsTheSame(
            oldItem: Lyric,
            newItem: Lyric
        ) = oldItem == newItem
    }

    val SEARCH_DIFF_UTIL = object : DiffUtil.ItemCallback<Search>() {
        override fun areContentsTheSame(
            oldItem: Search,
            newItem: Search
        ) = oldItem.id == newItem.id

        override fun areItemsTheSame(
            oldItem: Search,
            newItem: Search
        ) = oldItem == newItem
    }


    val OTHER_DIFF_UTIL = object : DiffUtil.ItemCallback<Other>() {
        override fun areContentsTheSame(
            oldItem: Other,
            newItem: Other
        ) = oldItem.resourceId == newItem.resourceId

        override fun areItemsTheSame(
            oldItem: Other,
            newItem: Other
        ) = oldItem == newItem
    }

}