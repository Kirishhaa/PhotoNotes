package com.kirishhaa.photonotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.kirishhaa.photonotes.presentation.languagescreen.LanguageScreen
import com.kirishhaa.photonotes.presentation.localusersscreen.LocalUsersScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LocalUsersScreen()
        }
    }
}