package net.techandgraphics.hymn.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.techandgraphics.hymn.databinding.FragmentMainItemBinding
import net.techandgraphics.hymn.models.Lyric
import net.techandgraphics.hymn.ui.diffs.DiffUtils.HYMN_DIFF_UTIL

class LyricAdapter(
    private val click: (Lyric) -> Unit,
    private val share: (Lyric) -> Unit
) : ListAdapter<Lyric, LyricAdapter.ViewHolder>(HYMN_DIFF_UTIL) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            FragmentMainItemBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ViewHolder(
        private val binding: FragmentMainItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lyric: Lyric) = binding.apply {
            this.lyric = lyric
            executePendingBindings()
        }


        init {
            binding.root.setOnClickListener { click.invoke(currentList[absoluteAdapterPosition]) }
            binding.buttonShare.setOnClickListener { share.invoke(currentList[absoluteAdapterPosition]) }
        }
    }

}