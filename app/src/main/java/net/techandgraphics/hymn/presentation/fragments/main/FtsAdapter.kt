package net.techandgraphics.hymn.presentation.fragments.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import net.techandgraphics.hymn.domain.model.Lyric
import net.techandgraphics.hymn.presentation.diffs.DiffUtils
import net.techandgraphics.hymn.databinding.FragmentMainSuggestDescItemBinding as SuggestBind
import net.techandgraphics.hymn.databinding.FragmentMainTheHymnBookItemBinding as HymnBind

sealed class OnEvent {
  class Hymn(val lyric: Lyric) : OnEvent()
  class Delete(val lyric: Lyric) : OnEvent()
  object Suggest : OnEvent()
}

class FtsAdapter(
  val onEvent: (OnEvent) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  val asyncListDiffer = AsyncListDiffer(this, DiffUtils.HYMN_DIFF_UTIL)

  private enum class Type(val value: Int) { SUGGEST(0), HYMN(1) }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return when (viewType) {
      Type.HYMN.value -> HymnViewHolder(HymnBind.inflate(LayoutInflater.from(parent.context)))
      else -> SuggestViewHolder(SuggestBind.inflate(LayoutInflater.from(parent.context)))
    }
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    when (position) {
      Type.SUGGEST.value -> (holder as SuggestViewHolder)
      else -> (holder as HymnViewHolder).bind(asyncListDiffer.currentList[position])
    }
  }

  override fun getItemViewType(position: Int): Int =
    if (position == 0) Type.SUGGEST.value else Type.HYMN.value

  override fun getItemCount(): Int {
    return asyncListDiffer.currentList.size
  }

  inner class HymnViewHolder(private val bind: HymnBind) : RecyclerView.ViewHolder(bind.root) {
    fun bind(lyric: Lyric) = bind.apply {
      this.lyric = lyric
      suggest.isInvisible = lyric.ftsSuggestion.not()
      executePendingBindings()
    }

    init {
      bind.root.setOnClickListener {
        onEvent.invoke(OnEvent.Hymn(asyncListDiffer.currentList[absoluteAdapterPosition]))
      }

      bind.root.setOnLongClickListener {
        onEvent.invoke(OnEvent.Delete(asyncListDiffer.currentList[absoluteAdapterPosition]))
        true
      }
    }
  }

  inner class SuggestViewHolder(bind: SuggestBind) : RecyclerView.ViewHolder(bind.root) {
    init {
      bind.root.setOnClickListener {
        onEvent.invoke(OnEvent.Suggest)
      }
    }
  }
}
