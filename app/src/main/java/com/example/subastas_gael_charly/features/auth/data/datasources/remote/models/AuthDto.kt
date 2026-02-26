package com.example.subastas_gael_charly.features.auth.data.datasources.remote.models

data class LoginRequest(val username: String, val password: String)

data class LoginResponse(val message: String, val user: UserDto)

data class UserDto(val id: Int, val username: String)

data class MessageResponse(val message: String)