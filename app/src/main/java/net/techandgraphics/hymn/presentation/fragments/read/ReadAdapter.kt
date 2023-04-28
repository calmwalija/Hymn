package net.techandgraphics.hymn.presentation.fragments.read

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import net.techandgraphics.hymn.data.local.entities.Lyric
import net.techandgraphics.hymn.databinding.FragmentReadItemBinding
import net.techandgraphics.hymn.presentation.diffs.DiffUtils
import net.techandgraphics.hymn.presentation.fragments.read.ReadAdapter.PreviewFragmentViewHolder

class ReadAdapter(
  var fontSize: Int
) : ListAdapter<Lyric, PreviewFragmentViewHolder>(DiffUtils.HYMN_DIFF_UTIL) {

  private var hasChorus = false

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviewFragmentViewHolder {
    return PreviewFragmentViewHolder(
      FragmentReadItemBinding.inflate(
        LayoutInflater.from(parent.context)
      )
    )
  }

  override fun onBindViewHolder(holder: PreviewFragmentViewHolder, position: Int) {
    getItem(position)?.let { holder.bind(it, position) }
  }

  inner class PreviewFragmentViewHolder(
    private val binding: FragmentReadItemBinding
  ) : RecyclerView.ViewHolder(binding.root) {
    fun bind(lyric: Lyric, position: Int) = binding.apply {

      this.lyric = lyric.copy(
        categoryName = if (lyric.chorus == 0) {
          (if (position == 0) 1 else (position + if (hasChorus) 0 else 1)).toString()
        } else {
          hasChorus = true
          "Chorus"
        }
      )

      executePendingBindings()

      stanza.textSize = fontSize.toFloat()
      stanzaIndexTextView.isVisible = lyric.chorus != 2
      stanza.setPadding(16, if (lyric.chorus == 2) 0 else 16, 16, 16)
      stanza.setTypeface(
        null,
        if (lyric.chorus > 0) Typeface.ITALIC else Typeface.NORMAL
      )
    }
  }
}
