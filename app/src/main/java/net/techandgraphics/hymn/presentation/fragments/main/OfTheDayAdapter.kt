package net.techandgraphics.hymn.presentation.fragments.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.techandgraphics.hymn.databinding.FragmentMainHymnOfTheDayBinding
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.presentation.diffs.DiffUtils.HYMN_DIFF_UTIL

class OfTheDayAdapter(
  val event: (Event) -> Unit,
) : ListAdapter<Lyric, OfTheDayAdapter.ViewHolder>(HYMN_DIFF_UTIL) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(
      FragmentMainHymnOfTheDayBinding.inflate(
        LayoutInflater.from(parent.context)
      )
    )
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    getItem(position)?.let { holder.bind(it) }
  }

  inner class ViewHolder(
    private val binding: FragmentMainHymnOfTheDayBinding
  ) : RecyclerView.ViewHolder(binding.root) {
    fun bind(lyric: Lyric) = binding.apply {
      this.lyric = lyric
      executePendingBindings()
    }

    init {
      binding.apply {
        root.setOnClickListener {
          if (absoluteAdapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
          getItem(absoluteAdapterPosition)?.let { event.invoke(Event.Click(it)) }
        }
        favorite.setOnClickListener {
          if (absoluteAdapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
          getItem(absoluteAdapterPosition)?.let { event.invoke(Event.Favorite(it)) }
        }
      }
    }
  }

  sealed class Event {
    class Favorite(val lyric: Lyric) : Event()
    class Click(val lyric: Lyric) : Event()
  }
}
