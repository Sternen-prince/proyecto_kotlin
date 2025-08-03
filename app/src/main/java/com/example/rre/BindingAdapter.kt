package com.example.rre

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, url: String?) {
    Glide.with(view.context)
        .load(url)
        .centerCrop()
        .into(view)

    Log.d("BindingAdapter", "URL cargada: $url")
}
