package net.techandgraphics.hymn.presentation.fragments.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import net.techandgraphics.hymn.databinding.LyricItemBinding
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.presentation.diffs.DiffUtils.HYMN_DIFF_UTIL
import net.techandgraphics.hymn.presentation.fragments.main.MainAdapter.ViewHolder

class MainAdapter(
  val event: (Lyric) -> Unit,
) : PagingDataAdapter<Lyric, ViewHolder>(HYMN_DIFF_UTIL) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(
      LyricItemBinding.inflate(
        LayoutInflater.from(parent.context)
      )
    )
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    getItem(position)?.let { holder.bind(it) }
  }

  inner class ViewHolder(
    private val binding: LyricItemBinding
  ) : RecyclerView.ViewHolder(binding.root) {
    fun bind(lyric: Lyric) = binding.apply {
      this.lyric = lyric
      executePendingBindings()
    }

    init {

      binding.root.setOnClickListener {
        getItem(absoluteAdapterPosition)?.let { event.invoke(it) }
      }
    }
  }
}
