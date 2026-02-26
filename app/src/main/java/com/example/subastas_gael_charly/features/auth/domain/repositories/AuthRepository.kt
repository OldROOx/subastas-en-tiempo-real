package com.example.subastas_gael_charly.features.auth.domain.repositories

import com.example.subastas_gael_charly.features.auth.domain.entities.User

interface AuthRepository {
    suspend fun login(username: String, password: String): Result<User>
    suspend fun register(username: String, password: String): Result<String>
}