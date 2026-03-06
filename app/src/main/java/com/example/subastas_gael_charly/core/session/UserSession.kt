package com.example.subastas_gael_charly.core.session

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object UserSession {
    private val _userId = MutableStateFlow<Int?>(null)
    val userId = _userId.asStateFlow()

    private val _username = MutableStateFlow<String?>(null)
    val username = _username.asStateFlow()

    fun login(id: Int, username: String) {
        _userId.value = id
        _username.value = username
    }

    fun logout() {
        _userId.value = null
        _username.value = null
    }

    fun isLoggedIn() = _userId.value != null
    fun getCurrentUserId() = _userId.value
}