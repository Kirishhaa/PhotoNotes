package com.kirishhaa.photonotes.domain

data class LocalUser(
    val id: Int,
    val imagePath: String,
    val entered: Boolean,
    val hashPassword: Long,
    val hashLogin: Long,
    val name: String,
    val language: Language
)