package net.techandgraphics.hymn

import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso
import net.techandgraphics.hymn.data.local.entities.Lyric

@BindingAdapter("setImageView")
fun setImageView(imageView: ImageView, drawableRes: Int) {
  Picasso.get().load(Constant.images[drawableRes].drawableRes).into(imageView)
}

@BindingAdapter("setTextHymnNumber")
fun setTextHymnNumber(textView: AppCompatTextView, lyric: Lyric) {
  "#${lyric.number}".also { textView.text = it }
}

@BindingAdapter("setTextHymnCount")
fun setTextHymnCount(textView: AppCompatTextView, count: Int) {
  textView.text = textView.context.resources.getQuantityString(R.plurals.hymn_count, count, count)
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
