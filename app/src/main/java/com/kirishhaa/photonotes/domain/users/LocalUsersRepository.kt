package com.kirishhaa.photonotes.domain.users

import com.kirishhaa.photonotes.domain.Language
import com.kirishhaa.photonotes.domain.LocalUser
import com.kirishhaa.photonotes.domain.exceptions.WrongLoginException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

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
    suspend fun signUp(username: String, login: String, password: String, remember: Boolean)


    object Mock: LocalUsersRepository {

        private val _users: MutableStateFlow<List<LocalUser>> = MutableStateFlow(
            List(4) { LocalUser(
                id = it + 1,
                imagePath = "",
                entered = false,
                login = "Test@gmail.com",
                password = "123",
                name = "Username",
                language = Language(
                    selected = true,
                    name = "Ukrainian"
                )
            ) }
        )

        override fun getEnteredUser(): Flow<LocalUser?> = _users.map { users -> users.firstOrNull { it.entered } }

        override fun getLocalUsers(): Flow<List<LocalUser>> = _users

        override suspend fun signIn(
            userId: Int,
            login: String,
            password: String,
            remember: Boolean
        ) = withContext(Dispatchers.Default) {
            delay(500)
            val users = _users.value.toMutableList()
            val index = users.indexOfFirst { it.id == userId }
//            throw WrongPasswordException()
            users[index] = users[index].copy(entered = true)
            _users.value = users
        }

        override suspend fun signUp(
            username: String,
            login: String,
            password: String,
            remember: Boolean
        ) = withContext(Dispatchers.Default) {
            delay(500)
            throw WrongLoginException()
        }

    }

}