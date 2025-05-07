package com.kirishhaa.photonotes.presentation.profile.changelanguagescreen.languagescreen

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kirishhaa.photonotes.R

@Preview
@Composable
fun LanguageScreen(viewModel: LanguageViewModel = viewModel(factory = LanguageViewModel.Factory)) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(0) {
        viewModel.events.collect { event ->
            when (event) {
                is LanguageEvent.UserNotFound -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.user_not_found),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                LanguageEvent.ReadWrite -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.read_write_exception),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LanguageDialog(
            state = state,
            onNextLanguage = viewModel::onNextLanguage,
            onPreviousLanguage = viewModel::onPreviousLanguage
        )
    }
}