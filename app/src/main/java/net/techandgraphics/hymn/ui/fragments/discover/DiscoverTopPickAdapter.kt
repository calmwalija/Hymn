package net.techandgraphics.hymn.ui.fragments.discover

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
 import net.techandgraphics.hymn.databinding.FragmentDiscoverTopPickItemBinding
import net.techandgraphics.hymn.models.Lyric
import net.techandgraphics.hymn.ui.diffs.DiffUtils

class DiscoverTopPickAdapter(
    private val itemClickListener: (Lyric) -> Unit
) : ListAdapter<Lyric, DiscoverTopPickAdapter.ViewHolder>(DiffUtils.HYMN_DIFF_UTIL) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FragmentDiscoverTopPickItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ViewHolder(
        private val binding: FragmentDiscoverTopPickItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lyric: Lyric) = binding.apply {
            this.lyric = lyric
             executePendingBindings()
        }
        init {
            binding.root.setOnClickListener { itemClickListener.invoke(currentList[absoluteAdapterPosition]) }
        }
    }
}