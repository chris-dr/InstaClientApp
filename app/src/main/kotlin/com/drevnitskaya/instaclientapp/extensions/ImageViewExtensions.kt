package com.drevnitskaya.instaclientapp.extensions

import android.widget.ImageView
import com.drevnitskaya.instaclientapp.utils.GlideApp

fun ImageView.loadImage(url: String?) {
    GlideApp.with(context).load(url)
        .into(this)
}