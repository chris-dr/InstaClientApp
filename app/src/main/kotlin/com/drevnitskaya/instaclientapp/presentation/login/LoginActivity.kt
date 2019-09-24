package com.drevnitskaya.instaclientapp.presentation.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
        initViews()
    }

    private fun initViews() {
        loginPasswordLayout.isPasswordVisibilityToggleEnabled = true
    }
}