package net.techandgraphics.hymn.presentation.fragments.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.techandgraphics.hymn.databinding.FragmentSearchItemBinding
import net.techandgraphics.hymn.domain.model.Search
import net.techandgraphics.hymn.presentation.diffs.DiffUtils

class SearchTagAdapter(
  private val itemClickListener: (Search) -> Unit,
  private val onDelete: (Search) -> Unit,
  private val onLongTap: (Search) -> Unit,
) : ListAdapter<Search, SearchTagAdapter.ViewHolder>(DiffUtils.SEARCH_DIFF_UTIL) {


  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(FragmentSearchItemBinding.inflate(LayoutInflater.from(parent.context)))
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    getItem(position)?.let { holder.bind(it) }
  }

  inner class ViewHolder(
    private val binding: FragmentSearchItemBinding
  ) : RecyclerView.ViewHolder(binding.root) {
    fun bind(search: Search) = binding.apply {
      this.search = search
      executePendingBindings()
    }

    init {
      binding.root.setOnClickListener { itemClickListener.invoke(currentList[absoluteAdapterPosition]) }
      binding.buttonDelete.setOnClickListener { onDelete.invoke(currentList[absoluteAdapterPosition]) }
      binding.root.setOnLongClickListener {
        onLongTap.invoke(currentList[absoluteAdapterPosition])
        true
      }
    }
  }
}