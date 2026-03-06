package com.example.subastas_gael_charly.features.auth.data.repositories

import com.example.subastas_gael_charly.features.auth.data.datasources.remote.api.AuthApi
import com.example.subastas_gael_charly.features.auth.data.datasources.remote.models.LoginRequest
import com.example.subastas_gael_charly.features.auth.domain.entities.User
import com.example.subastas_gael_charly.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi
) : AuthRepository {

    override suspend fun login(username: String, password: String): Result<User> {
        return try {
            val response = api.login(LoginRequest(username, password))
            if (response.isSuccessful) {
                val body = response.body()!!
                Result.success(User(id = body.user.id, username = body.user.username))
            } else {
                Result.failure(Exception("Credenciales incorrectas"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun register(username: String, password: String): Result<String> {
        return try {
            val response = api.register(LoginRequest(username, password))
            if (response.isSuccessful) {
                Result.success(response.body()?.message ?: "Registrado correctamente")
            } else {
                Result.failure(Exception("Error al registrar usuario"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}