package com.example.tasktracker.domain.repository

sealed class AuthResult {
    data class Success(val userId: Long, val username: String) : AuthResult()
    data class Error(val message: String) : AuthResult()
}

interface AuthRepository {
    suspend fun login(username: String, password: String): AuthResult
    suspend fun register(username: String, password: String): AuthResult
}
