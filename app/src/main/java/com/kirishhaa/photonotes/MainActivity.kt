package com.kirishhaa.photonotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.kirishhaa.photonotes.domain.Language
import com.kirishhaa.photonotes.presentation.navigation.RootNavigation
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            RootNavigation()
        }
    }

    fun changeLanguage(language: Language) {
        val code = language.getCode()
        val newLocale = Locale(code)
        Locale.setDefault(newLocale)
        val config = resources.configuration
        config.setLocale(newLocale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

}