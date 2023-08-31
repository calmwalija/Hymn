package net.techandgraphics.hymn.presentation.fragments.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.techandgraphics.hymn.databinding.FragmentCategoryItemBinding
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.presentation.diffs.DiffUtils

class CategoryAdapter(
  private val event: (CategoryEvent) -> Unit,
) : ListAdapter<Lyric, CategoryAdapter.ViewHolder>(DiffUtils.HYMN_DIFF_UTIL) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(FragmentCategoryItemBinding.inflate(LayoutInflater.from(parent.context)))
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    getItem(position)?.let { holder.bind(it) }
  }

  inner class ViewHolder(
    private val binding: FragmentCategoryItemBinding
  ) : RecyclerView.ViewHolder(binding.root) {
    fun bind(lyric: Lyric) = binding.apply {
      this.lyric = lyric
      executePendingBindings()
    }

    init {
      binding.root.setOnClickListener {
        if (absoluteAdapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
        event.invoke(CategoryEvent.Click(currentList[absoluteAdapterPosition]))
      }
      binding.buttonFav.setOnClickListener {
        if (absoluteAdapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
        event.invoke(CategoryEvent.Favorite(currentList[absoluteAdapterPosition]))
      }
    }
  }

  sealed class CategoryEvent {
    class Click(val lyric: Lyric) : CategoryEvent()
    class Favorite(val lyric: Lyric) : CategoryEvent()
  }
}
