package com.drevnitskaya.instaclientapp.presentation.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.drevnitskaya.instaclientapp.presentation.login.LoginActivity
import com.drevnitskaya.instaclientapp.presentation.profile.ProfileActivity
import org.koin.android.viewmodel.ext.android.viewModel


class SplashActivity : AppCompatActivity() {
    private val viewModel: SplashViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.openLogin.observe(this, Observer {
            startActivity(LoginActivity.getStartIntent(this))
            finish()
        })
        viewModel.openProfile.observe(this, Observer {
            startActivity(ProfileActivity.getStartIntent(this))
            finish()
        })
    }
}