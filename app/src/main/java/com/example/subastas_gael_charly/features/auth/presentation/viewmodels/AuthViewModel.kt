package com.example.subastas_gael_charly.features.auth.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.subastas_gael_charly.features.auth.domain.usecases.LoginUseCase
import com.example.subastas_gael_charly.features.auth.domain.usecases.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val username: String = "",
    val password: String = ""
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<AuthEvent>()
    val events = _events.asSharedFlow()

    fun onUsernameChange(value: String) {
        _uiState.value = _uiState.value.copy(username = value)
    }

    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(password = value)
    }

    fun login() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = loginUseCase(_uiState.value.username, _uiState.value.password)
            _uiState.value = _uiState.value.copy(isLoading = false)
            if (result.isSuccess) {
                _events.emit(AuthEvent.LoginSuccess)
            } else {
                _events.emit(AuthEvent.Error(result.exceptionOrNull()?.message ?: "Error desconocido"))
            }
        }
    }

    fun register() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            val result = registerUseCase(_uiState.value.username, _uiState.value.password)
            _uiState.value = _uiState.value.copy(isLoading = false)
            if (result.isSuccess) {
                _events.emit(AuthEvent.RegisterSuccess)
            } else {
                _events.emit(AuthEvent.Error(result.exceptionOrNull()?.message ?: "Error desconocido"))
            }
        }
    }
}

sealed class AuthEvent {
    object LoginSuccess : AuthEvent()
    object RegisterSuccess : AuthEvent()
    data class Error(val message: String) : AuthEvent()
}