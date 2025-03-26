package com.kirishhaa.photonotes.data.repos.localusers

class PasswordValidator {

    fun validate(password: String): Boolean {
        return (password.isEmpty() || password.length < 8 || password.length > 16).not()
    }

}