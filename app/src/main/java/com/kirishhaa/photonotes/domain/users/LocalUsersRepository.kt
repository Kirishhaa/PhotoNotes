package com.kirishhaa.photonotes.domain.users

import com.kirishhaa.photonotes.domain.Language
import com.kirishhaa.photonotes.domain.LocalUser
import com.kirishhaa.photonotes.domain.exceptions.EmailsAreNotTheSameException
import com.kirishhaa.photonotes.domain.exceptions.PasswordsAreNotTheSameException
import com.kirishhaa.photonotes.domain.exceptions.WrongLoginException
import com.kirishhaa.photonotes.domain.exceptions.WrongPasswordException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicInteger

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
        imagePath: String?
    )

    //throws
    //emails are not the same exception
    //wrong login exception
    suspend fun changeEmail(userId: Int, currentEmail: String, newEmail: String, repeatNewEmail: String)

    suspend fun changeUsername(userId: Int, newUsername: String)

    //throws
    //passwords are not the same exception
    //wrong password exception
    suspend fun changePassword(userId: Int, currentPassword: String, newPassword: String, repeatNewPassword: String)

    fun getUserLanguage(userId: Int): Flow<Language?>

    suspend fun selectNextLanguage(userId: Int)

    suspend fun selectPreviousLanguage(userId: Int)

    suspend fun deleteProfile(userId: Int)

    suspend fun logOut(userId: Int)

}