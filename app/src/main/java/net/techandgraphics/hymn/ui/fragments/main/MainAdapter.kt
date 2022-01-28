package net.techandgraphics.hymn.ui.fragments.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.techandgraphics.hymn.databinding.FragmentMainItemBinding
import net.techandgraphics.hymn.models.Lyric
import net.techandgraphics.hymn.ui.diffs.DiffUtils.HYMN_DIFF_UTIL
import net.techandgraphics.hymn.ui.fragments.main.MainAdapter.HymnLyricViewHolder

class MainAdapter(
    private val itemClickListener: (Lyric) -> Unit,
    private val share: (Lyric) -> Unit
) : ListAdapter<Lyric, HymnLyricViewHolder>(HYMN_DIFF_UTIL) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HymnLyricViewHolder {
        return HymnLyricViewHolder(
            FragmentMainItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: HymnLyricViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class HymnLyricViewHolder(
        private val binding: FragmentMainItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lyric: Lyric) = binding.apply {
            this.lyric = lyric
            executePendingBindings()
        }


        init {
            binding.root.setOnClickListener { itemClickListener.invoke(currentList[absoluteAdapterPosition]) }
            binding.buttonShare.setOnClickListener { share.invoke(currentList[absoluteAdapterPosition]) }
        }
    }

}