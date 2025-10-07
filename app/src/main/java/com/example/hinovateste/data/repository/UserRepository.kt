package com.example.hinovateste.data.repository

import com.example.hinovateste.data.local.MockUserDataSource
import com.example.hinovateste.data.model.User

class UserRepository {
    fun login(cpf: String, password: String): User? {
        return if (cpf.isNotBlank() && password.length >= 8) {
            MockUserDataSource.user
        } else null
    }
}