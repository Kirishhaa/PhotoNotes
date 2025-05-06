package com.kirishhaa.photonotes.presentation.auth.localusersscreen

import android.net.Uri

class SignUpData(
    val username: String,
    val login: String,
    val password: String,
    val picturePath: Uri?,
    val remember: Boolean,
)