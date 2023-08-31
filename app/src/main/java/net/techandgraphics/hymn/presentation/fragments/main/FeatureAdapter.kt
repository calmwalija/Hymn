package net.techandgraphics.hymn.presentation.fragments.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.techandgraphics.hymn.data.local.entities.Discover
import net.techandgraphics.hymn.databinding.FragmentMainFeaturedHymnBinding
import net.techandgraphics.hymn.presentation.diffs.DiffUtils.DISCOVER_DIFF_UTIL

class FeatureAdapter(
  val event: (Discover) -> Unit,
) : ListAdapter<Discover, FeatureAdapter.ViewHolder>(DISCOVER_DIFF_UTIL) {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return ViewHolder(
      FragmentMainFeaturedHymnBinding.inflate(
        LayoutInflater.from(parent.context), parent, false
      )
    )
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    getItem(position)?.let { holder.bind(it) }
  }

  inner class ViewHolder(
    private val binding: FragmentMainFeaturedHymnBinding
  ) : RecyclerView.ViewHolder(binding.root) {
    fun bind(discover: Discover) = binding.apply {
      this.discover = discover
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
