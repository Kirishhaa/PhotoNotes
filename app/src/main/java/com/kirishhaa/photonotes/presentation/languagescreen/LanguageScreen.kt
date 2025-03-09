package com.kirishhaa.photonotes.presentation.languagescreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel

@Preview
@Composable
fun LanguageScreen(viewModel: LanguageViewModel = viewModel(factory = LanguageViewModel.Factory)) {
    val state by viewModel.state.collectAsState()
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