package net.techandgraphics.hymn.ui.fragments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.techandgraphics.hymn.databinding.FragmentCategoryItemBinding
import net.techandgraphics.hymn.models.Lyric
import net.techandgraphics.hymn.ui.diffs.DiffUtils
import net.techandgraphics.hymn.ui.fragments.BaseAdapter.*


abstract class BaseAdapter(
    private val itemClickListener: (Any) -> Unit
) : ListAdapter<Lyric, BaseViewHolder>(DiffUtils.HYMN_DIFF_UTIL) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(FragmentCategoryItemBinding.inflate(LayoutInflater.from(parent.context)))
    }


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class BaseViewHolder(
        private val binding: FragmentCategoryItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(lyric: Lyric) = binding.apply {
            this.lyric = lyric
            executePendingBindings()
        }

        init {
            binding.root.setOnClickListener { itemClickListener.invoke(currentList[absoluteAdapterPosition].number) }
        }
    }
}