package com.eliseubatista.pocketdex.utils

import android.widget.ImageView
import androidx.core.net.toUri
import com.bumptech.glide.Glide

//Function to load and image (with the image Url) into a image view
fun loadImageWithGlide(imageUrl: String, imageView: ImageView) {
    val imgUri = imageUrl.toUri().buildUpon().scheme("https").build()

    Glide.with(imageView.context)
        .load(imgUri)
        .into(imageView)
}
