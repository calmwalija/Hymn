package net.techandgraphics.hymn.presentation.fragments.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.techandgraphics.hymn.databinding.FragmentSearchRandomItemBinding
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.presentation.diffs.DiffUtils

class SearchRandomAdapter(
  private val event: (SearchRandomEvent) -> Unit,
) : ListAdapter<Lyric, SearchRandomAdapter.ViewHolder>(DiffUtils.HYMN_DIFF_UTIL) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(FragmentSearchRandomItemBinding.inflate(LayoutInflater.from(parent.context)))
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    getItem(position)?.let { holder.bind(it, position) }
  }

  inner class ViewHolder(
    private val binding: FragmentSearchRandomItemBinding
  ) : RecyclerView.ViewHolder(binding.root) {
    fun bind(lyric: Lyric, position: Int) = binding.apply {
      this.lyric = lyric
      text.text = String.format("%d. %s", position.plus(1), lyric.title)
      executePendingBindings()
    }

    init {
      binding.root.setOnClickListener { event.invoke(SearchRandomEvent.Click(currentList[absoluteAdapterPosition])) }
    }
  }

  sealed class SearchRandomEvent {
    class Click(val lyric: Lyric) : SearchRandomEvent()
  }
}
