package com.kirishhaa.photonotes.data.repos.localusers

import android.util.Patterns

class LoginValidator {

    fun validate(login: String): Boolean {
        return  (login.isEmpty() || Patterns.EMAIL_ADDRESS.matcher(login).matches().not()).not()
    }

}