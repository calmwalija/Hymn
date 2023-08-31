package net.techandgraphics.hymn.presentation.fragments.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.techandgraphics.hymn.databinding.FragmentMainSuggestItemBinding
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.presentation.diffs.DiffUtils.HYMN_DIFF_UTIL

class FtsDialogAdapter(
  private val event: (Lyric) -> Unit,
) : ListAdapter<Lyric, FtsDialogAdapter.ViewHolder>(HYMN_DIFF_UTIL) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(
      FragmentMainSuggestItemBinding.inflate(LayoutInflater.from(parent.context))
    )
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    getItem(position)?.let { holder.bind(it) }
  }

  inner class ViewHolder(
    private val binding: FragmentMainSuggestItemBinding
  ) : RecyclerView.ViewHolder(binding.root) {
    fun bind(lyric: Lyric) = binding.apply {
      this.lyric = lyric
      executePendingBindings()
    }

    init {
      binding.root.setOnClickListener {
        if (absoluteAdapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
        getItem(absoluteAdapterPosition)?.let { event.invoke(it) }
      }
    }
  }
}
