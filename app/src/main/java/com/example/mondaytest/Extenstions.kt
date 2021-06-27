package com.example.mondaytest

import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.NonNull
import com.bumptech.glide.Glide

fun ImageView.loadImage(
    @NonNull url: String,
    @DrawableRes error: Int = 0,
    @DrawableRes holder: Int = 0
) {
    Glide
        .with(this.context)
        .load(url)
        .error(error)
        .placeholder(holder)
        .into(this)
}