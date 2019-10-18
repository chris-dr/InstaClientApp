package com.drevnitskaya.instaclientapp.presentation.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.updatePadding
import com.drevnitskaya.instaclientapp.R
import com.drevnitskaya.instaclientapp.extensions.makeRevealAnimation
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initViews()
    }

    private fun initViews() {
        loginRoot.systemUiVisibility = SYSTEM_UI_FLAG_LAYOUT_STABLE or
                SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        loginRoot.setOnApplyWindowInsetsListener { _, windowInsets ->
            windowInsets
        }
        loginBackgroundImg.setOnApplyWindowInsetsListener { _, windowInsets ->
            windowInsets
        }
        loginForm.setOnApplyWindowInsetsListener { view, windowInsets ->
            view.updatePadding(bottom = windowInsets.systemWindowInsetBottom)
            windowInsets
        }

        loginButton.setOnClickListener {
            val intent = LoginWebActivity.getStartIntent(this)
            val options = makeRevealAnimation(loginButton, intent)
            ActivityCompat.startActivity(this, intent, options.toBundle())
        }
    }
}