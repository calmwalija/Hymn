package net.techandgraphics.hymn.presentation.diffs

import androidx.recyclerview.widget.DiffUtil
import net.techandgraphics.hymn.data.local.entities.Discover
import net.techandgraphics.hymn.domain.model.Essential
import net.techandgraphics.hymn.domain.model.Lyric
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

  val ESSENTIAL_DIFF_UTIL = object : DiffUtil.ItemCallback<Essential>() {
    override fun areContentsTheSame(
      oldItem: Essential,
      newItem: Essential
    ) = oldItem.resourceId == newItem.resourceId

    override fun areItemsTheSame(
      oldItem: Essential,
      newItem: Essential
    ) = oldItem == newItem
  }

  val DISCOVER_DIFF_UTIL = object : DiffUtil.ItemCallback<Discover>() {
    override fun areContentsTheSame(
      oldItem: Discover,
      newItem: Discover
    ) = oldItem.lyric.lyricId == newItem.lyric.lyricId

    override fun areItemsTheSame(
      oldItem: Discover,
      newItem: Discover
    ) = oldItem == newItem
  }
}
