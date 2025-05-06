package com.kirishhaa.photonotes.presentation.profile.changelanguagescreen.languagescreen

sealed interface LanguageEvent {

    class SendMessage(val message: String): LanguageEvent

}