package com.kirishhaa.photonotes.data.repos.localusers

import android.content.Context
import android.net.Uri
import com.kirishhaa.photonotes.data.db.dao.LocalUsersDao
import com.kirishhaa.photonotes.data.db.entity.LocalUserEntity
import com.kirishhaa.photonotes.domain.Language
import com.kirishhaa.photonotes.domain.LocalUser
import com.kirishhaa.photonotes.domain.exceptions.EmailsAreNotTheSameException
import com.kirishhaa.photonotes.domain.exceptions.PasswordsAreNotTheSameException
import com.kirishhaa.photonotes.domain.exceptions.ReadWriteException
import com.kirishhaa.photonotes.domain.exceptions.UserAlreadyExistException
import com.kirishhaa.photonotes.domain.exceptions.UserNotFoundException
import com.kirishhaa.photonotes.domain.exceptions.WrongLoginException
import com.kirishhaa.photonotes.domain.exceptions.WrongNewLoginException
import com.kirishhaa.photonotes.domain.exceptions.WrongNewPasswordException
import com.kirishhaa.photonotes.domain.exceptions.WrongPasswordException
import com.kirishhaa.photonotes.domain.exceptions.WrongUsernameException
import com.kirishhaa.photonotes.domain.users.LocalUsersRepository
import com.kirishhaa.photonotes.extensions.coroutineTryCatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File

