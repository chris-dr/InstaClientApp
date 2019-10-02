package com.drevnitskaya.instaclientapp.application

import android.app.Application
import com.drevnitskaya.instaclientapp.BuildConfig
import com.drevnitskaya.instaclientapp.di.appModule
import com.drevnitskaya.instaclientapp.di.loginModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.module.Module

class InstaClientApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            if (BuildConfig.DEBUG) {
                androidLogger()
            }
            androidContext(this@InstaClientApp)
            modules(getKoinModules())
        }
    }

    private fun getKoinModules(): List<Module> {
        return listOf(appModule, loginModule)
    }

}