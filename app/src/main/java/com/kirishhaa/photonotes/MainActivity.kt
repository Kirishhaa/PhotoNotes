package com.kirishhaa.photonotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.maps.android.ktx.BuildConfig
import com.kirishhaa.photonotes.presentation.navigation.RootNavigation

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            RootNavigation()
        }
    }
}