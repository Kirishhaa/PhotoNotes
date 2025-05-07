package com.kirishhaa.photonotes.domain

class Language(
    val name: String,
) {

    fun getCode(): String {
        return when(name) {
            "Ukrainian" -> "uk"
            "English" -> "en"
            else -> throw IllegalStateException()
        }
    }

}
