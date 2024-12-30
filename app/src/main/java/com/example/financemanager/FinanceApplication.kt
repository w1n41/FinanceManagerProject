package com.example.financemanager

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp

class FinanceApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            FirebaseApp.initializeApp(applicationContext)
            Log.d("FinanceApp", "Firebase initialized successfully")
        } catch (e: Exception) {
            Log.e("FinanceApp", "Failed to initialize Firebase", e)
        }
    }

    companion object {
        lateinit var instance: FinanceApplication
            private set
    }

    init {
        instance = this
    }
}