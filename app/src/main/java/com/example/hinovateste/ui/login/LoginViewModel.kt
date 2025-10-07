package com.example.hinovateste.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.hinovateste.R
import com.example.hinovateste.data.local.UserPreferences
import com.example.hinovateste.data.repository.UserRepository
import com.example.hinovateste.domain.usecase.ValidateLoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

data class LoginUiState(
    val cpf: String = "",
    val senha: String = "",
    val error: String? = null,
    val userName: String? = null,
    val isLogged: Boolean = false
)

class LoginViewModel(
    private val repository: UserRepository,
    private val validate: ValidateLoginUseCase,
    private val prefs: UserPreferences,
    private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    init {
        checkExistingLogin()
    }

    private fun checkExistingLogin() {
        viewModelScope.launch {
            val (cpf, name) = prefs.userFlow.first()
            if (!cpf.isNullOrBlank() && !name.isNullOrBlank()) {
                _uiState.value = _uiState.value.copy(
                    cpf = cpf,
                    userName = name,
                    isLogged = true
                )
            }
        }
    }

    fun onCpfChange(value: String) {
        _uiState.value = _uiState.value.copy(cpf = value)
    }

    fun onPasswordChange(value: String) {
        _uiState.value = _uiState.value.copy(senha = value)
    }

    fun onLoginClick() {
        viewModelScope.launch {
            val cpf = _uiState.value.cpf
            val senha = _uiState.value.senha

            if (!validate.isCpfValid(cpf)) {
                _uiState.value = _uiState.value.copy(error = context.getString(R.string.invalid_cpf))
                return@launch
            }

            if (!validate.isPasswordValid(senha)) {
                _uiState.value = _uiState.value.copy(error = context.getString(R.string.invalid_password))
                return@launch
            }

            val user = repository.login(cpf, senha)
            if (user != null) {
                prefs.saveUser(cpf, user.nome)
                _uiState.value = _uiState.value.copy(
                    error = null,
                    userName = user.nome,
                    isLogged = true
                )
            } else {
                _uiState.value = _uiState.value.copy(error = context.getString(R.string.wrong_credentials))
            }
        }
    }
}

class LoginViewModelFactory(
    private val repository: UserRepository,
    private val validate: ValidateLoginUseCase,
    private val prefs: UserPreferences,
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(repository, validate, prefs, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}