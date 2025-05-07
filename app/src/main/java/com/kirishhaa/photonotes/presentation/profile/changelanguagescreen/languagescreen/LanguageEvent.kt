package com.kirishhaa.photonotes.presentation.profile.changelanguagescreen.languagescreen

sealed interface LanguageEvent {

    data object UserNotFound: LanguageEvent

    data object ReadWrite: LanguageEvent

}