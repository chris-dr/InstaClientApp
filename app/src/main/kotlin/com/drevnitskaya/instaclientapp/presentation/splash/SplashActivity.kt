package com.drevnitskaya.instaclientapp.presentation.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.drevnitskaya.instaclientapp.presentation.login.LoginActivity
import com.drevnitskaya.instaclientapp.presentation.main.MainActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(LoginActivity.getStartIntent(this))
        finish()
    }
}