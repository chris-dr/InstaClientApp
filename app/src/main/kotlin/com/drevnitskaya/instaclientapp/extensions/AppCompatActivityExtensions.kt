package com.drevnitskaya.instaclientapp.extensions

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewTreeObserver
import android.view.animation.AccelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityOptionsCompat
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

const val EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X"
const val EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y"
const val EXTRA_CIRCULAR_REVEAL_START_RADIUS = "EXTRA_CIRCULAR_REVEAL_START_RADIUS"

const val DEFAULT_ENTER_REVEAL_ML = 400L
const val DEFAULT_EXIT_REVEAL_ML = 300L

fun AppCompatActivity.revealActivity(savedInstanceState: Bundle?, rootView: View) {
    if (savedInstanceState == null && intent.hasExtra(EXTRA_CIRCULAR_REVEAL_X)
        && intent.hasExtra(EXTRA_CIRCULAR_REVEAL_Y)
    ) {

        val revealX = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0)
        val revealY = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0)
        val startRadius = intent.getFloatExtra(EXTRA_CIRCULAR_REVEAL_START_RADIUS, 0f)

        val viewTreeObserver = rootView.viewTreeObserver
        if (viewTreeObserver.isAlive) {
            viewTreeObserver.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    startRevealActivityAnimation(rootView, revealX, revealY, startRadius)
                    rootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
        }
    } else {
        rootView.visibility = View.VISIBLE
    }
}

fun AppCompatActivity.startUnRevealActivity(rootView: View) {
    if (intent.hasExtra(EXTRA_CIRCULAR_REVEAL_X) && intent.hasExtra(EXTRA_CIRCULAR_REVEAL_Y)) {
        val revealX = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0)
        val revealY = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0)
        val startRadius = intent.getFloatExtra(EXTRA_CIRCULAR_REVEAL_START_RADIUS, 0f)
        val finalRadius = rootView.width.coerceAtLeast(rootView.height) * 1.1f
        val circularReveal = ViewAnimationUtils.createCircularReveal(
            rootView, revealX, revealY, finalRadius, startRadius
        )

        circularReveal.duration = DEFAULT_EXIT_REVEAL_ML
        circularReveal.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                rootView.visibility = View.INVISIBLE
            }
        })
        circularReveal.start()
        finishAfterTransition()
    } else {
        finish()
    }
}

private fun startRevealActivityAnimation(rootView: View, x: Int, y: Int, startRadius: Float) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val finalRadius = (rootView.width.coerceAtLeast(rootView.height) * 1.1).toFloat()

        val circularReveal =
            ViewAnimationUtils.createCircularReveal(rootView, x, y, startRadius, finalRadius)
        circularReveal.duration = DEFAULT_ENTER_REVEAL_ML
        circularReveal.interpolator = AccelerateInterpolator()

        // make the view visible and start the animation
        rootView.visibility = View.VISIBLE
        circularReveal.start()
    }
}

fun Activity.makeRevealAnimation(
    view: View,
    intent: Intent,
    sharedElementName: String = ""
): ActivityOptionsCompat {
    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, sharedElementName)
    val location = IntArray(2)
    view.getLocationOnScreen(location)
    val revealX = (location[0] + view.width / 2)
    val revealY = (location[1] + view.height / 2)

    intent.putExtra(EXTRA_CIRCULAR_REVEAL_X, revealX)
    intent.putExtra(EXTRA_CIRCULAR_REVEAL_Y, revealY)
    intent.putExtra(EXTRA_CIRCULAR_REVEAL_START_RADIUS, view.height.toFloat())
    return options
}