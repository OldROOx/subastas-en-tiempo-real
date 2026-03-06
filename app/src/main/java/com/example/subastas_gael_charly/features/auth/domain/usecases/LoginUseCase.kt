package com.example.subastas_gael_charly.features.auth.domain.usecases

import com.example.subastas_gael_charly.features.auth.domain.entities.User
import com.example.subastas_gael_charly.features.auth.domain.repositories.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String): Result<User> {
        if (username.isBlank() || password.isBlank()) {
            return Result.failure(Exception("Usuario y contraseña requeridos"))
        }
        return repository.login(username, password)
    }
}