package com.drevnitskaya.instaclientapp.extensions

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.drevnitskaya.instaclientapp.utils.GlideApp

fun ImageView.loadImage(placeholder: Drawable?, url: String?) {
    GlideApp.with(context).load(url)
        .placeholder(placeholder)
        .error(placeholder)
        .fallback(placeholder)
        .into(this)
}