package net.techandgraphics.hymn

import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import net.techandgraphics.hymn.domain.model.Lyric
import kotlin.random.Random

@BindingAdapter("setTextHymnNumber")
fun setTextHymnNumber(textView: AppCompatTextView, lyric: Lyric) {
  "#${lyric.number}".also { textView.text = it }
}

@BindingAdapter("setTextHymnCount")
fun setTextHymnCount(textView: AppCompatTextView, str: String) {
  if (str.contains("-").not())
    textView.text =
      textView.context.resources.getQuantityString(R.plurals.hymn_count, str.toInt(), str.toInt())
  else
    str.take(str.indexOf("-")).toInt().also {
      textView.text = textView.context.resources.getQuantityString(R.plurals.hymn_count, it, it)
    }
}

@BindingAdapter("setTag")
fun setTag(textView: AppCompatTextView, content: String) {
  "#$content".also { textView.text = it }
}

@BindingAdapter("favorite")
fun favorite(view: AppCompatImageView, favorite: Boolean) {
  view.setImageResource(if (favorite) R.drawable.ic_favorite_fill else R.drawable.ic_favorite)
}

@BindingAdapter("hymnNumber")
fun hymnNumber(textView: AppCompatTextView, number: Int) {
  textView.text = textView.context.getString(R.string.hymn_number, number)
}

@BindingAdapter("hymn")
fun hymn(textView: AppCompatTextView, lyric: Lyric) {
  textView.text = lyric.title
}

@BindingAdapter("setHymnOfTheDay")
fun setHymnOfTheDay(textView: AppCompatTextView, lyric: Lyric) {
  "#${lyric.number} - ${lyric.title}".also { textView.text = it }
}

@BindingAdapter(
  value = ["bind:drawableRes", "bind:blurRadius", "bind:blurSampling"],
  requireAll = false
)
fun blurTransformation(view: ImageView, p0: Int, p1: Int, p2: Int) {
  Glide.with(view.context).load(Constant.images[p0].drawableRes)
//    .apply(RequestOptions.bitmapTransform(BlurTransformation(p1, p2)))
    .into(view)
}

@BindingAdapter("setImageGlide")
fun setImageGlide(view: ImageView, p0: Int) {
  Glide.with(view.context).load(Constant.ofTheDay[Random.nextInt(5)]).into(view)
}

@BindingAdapter("backgroundTint")
fun backgroundTint(view: AppCompatTextView, color: Int) {
  view.background.setTint(ContextCompat.getColor(view.context, color))
}

@BindingAdapter("timestamp")
fun timestamp(view: AppCompatTextView, timestamp: Long) {
  view.text = timeAgo(view.context, timestamp)
  view.isInvisible = timestamp == 0L
}

@BindingAdapter("isVisible")
fun isVisible(view: View, flag: Boolean) {
  view.isVisible = flag
}

@BindingAdapter("isInvisible")
fun isInvisible(view: View, flag: Boolean) {
  view.isInvisible = flag.not()
}

@BindingAdapter("favCount")
fun favCount(textView: AppCompatTextView, str: String) {
  str.substring(str.indexOf("-").plus(1)).toInt().also {
    textView.isVisible = it != 0
    textView.text = String.format("%d", it)
  }
}
