package com.drevnitskaya.instaclientapp.presentation.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.drevnitskaya.instaclientapp.R
import com.drevnitskaya.instaclientapp.framework.api.AUTH_REDIRECT_URL
import kotlinx.android.synthetic.main.activity_web_login.*
import org.koin.android.viewmodel.ext.android.viewModel

class LoginWebActivity : AppCompatActivity() {
    private val viewModel: LoginWebViewModel by viewModel()

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, LoginWebActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_login)
        initViewModel()
        initViews()
    }

    override fun onBackPressed() {
        if (loginWebView.canGoBack()) {
            loginWebView.goBack()
            return
        }
        super.onBackPressed()
    }

    private fun initViewModel() {
        viewModel.showProgress.observe(this, Observer { shouldShow ->
            loginProgress.visibility = if (shouldShow) View.VISIBLE else View.GONE
        })
        viewModel.loadLoginForm.observe(this, Observer { url ->
            loginWebView.loadUrl(url)
        })
        viewModel.showLoginForm.observe(this, Observer { shouldShow ->
            TransitionManager.beginDelayedTransition(loginRoot)
            loginWebView.visibility = if (shouldShow) View.VISIBLE else View.GONE
        })
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initViews() {
        setSupportActionBar(loginToolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setTitle(R.string.login_title)
        }
        loginToolbar.setNavigationOnClickListener { onBackPressed() }
        loginWebView.settings.apply {
            javaScriptEnabled = true
        }
        loginWebView.webViewClient = AuthWebViewClient()
    }

    inner class AuthWebViewClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            viewModel.handleAuthUrl(url)
            return url?.startsWith(AUTH_REDIRECT_URL) == true
        }

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            val authUrl = request?.url.toString()
            viewModel.handleAuthUrl(authUrl)
            return authUrl.startsWith(AUTH_REDIRECT_URL)
        }

        override fun onReceivedError(
            view: WebView?,
            request: WebResourceRequest?,
            error: WebResourceError?
        ) {
            super.onReceivedError(view, request, error)
            viewModel.handleError()
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            viewModel.onLoadFormFinished(url)
        }
    }
}