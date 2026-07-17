package com.example.idletest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.idletest.ui.screen.AppRoot
import com.example.idletest.data.remote.RetrofitClient

// porneste GameScreen
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        RetrofitClient.initialize(applicationContext)

        setContent {
            AppRoot()
        }
    }
}