class LocalUsersRepositoryImpl(
    private val localUsersDao: LocalUsersDao,
    private val passwordValidator: PasswordValidator,
    private val loginValidator: LoginValidator,
    private val usernameValidator: UsernameValidator,
    private val localUserEntityMapper: LocalUserEntityMapper,
    private val context: Context
) : LocalUsersRepository {

    override fun getEnteredUser(): Flow<LocalUser?> {
        return localUsersDao.getAllUsers().map { users ->
            val localUserEntity =
                users.firstOrNull { userEntity -> userEntity.entered }
            if (localUserEntity != null) {
                val languageEntity = localUsersDao.getLanguageById(localUserEntity.langId)
                localUserEntityMapper.map(localUserEntity, languageEntity)
            } else {
                null
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getLocalUsers(): Flow<List<LocalUser>> {
        return localUsersDao.getAllUsers().map { users ->
            users.map { userEntity ->
                val languageEntity = localUsersDao.getLanguageById(userEntity.langId)
                localUserEntityMapper.map(userEntity, languageEntity)
            }
        }.flowOn(Dispatchers.IO)
    }

    //throws
    //user not found exception
    //wrong password exception
    //wrong login exception
    //ReadWriteException
    override suspend fun signIn(userId: Int, login: String, password: String, remember: Boolean) =
        withContext(Dispatchers.IO) {
            coroutineTryCatcher(
                tryBlock = {
                    val entity =
                        localUsersDao.getUser(userId).first() ?: throw UserNotFoundException()
                    if (entity.password != password) throw WrongPasswordException()
                    if (entity.login != login) throw WrongLoginException()
                    val newEntity = entity.copy(
                        entered = true,
                    )
                    localUsersDao.updateUser(newEntity)
                },
                catchBlock = { throwable ->
                    if (throwable !is UserNotFoundException && throwable !is WrongPasswordException && throwable !is WrongLoginException) {
                        throw ReadWriteException()
                    } else throw throwable
                }
            )
        }

    //throws
    //UserAlreadyExistException
    //WrongLoginException
    //WrongPasswordException
    //ReadWriteException
    //WrongUsernameException
    override suspend fun signUp(
        username: String,
        login: String,
        password: String,
        remember: Boolean,
        imagePath: Uri?
    ) = withContext(Dispatchers.IO) {
        coroutineTryCatcher(
            tryBlock = {
                val path = imagePath?.let { getPathFromUri(it) }
                val usersEntity = localUsersDao.getAllUsers().first()
                if (usersEntity.any { it.name == username }) throw UserAlreadyExistException()
                if (usernameValidator.validate(username).not()) throw WrongUsernameException()
                if (loginValidator.validate(login).not()) throw WrongLoginException()
                if (passwordValidator.validate(password).not()) throw WrongPasswordException()
                val newUser = LocalUserEntity(
                    id = 0,
                    imagePath = path ?: "",
                    entered = true,
                    password = password,
                    login = login,
                    name = username,
                    langId = localUsersDao.getAllLanguages().first().id
                )
                localUsersDao.insertUser(newUser)
            },
            catchBlock = { throwable ->
                if (throwable !is UserAlreadyExistException && throwable !is WrongLoginException && throwable !is WrongPasswordException && throwable !is WrongUsernameException) {
                    throw ReadWriteException()
                } else {
                    throw throwable
                }
            }
        )
    }

    private suspend fun getPathFromUri(uri: Uri): String? = withContext(Dispatchers.IO) {
        coroutineTryCatcher(
            tryBlock = {
                val fileToWrite = File(context.cacheDir, "file_in${System.currentTimeMillis()}")
                fileToWrite.createNewFile()
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val bytes = inputStream.readBytes()
                    fileToWrite.outputStream().use { outputStream ->
                        outputStream.write(bytes)
                    }
                    fileToWrite.absolutePath
                }
            },
            catchBlock = {
                null
            }
        )
    }


    //throws
    //UserNotFoundException
    //WrongLoginException
    //WrongNewLoginException
    //EmailsAreNotTheSameException
    //ReadWriteException
    override suspend fun changeEmail(
        userId: Int,
        currentEmail: String,
        newEmail: String,
        repeatNewEmail: String
    ) = withContext(Dispatchers.IO) {
        coroutineTryCatcher(
            tryBlock = {
                val userEntity =
                    localUsersDao.getUser(userId).first() ?: throw UserNotFoundException()
                if (userEntity.login != currentEmail) throw WrongLoginException()
                if (loginValidator.validate(newEmail).not()) throw WrongNewLoginException()
                if (newEmail != repeatNewEmail) throw EmailsAreNotTheSameException()
                val newUserEntity = userEntity.copy(login = newEmail)
                localUsersDao.updateUser(newUserEntity)
            },
            catchBlock = { throwable ->
                if (throwable !is UserNotFoundException && throwable !is WrongLoginException && throwable !is WrongNewLoginException && throwable !is EmailsAreNotTheSameException) {
                    throw ReadWriteException()
                } else throw throwable
            }
        )
    }

    //throws
    //UserNotFoundException
    //UserAlreadyExistException
    //ReadWriteException
    //WrongUsernameException
    override suspend fun changeUsername(userId: Int, newUsername: String) =
        withContext(Dispatchers.IO) {
            coroutineTryCatcher(
                tryBlock = {
                    if (usernameValidator.validate(newUsername)
                            .not()
                    ) throw WrongUsernameException()
                    val userEntity =
                        localUsersDao.getUser(userId).first() ?: throw UserNotFoundException()
                    val userWithNewUsername = localUsersDao.getAllUsers()
                        .map { users -> users.firstOrNull { it.name == newUsername } }.first()
                    if (userWithNewUsername != null) throw UserAlreadyExistException()
                    val newUserEntity = userEntity.copy(name = newUsername)
                    localUsersDao.updateUser(newUserEntity)
                },
                catchBlock = { throwable ->
                    if (throwable !is UserNotFoundException && throwable !is UserAlreadyExistException && throwable !is WrongUsernameException) {
                        throw ReadWriteException()
                    } else throw throwable
                }
            )
        }

    //throws
    //UserNotFoundException
    //WrongPasswordException
    //WrongNewPasswordException
    //PasswordsAreNotTheSameException
    //ReadWriteException
    override suspend fun changePassword(
        userId: Int,
        currentPassword: String,
        newPassword: String,
        repeatNewPassword: String
    ) = withContext(Dispatchers.IO) {
        coroutineTryCatcher(
            tryBlock = {
                val userEntity =
                    localUsersDao.getUser(userId).first() ?: throw UserNotFoundException()
                if (userEntity.password != currentPassword) throw WrongPasswordException()
                if (passwordValidator.validate(newPassword).not()) throw WrongNewPasswordException()
                if (newPassword != repeatNewPassword) throw PasswordsAreNotTheSameException()
                val newUserEntity = userEntity.copy(password = newPassword)
                localUsersDao.updateUser(newUserEntity)
            },
            catchBlock = { throwable ->
                if (throwable !is UserNotFoundException && throwable !is WrongPasswordException && throwable !is WrongNewPasswordException && throwable !is PasswordsAreNotTheSameException) {
                    throw ReadWriteException()
                } else throw throwable
            }
        )
    }

    override fun getUserLanguage(userId: Int): Flow<Language?> {
        return localUsersDao.getUser(userId).map { userEntity ->
            if (userEntity != null) {
                localUsersDao.getLanguageById(userEntity.langId).toLanguage()
            } else {
                null
            }
        }.flowOn(Dispatchers.IO)
    }


    //throws
    //UserNotFoundException
    //ReadWriteException
    override suspend fun selectNextLanguage(userId: Int) = withContext(Dispatchers.IO) {
        coroutineTryCatcher(
            tryBlock = {
                val userEntity =
                    localUsersDao.getUser(userId).first() ?: throw UserNotFoundException()
                val languageEntity = localUsersDao.getLanguageById(userEntity.langId)
                val languagesEntity = localUsersDao.getAllLanguages()
                val index = languagesEntity.indexOfFirst { it.name == languageEntity.name }
                if (index == -1) {
                    //unsupported language
                    val firstEntity = languagesEntity.first()
                    val newUserEntity = userEntity.copy(langId = firstEntity.id)
                    localUsersDao.updateUser(newUserEntity)
                } else {
                    val nextLanguageIndex = (index + 1) % languagesEntity.size
                    val newLanguageEntity = languagesEntity[nextLanguageIndex]
                    val newUserEntity = userEntity.copy(langId = newLanguageEntity.id)
                    localUsersDao.updateUser(newUserEntity)
                }
            },
            catchBlock = { throwable ->
                if (throwable !is UserNotFoundException) {
                    throw ReadWriteException()
                } else {
                    throw throwable
                }
            }
        )
    }

    //throws
    //UserNotFoundException
    //ReadWriteException
    override suspend fun selectPreviousLanguage(userId: Int) = withContext(Dispatchers.IO) {
        coroutineTryCatcher(
            tryBlock = {
                val userEntity =
                    localUsersDao.getUser(userId).first() ?: throw UserNotFoundException()
                val languageEntity = localUsersDao.getLanguageById(userEntity.langId)
                val languagesEntity = localUsersDao.getAllLanguages()
                val index = languagesEntity.indexOfFirst { it.name == languageEntity.name }
                if (index == -1) {
                    //unsupported language
                    val firstEntity = languagesEntity.first()
                    val newUserEntity = userEntity.copy(langId = firstEntity.id)
                    localUsersDao.updateUser(newUserEntity)
                } else {
                    var prevLanguageIndex = (index - 1)
                    if (prevLanguageIndex < 0) prevLanguageIndex = languagesEntity.size - 1
                    val newLanguageEntity = languagesEntity[prevLanguageIndex]
                    val newUserEntity = userEntity.copy(langId = newLanguageEntity.id)
                    localUsersDao.updateUser(newUserEntity)
                }
            },
            catchBlock = { throwable ->
                if (throwable !is UserNotFoundException) {
                    throw ReadWriteException()
                } else throw throwable
            }
        )
    }

    //throws
    //UserNotFoundException
    //ReadWriteException
    override suspend fun deleteProfile(userId: Int) = withContext(Dispatchers.IO) {
        coroutineTryCatcher(
            tryBlock = {
                val userEntity =
                    localUsersDao.getUser(userId).first() ?: throw UserNotFoundException()
                localUsersDao.deleteUser(userEntity)
            },
            catchBlock = { throwable ->
                if (throwable !is UserNotFoundException) {
                    throw ReadWriteException()
                } else throw throwable
            }
        )
    }

    //throws
    //UserNotFoundException
    //ReadWriteException
    override suspend fun logOut(userId: Int) = withContext(Dispatchers.IO) {
        coroutineTryCatcher(
            tryBlock = {
                val userEntity =
                    localUsersDao.getUser(userId).first() ?: throw UserNotFoundException()
                val newUserEntity = userEntity.copy(
                    entered = false
                )
                localUsersDao.updateUser(newUserEntity)
            },
            catchBlock = { throwable ->
                if (throwable !is UserNotFoundException) {
                    throw ReadWriteException()
                } else throw throwable
            }
        )
    }

    override suspend fun changeUserPhoto(userId: Int, uri: Uri?) {
        coroutineTryCatcher(
            tryBlock = {
                val path = uri?.let { getPathFromUri(it) }
                val userEntity = localUsersDao.getUser(userId).first()
                userEntity?.copy(imagePath = path)?.let { newUserEntity ->
                    localUsersDao.updateUser(newUserEntity)
                }
            },
            catchBlock = {
                throw ReadWriteException()
            }
        )
    }
}