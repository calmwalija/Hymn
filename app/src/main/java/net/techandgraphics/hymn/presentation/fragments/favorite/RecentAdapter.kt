package net.techandgraphics.hymn.presentation.fragments.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.techandgraphics.hymn.databinding.FragmentFavoriteRecentItemBinding
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.presentation.diffs.DiffUtils

class RecentAdapter(
  private val click: (Lyric) -> Unit
) : ListAdapter<Lyric, RecentAdapter.ViewHolder>(DiffUtils.HYMN_DIFF_UTIL) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(FragmentFavoriteRecentItemBinding.inflate(LayoutInflater.from(parent.context)))
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    getItem(position)?.let { holder.bind(it) }
  }

  inner class ViewHolder(
    private val binding: FragmentFavoriteRecentItemBinding
  ) : RecyclerView.ViewHolder(binding.root) {
    fun bind(lyric: Lyric) = binding.apply {
      this.lyric = lyric
      executePendingBindings()
    }

    init {
      binding.root.setOnClickListener { click.invoke(currentList[absoluteAdapterPosition]) }
    }
  }
}
