package com.kirishhaa.photonotes.domain.users

import android.net.Uri
import com.kirishhaa.photonotes.domain.Language
import com.kirishhaa.photonotes.domain.LocalUser
import kotlinx.coroutines.flow.Flow

interface LocalUsersRepository {

    fun getEnteredUser(): Flow<LocalUser?>

    fun getLocalUsers(): Flow<List<LocalUser>>

    //throws exception if unsuccessful operation
    //wrong password or login
    suspend fun signIn(userId: Int, login: String, password: String, remember: Boolean)

    //throws
    //wrong username exception
    //wrong login exception
    //wrong password exception
    suspend fun signUp(
        username: String,
        login: String,
        password: String,
        remember: Boolean,
        imagePath: Uri?
    )

    //throws
    //UserNotFoundException
    //WrongLoginException
    //WrongNewLoginException
    //EmailsAreNotTheSameException
    //ReadWriteException
    suspend fun changeEmail(
        userId: Int,
        currentEmail: String,
        newEmail: String,
        repeatNewEmail: String
    )

    suspend fun changeUsername(userId: Int, newUsername: String)

    //throws
    //passwords are not the same exception
    //wrong password exception
    suspend fun changePassword(
        userId: Int,
        currentPassword: String,
        newPassword: String,
        repeatNewPassword: String
    )

    fun getUserLanguage(userId: Int): Flow<Language?>

    suspend fun selectNextLanguage(userId: Int)

    suspend fun selectPreviousLanguage(userId: Int)

    suspend fun deleteProfile(userId: Int)

    suspend fun logOut(userId: Int)

    suspend fun changeUserPhoto(userId: Int, uri: Uri?)

}