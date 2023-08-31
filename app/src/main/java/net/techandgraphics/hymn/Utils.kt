package net.techandgraphics.hymn

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.navigation.NavController
import androidx.palette.graphics.Palette
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.elconfidencial.bubbleshowcase.BubbleShowCase
import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder
import com.google.firebase.analytics.FirebaseAnalytics
import net.techandgraphics.hymn.data.local.entities.LyricEntity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Objects
import java.util.concurrent.TimeUnit

object Utils {

  const val FEATURE_LIMIT = 50

  fun readJsonFromAssetToString(context: Context, file: String): String? {
    return try {
      context.assets.open(file).bufferedReader().use { it.readText() }
    } catch (e: Exception) {
      null
    }
  }

  fun (RecyclerView.Adapter<*>).stateRestorationPolicy() {
    stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
  }

  infix fun Context.share(lyricList: List<LyricEntity>) = with(Intent(Intent.ACTION_SEND)) {
    var index = 0
    buildString {
      append(lyricList[0].title.uppercase())
      append("\n\n")
      lyricList.forEach { lyric ->
        if (lyric.chorus == 0) {
          index += 1
          append(index)
        } else append("Chorus")
        append("\n")
        append(lyric.content)
        append("\n\n")
      }
    }.also { putExtra(Intent.EXTRA_TEXT, it) }
    type = "text/plain"
    startActivity(Intent.createChooser(this, "Share"))
  }

  fun decodeResource(
    context: Context,
    @DrawableRes
    drawableRes: Int
  ): Bitmap =
    BitmapFactory.decodeResource(context.resources, drawableRes)

  fun createPaletteSync(bitmap: Bitmap): Palette = Palette.from(bitmap).generate()

  fun String.regexLowerCase() = replace(Regex("[_',.;!-\"?]"), "").lowercase()
  fun String.capitaliseWord() = split(" ").joinToString(" ") {
    it.replaceFirstChar {
      if (it.isLowerCase()) it.titlecase(
        Locale.getDefault()
      ) else it.toString()
    }
  }

  fun Dialog.dialog(): Dialog {
    window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)).also { return this }
  }

  fun Dialog.dialogShow() {
    Objects.requireNonNull(window!!).setLayout(
      WindowManager.LayoutParams.MATCH_PARENT,
      WindowManager.LayoutParams.WRAP_CONTENT
    )
    show()
  }

  fun EditText.onAddTextChangedListener(onTextChangedCallback: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
      override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

      override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        onTextChangedCallback.invoke(p0.toString())
      }

      override fun afterTextChanged(p0: Editable?) = Unit
    })
  }

  fun toast(context: Context, message: String) =
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

  fun openWebsite(activity: Activity, url: String) = activity.startActivity(
    Intent(Intent.ACTION_VIEW)
      .setData(Uri.parse(url))
  )

  fun changeFontSize(dialog: Dialog, fontSize: Int, onTextChanged: (Int) -> Unit) =
    dialog.dialog().apply {
      setContentView(R.layout.dialog_change_font)
      val size = findViewById<TextView>(R.id.size)
      size.text = fontSize.toString()
      window?.setGravity(Gravity.BOTTOM)
      window?.attributes?.let {
        it.y = 100
      }
      findViewById<SeekBar>(R.id.seekbar).apply {
        progress = fontSize
        seekBarChangeListener {
          onTextChanged.invoke(it)
          size.text = it.toString()
          PreferenceManager
            .getDefaultSharedPreferences(context)
            .edit()
            .putInt(context.getString(R.string.font_key), it)
            .apply()
        }
      }
      dialogShow()
    }

  private fun SeekBar.seekBarChangeListener(progressChanged: (Int) -> Unit) {
    setOnSeekBarChangeListener(object :
        SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
          progressChanged.invoke(p1)
        }

        override fun onStartTrackingTouch(p0: SeekBar?) = Unit
        override fun onStopTrackingTouch(p0: SeekBar?) = Unit
      })
  }

  fun currentMillsDiff(timeInMills: Long): Boolean {
    val today = TimeUnit.MILLISECONDS.toDays(System.currentTimeMillis())
    val diff = TimeUnit.MILLISECONDS.toDays(timeInMills)
    return today != diff
  }

  fun getThreshold(maxValue: Int, size: Int, random: Int): Int {
    val percentage = random.toFloat().div(maxValue).times(100).toInt()
    val range = size.toFloat().div(100).times(percentage).toInt()
    return if (range.plus(FEATURE_LIMIT) > size) range.minus(FEATURE_LIMIT) else range
  }
}

infix fun NavController.onChangeBook(str: String) {
  val versionEntity = context.resources.getStringArray(R.array.version_entries)
  val versionValue = context.resources.getStringArray(R.array.version_values)
  val fragmentId = currentDestination?.id
  popBackStack(fragmentId!!, true)
  navigate(fragmentId)
  val versionName = if (str == versionValue[0]) versionEntity.first() else versionEntity.last()
  val msg = "You are now reading $versionName version."
  Utils.toast(context, msg)
}

const val SECOND_MILLIS = 1000
const val MINUTE_MILLIS = 60 * SECOND_MILLIS
const val HOUR_MILLIS = 60 * MINUTE_MILLIS

fun timeAgo(context: Context, timestamp: Long): String {
  val diff = System.currentTimeMillis() - timestamp
  return when {
    diff < MINUTE_MILLIS -> "just now"
    diff < 60 * MINUTE_MILLIS -> {
      val minutes = diff.div(MINUTE_MILLIS).toInt()
      "${context.resources.getQuantityString(R.plurals.minutes, minutes, minutes)} ago"
    }

    diff < 24 * HOUR_MILLIS -> {
      val hours = diff.div(HOUR_MILLIS).toInt()
      "${context.resources.getQuantityString(R.plurals.hours, hours, hours)} ago"
    }

    diff < 48 * HOUR_MILLIS -> "yesterday"
    else -> longDateFormat(timestamp)
  }
}

fun longDateFormat(timestamp: Long): String {
  val simpleDateFormat = SimpleDateFormat("dd MMM, yyyy ", Locale.getDefault())
  val currentTimeMillis = Date(timestamp)
  return simpleDateFormat.format(currentTimeMillis)
}

fun Activity.onBubbleShowCaseBuilder(view: View, title: String, description: String) =
  BubbleShowCaseBuilder(this)
    .description(description)
    .title(title)
    .arrowPosition(BubbleShowCase.ArrowPosition.TOP)
    .backgroundColor(Color.WHITE)
    .textColor(Color.BLACK)
    .targetView(view)
    .closeActionImageResourceId(R.drawable.ic_close_44)
    .highlightMode(BubbleShowCase.HighlightMode.VIEW_SURFACE)

fun FirebaseAnalytics.tagEvent(name: String, bundle: Bundle) {
  logEvent(name, bundle)
}

fun timeInMillisMonth(month: Int = 1) =
  Calendar.getInstance().apply { add(Calendar.MONTH, month) }.timeInMillis
