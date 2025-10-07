package com.example.hinovateste.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        private val KEY_CPF = stringPreferencesKey("cpf")
        private val KEY_NAME = stringPreferencesKey("user_name")
    }

    suspend fun saveUser(cpf: String, userName: String) {
        context.dataStore.edit { prefs ->
            prefs[KEY_CPF] = cpf
            prefs[KEY_NAME] = userName
        }
    }

    val userFlow: Flow<Pair<String?, String?>> = context.dataStore.data.map { prefs ->
        val cpf = prefs[KEY_CPF]
        val name = prefs[KEY_NAME]
        cpf to name
    }
}