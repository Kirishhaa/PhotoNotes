package com.kirishhaa.photonotes.domain

data class LocalUser(
    val id: Int,
    val imagePath: String?,
    val entered: Boolean,
    val password: String,
    val login: String,
    val name: String,
    val language: Language
)