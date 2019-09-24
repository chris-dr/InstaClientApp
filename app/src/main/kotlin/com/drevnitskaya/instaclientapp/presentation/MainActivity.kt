package com.drevnitskaya.instaclientapp.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.drevnitskaya.instaclientapp.R

class MainActivity : AppCompatActivity() {

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
