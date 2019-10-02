package com.drevnitskaya.instaclientapp.presentation.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.drevnitskaya.instaclientapp.R
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
        loginRoot.systemUiVisibility =
            SYSTEM_UI_FLAG_LAYOUT_STABLE or SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        ViewCompat.setOnApplyWindowInsetsListener(loginRoot) { _, insets ->
            insets
        }
        ViewCompat.setOnApplyWindowInsetsListener(loginBackgroundImg) { _, insets ->
            insets
        }



        initViews()
    }

    private fun initViews() {
    }
}