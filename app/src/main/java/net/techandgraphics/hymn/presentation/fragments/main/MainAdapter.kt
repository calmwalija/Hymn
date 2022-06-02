package net.techandgraphics.hymn.presentation.fragments.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import net.techandgraphics.hymn.databinding.FragmentMainItemBinding
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.presentation.diffs.DiffUtils.HYMN_DIFF_UTIL
import net.techandgraphics.hymn.presentation.fragments.main.MainAdapter.ViewHolder

class MainAdapter(
    private val click: (Lyric) -> Unit,
    private val share: (Lyric) -> Unit
) : PagingDataAdapter<Lyric, ViewHolder>(HYMN_DIFF_UTIL) {


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

            binding.root.setOnClickListener {
                getItem(absoluteAdapterPosition)?.let { click.invoke(it) }
            }

            binding.buttonShare.setOnClickListener {
                getItem(absoluteAdapterPosition)?.let { share.invoke(it) }
            }
        }

    }

}