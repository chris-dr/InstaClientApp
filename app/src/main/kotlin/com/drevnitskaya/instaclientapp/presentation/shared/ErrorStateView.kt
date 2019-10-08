package com.drevnitskaya.instaclientapp.presentation.shared

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.drevnitskaya.instaclientapp.R
import kotlinx.android.synthetic.main.shared_error_state_view.view.*

class ErrorStateView : FrameLayout {
    var onRetryClicked: (() -> Unit)? = null

    constructor(context: Context) : super(context) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initialize()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        initialize()
    }

    private fun initialize() {
        LayoutInflater.from(context).inflate(R.layout.shared_error_state_view, this, true)
        errorStateRetryButton.setOnClickListener {
            onRetryClicked?.invoke()
        }
    }
}