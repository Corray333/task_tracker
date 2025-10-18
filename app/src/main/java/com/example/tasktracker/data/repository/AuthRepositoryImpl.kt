package com.example.tasktracker.data.repository

import com.example.tasktracker.data.local.UserPreferencesManager
import com.example.tasktracker.data.local.dao.UserDao
import com.example.tasktracker.data.local.entity.UserEntity
import com.example.tasktracker.domain.repository.AuthRepository
import com.example.tasktracker.domain.repository.AuthResult
import java.security.MessageDigest
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val userPreferencesManager: UserPreferencesManager
) : AuthRepository {

    override suspend fun login(username: String, password: String): AuthResult {
        if (username.isBlank() || password.isBlank()) {
            return AuthResult.Error("Username and password cannot be empty")
        }

        val user = userDao.getUserByUsername(username)

        return if (user != null) {
            // User exists, check password
            val passwordHash = hashPassword(password)
            if (user.passwordHash == passwordHash) {
                userPreferencesManager.userId = user.id
                userPreferencesManager.username = user.username
                AuthResult.Success(user.id, user.username)
            } else {
                AuthResult.Error("Invalid password")
            }
        } else {
            // User doesn't exist, create new user
            register(username, password)
        }
    }

    override suspend fun register(username: String, password: String): AuthResult {
        if (username.isBlank() || password.isBlank()) {
            return AuthResult.Error("Username and password cannot be empty")
        }

        if (username.length < 3) {
            return AuthResult.Error("Username must be at least 3 characters")
        }

        if (password.length < 4) {
            return AuthResult.Error("Password must be at least 4 characters")
        }

        val existingUser = userDao.getUserByUsername(username)
        if (existingUser != null) {
            return AuthResult.Error("User already exists")
        }

        val passwordHash = hashPassword(password)
        val newUser = UserEntity(
            username = username,
            passwordHash = passwordHash
        )

        val userId = userDao.insertUser(newUser)
        userPreferencesManager.userId = userId
        userPreferencesManager.username = username

        return AuthResult.Success(userId, username)
    }

    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
