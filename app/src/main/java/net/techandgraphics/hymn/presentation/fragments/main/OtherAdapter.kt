package net.techandgraphics.hymn.presentation.fragments.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.techandgraphics.hymn.databinding.FragmentMainOtherItemBinding
import net.techandgraphics.hymn.domain.model.Other
import net.techandgraphics.hymn.presentation.diffs.DiffUtils

class OtherAdapter(
    val onClick: (Other) -> Unit
) : ListAdapter<Other, OtherAdapter.ViewHolder>(DiffUtils.OTHER_DIFF_UTIL) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(FragmentMainOtherItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ViewHolder(
        private val binding: FragmentMainOtherItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(other: Other) = binding.apply {
            this.other = other
            executePendingBindings()
        }

        init {
            binding.root.setOnClickListener {
                onClick.invoke(currentList[absoluteAdapterPosition])
            }
        }
    }
}