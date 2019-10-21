
package com.jmm.nasaastronomicallook.pictureoftheday.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.api.load

@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
  if (!imageUrl.isNullOrEmpty()) {
      view.load(imageUrl) {
           placeholder(android.R.drawable.screen_background_dark)
           error(android.R.drawable.screen_background_light)
      }
  }
}