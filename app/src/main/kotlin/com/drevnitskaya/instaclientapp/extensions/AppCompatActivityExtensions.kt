package com.drevnitskaya.instaclientapp.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.drevnitskaya.instaclientapp.R
import com.google.android.material.snackbar.Snackbar

fun AppCompatActivity.showSnackbar(
    rootView: CoordinatorLayout,
    message: String,
    actionName: String? = null,
    actionListener: (() -> Unit)? = null

) {
    Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)
        .apply {
            if (actionName.isNullOrEmpty().not()) {
                setActionTextColor(
                    ContextCompat.getColor(
                        this@showSnackbar,
                        R.color.shared_action_text
                    )
                )
                setAction(actionName) {
                    actionListener?.invoke()
                }
            }
            show()
        }
}