package net.techandgraphics.hymn.ui.fragments.discover

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.techandgraphics.hymn.databinding.FragmentDiscoverCategoryItemBinding
import net.techandgraphics.hymn.models.Lyric
import net.techandgraphics.hymn.ui.diffs.DiffUtils
import net.techandgraphics.hymn.ui.fragments.discover.DiscoverBrowseAdapter.ViewHolder

class DiscoverBrowseAdapter(
    private val itemClickListener: (Lyric) -> Unit
) : ListAdapter<Lyric, ViewHolder>(DiffUtils.HYMN_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FragmentDiscoverCategoryItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ViewHolder(
        private val binding: FragmentDiscoverCategoryItemBinding
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