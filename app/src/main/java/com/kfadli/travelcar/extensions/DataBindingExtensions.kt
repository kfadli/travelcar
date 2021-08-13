package com.kfadli.travelcar.extensions

import android.graphics.Bitmap
import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.kfadli.travelcar.R
import java.text.SimpleDateFormat
import java.util.*

object DataBindingExtensions {

    private val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)

    @BindingAdapter("dateFormatted")
    @JvmStatic
    fun dateFormatted(view: EditText, date: Date?) {
        if (date != null) {
            view.setText(sdf.format(date))
        } else {
            view.setText("")
        }
    }

    @BindingAdapter("userThumbnail")
    @JvmStatic
    fun userThumbnail(view: ImageView, thumbnail: Bitmap?) {

        if (thumbnail != null) {
            view.setImageBitmap(thumbnail)
        } else {
            view.setImageResource(R.drawable.ic_baseline_no_photography_24)
        }

    }
}