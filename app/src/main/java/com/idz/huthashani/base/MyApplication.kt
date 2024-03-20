package com.idz.huthashani.base

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(applicationContext)
        myContext = applicationContext
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var myContext: Context? = null

        fun getAppContext(): Context {
            return myContext ?: throw IllegalStateException("Application context not initialized yet.")
        }
    }
}