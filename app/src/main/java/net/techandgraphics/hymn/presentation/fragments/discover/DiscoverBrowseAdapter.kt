package net.techandgraphics.hymn.presentation.fragments.discover

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.techandgraphics.hymn.data.local.entities.Discover
import net.techandgraphics.hymn.databinding.FragmentDiscoverCategoryItemBinding
import net.techandgraphics.hymn.presentation.diffs.DiffUtils
import net.techandgraphics.hymn.presentation.fragments.discover.DiscoverBrowseAdapter.ViewHolder

class DiscoverBrowseAdapter(
  private val itemClickListener: (Discover) -> Unit
) : ListAdapter<Discover, ViewHolder>(DiffUtils.DISCOVER_DIFF_UTIL) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(FragmentDiscoverCategoryItemBinding.inflate(LayoutInflater.from(parent.context)))
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    getItem(position)?.let { holder.bind(it) }
  }

  inner class ViewHolder(
    private val binding: FragmentDiscoverCategoryItemBinding
  ) : RecyclerView.ViewHolder(binding.root) {
    fun bind(discover: Discover) = binding.apply {
      this.discover = discover
      executePendingBindings()
    }

    init {
      binding.root.setOnClickListener { itemClickListener.invoke(currentList[absoluteAdapterPosition]) }
    }
  }
}
