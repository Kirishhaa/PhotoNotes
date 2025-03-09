package com.kirishhaa.photonotes.domain

import com.kirishhaa.photonotes.domain.exceptions.WrongLoginException
import com.kirishhaa.photonotes.domain.exceptions.WrongPasswordException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

interface LocalUsersRepository {

    fun getLocalUsers(): Flow<List<LocalUser>>

    //throws exception if unsuccessful operation
    //wrong password or login
    suspend fun signIn(userId: Int, login: String, password: String, remember: Boolean)

    //throws
    //wrong username exception
    //wrong login exception
    //wrong password exception
    suspend fun signUp(username: String, login: String, password: String, remember: Boolean)


    class Mock: LocalUsersRepository {

        private val _users: MutableStateFlow<List<LocalUser>> = MutableStateFlow(
            List(4) { LocalUser(
                id = it + 1,
                imagePath = "",
                entered = false,
                hashLogin = 1,
                hashPassword = 1,
                name = "Username",
                language = Language(
                    selected = true,
                    name = "Ukrainian"
                )
            ) }
        )

        override fun getLocalUsers(): Flow<List<LocalUser>> = _users

        override suspend fun signIn(
            userId: Int,
            login: String,
            password: String,
            remember: Boolean
        ) = withContext(Dispatchers.Default) {
            delay(3000)
            val users = _users.value.toMutableList()
            val index = users.indexOfFirst { it.id == userId }
            throw WrongPasswordException()
//            users[index] = users[index].copy(entered = true)
            _users.value = users
        }

        override suspend fun signUp(
            username: String,
            login: String,
            password: String,
            remember: Boolean
        ) = withContext(Dispatchers.Default) {
            delay(3000)
            throw WrongLoginException()
        }

    }

}