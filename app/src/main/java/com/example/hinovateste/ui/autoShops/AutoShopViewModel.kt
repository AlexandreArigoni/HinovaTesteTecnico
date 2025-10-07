package com.example.hinovateste.ui.autoShops

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hinovateste.data.remote.AutoShopDto
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.hinovateste.data.repository.AutoShopRepository

class AutoShopViewModel(private val repo: AutoShopRepository): ViewModel() {

    private val _autoShops = MutableStateFlow<List<AutoShopDto>>(emptyList())

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    var showOnlyActive = MutableStateFlow(false)

    fun loadOficinas() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val codigoAssociacao = 601
                val resp = repo.fetchAutoShops(codigoAssociacao)
                _autoShops.value = resp.ListaOficinas
            } catch (e: Exception) {
                android.util.Log.e("API_ERROR", "${e.message}", e)
                _autoShops.value = emptyList()
            } finally {
                _loading.value = false
            }
        }
    }

    fun filteredList(): List<AutoShopDto> {
        val list = _autoShops.value
        return if (showOnlyActive.value) list.filter { it.Ativo == true } else list
    }
}