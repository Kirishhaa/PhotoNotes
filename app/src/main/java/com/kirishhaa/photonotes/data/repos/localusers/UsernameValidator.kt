package com.kirishhaa.photonotes.data.repos.localusers

class UsernameValidator {

    fun validate(username: String) : Boolean {
        return username.isNotEmpty()
    }

}