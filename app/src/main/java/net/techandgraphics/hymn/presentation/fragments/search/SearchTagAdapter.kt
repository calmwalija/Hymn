package net.techandgraphics.hymn.presentation.fragments.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.techandgraphics.hymn.data.local.entities.Search
import net.techandgraphics.hymn.databinding.FragmentSearchTagItemBinding
import net.techandgraphics.hymn.presentation.diffs.DiffUtils

class SearchTagAdapter(
  private val click: (Search) -> Unit,
) : ListAdapter<Search, SearchTagAdapter.ViewHolder>(DiffUtils.SEARCH_DIFF_UTIL) {


  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(FragmentSearchTagItemBinding.inflate(LayoutInflater.from(parent.context)))
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    getItem(position)?.let { holder.bind(it) }
  }

  inner class ViewHolder(
    private val binding: FragmentSearchTagItemBinding
  ) : RecyclerView.ViewHolder(binding.root) {
    fun bind(search: Search) = binding.apply {
      this.search = search
      executePendingBindings()
    }

    init {
      binding.root.setOnClickListener { click.invoke(currentList[absoluteAdapterPosition]) }
    }
  }
}