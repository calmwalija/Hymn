package net.techandgraphics.hymn.ui.fragments.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.techandgraphics.hymn.databinding.FragmentMainRecentItemBinding
import net.techandgraphics.hymn.models.Lyric
import net.techandgraphics.hymn.ui.diffs.DiffUtils.HYMN_DIFF_UTIL

class MainRecentAdapter(
    private val itemClickListener: (Lyric) -> Unit
) :
    ListAdapter<Lyric, MainRecentAdapter.HymnLyricViewHolder>(HYMN_DIFF_UTIL) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HymnLyricViewHolder {
        return HymnLyricViewHolder(
            FragmentMainRecentItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: HymnLyricViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class HymnLyricViewHolder(
        private val binding: FragmentMainRecentItemBinding
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