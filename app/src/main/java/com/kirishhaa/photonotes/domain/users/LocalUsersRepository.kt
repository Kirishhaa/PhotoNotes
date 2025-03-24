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

    fun getUserLanguage(userId: Int): Flow<Language>

    suspend fun selectNextLanguage(userId: Int)

    suspend fun selectPreviousLanguage(userId: Int)

    suspend fun deleteProfile(userId: Int)

    suspend fun logOut(userId: Int)

    object Mock : LocalUsersRepository {

        private val idAppender = AtomicInteger(0)

        private val _users: MutableStateFlow<List<LocalUser>> = MutableStateFlow(
            listOf(
                LocalUser(
                    id = idAppender.getAndIncrement(),
                    imagePath = "",
                    entered = false,
                    login = "test",
                    password = "123",
                    name = "Admin",
                    remember = true,
                    language = Language(
                        name = "Ukrainian"
                    ),
                )
            )
        )

        private val languagesGeneral =
            listOf(
                Language(
                    name = "Ukrainian"
                ),
                Language(
                    name = "English"
                )
            )

        private val usersLanguages = MutableStateFlow(mapOf<Int,Language>())

        override fun getEnteredUser(): Flow<LocalUser?> =
            _users.map { users -> users.firstOrNull { it.entered } }

        override fun getLocalUsers(): Flow<List<LocalUser>> = _users

        override fun getUserLanguage(userId: Int): Flow<Language> {
            return usersLanguages.map { it[userId] ?: Language(name = "English") }
        }

        override suspend fun selectNextLanguage(userId: Int) = withContext(Dispatchers.IO) {
            delay(1000)
            val currentId = getUserLanguage(userId).first().name
            val index = languagesGeneral.indexOfFirst { it.name == currentId }
            val currentMap = usersLanguages.value.toMutableMap()
            if(index + 1 == languagesGeneral.size) {
                currentMap[userId] = languagesGeneral[0]
            } else {
                currentMap[userId] = languagesGeneral[index + 1]
            }
            usersLanguages.value = currentMap
        }

        override suspend fun selectPreviousLanguage(userId: Int) = withContext(Dispatchers.IO) {
            delay(1000)
            val currentId = getUserLanguage(userId).first().name
            val index = languagesGeneral.indexOfFirst { it.name == currentId }
            val currentMap = usersLanguages.value.toMutableMap()
            if(index - 1 == -1) {
                currentMap[userId] = languagesGeneral[languagesGeneral.size-1]
            } else {
                currentMap[userId] = languagesGeneral[index - 1]
            }
            usersLanguages.value = currentMap
        }

        override suspend fun deleteProfile(userId: Int) = withContext(Dispatchers.IO) {
            delay(1000)
            val list = _users.value.toMutableList()
            val index = list.indexOfFirst { it.id == userId }
            if(index != -1) {
                list.removeAt(index)
                _users.value = list
            }
        }

        override suspend fun logOut(userId: Int) = withContext(Dispatchers.IO) {
            delay(1000)
            val list = _users.value.toMutableList()
            val index = list.indexOfFirst { it.id == userId }
            if(index != -1) {
                list[index] = list[index].copy(entered = false, remember = false)
                _users.value = list
            }
        }

        override suspend fun signIn(
            userId: Int,
            login: String,
            password: String,
            remember: Boolean
        ) = withContext(Dispatchers.Default) {
            delay(500)
            val users = _users.value.toMutableList()
            val index = users.indexOfFirst { it.id == userId }
            if (index != -1) {
                if (users[index].password != password) throw WrongPasswordException()
                if (users[index].login != login) throw WrongLoginException()
                users[index] = users[index].copy(entered = true)
                _users.value = users
            }
        }

        override suspend fun signUp(
            username: String,
            login: String,
            password: String,
            remember: Boolean,
            imagePath: String?
        ): Unit = withContext(Dispatchers.Default) {
            delay(500)
            val newUser = LocalUser(
                id = idAppender.getAndIncrement(),
                imagePath = imagePath ?: "",
                entered = true,
                remember = remember,
                password = password,
                login = login,
                name = username,
                language = Language(
                    name = "English",
                )
            )
            _users.value = _users.value.toMutableList().apply { add(newUser) }
        }

        override suspend fun changeEmail(userId: Int, currentEmail: String, newEmail: String, repeatNewEmail: String) = withContext(Dispatchers.IO) {
            delay(1000)
            val mutable = _users.value.toMutableList()
            val index = mutable.indexOfFirst { it.id == userId }
            if (index != -1) {
                val user = mutable[index]
                if (user.login != currentEmail) throw WrongLoginException()
                if (newEmail != repeatNewEmail) throw EmailsAreNotTheSameException()
                val newUser = user.copy(login = newEmail)
                mutable[index] = newUser
                _users.value = mutable
            }
        }

        override suspend fun changeUsername(userId: Int, newUsername: String) = withContext(Dispatchers.IO) {
            delay(1000)
            val mutable = _users.value.toMutableList()
            val index = mutable.indexOfFirst { it.id == userId }
            if(index != -1) {
                val user = mutable[index]
                val newuSER = user.copy(name = newUsername)
                mutable[index] = newuSER
                _users.value = mutable
            }
        }

        override suspend fun changePassword(userId: Int, currentPassword: String, newPassword: String, repeatNewPassword: String) = withContext(Dispatchers.IO) {
            delay(1000)
            val mutable = _users.value.toMutableList()
            val index = mutable.indexOfFirst { it.id == userId }
            if(index != -1) {
                val user = mutable[index]
                if(user.password != currentPassword) throw WrongPasswordException()
                if(newPassword != repeatNewPassword) throw PasswordsAreNotTheSameException()
                val newUser = user.copy(password = newPassword)
                mutable[index] = newUser
                _users.value = mutable
            }
        }
    }

}