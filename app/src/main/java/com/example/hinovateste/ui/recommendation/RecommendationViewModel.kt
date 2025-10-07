package com.example.hinovateste.ui.recommendation

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.hinovateste.data.remote.RecommendationEntry
import com.example.hinovateste.data.remote.Recommendation
import com.example.hinovateste.data.repository.AutoShopRepository
import kotlinx.coroutines.launch
import com.example.hinovateste.R

class RecommendationViewModel(
    private val repo: AutoShopRepository,
    application: Application
) : AndroidViewModel(application) {

    private val context: Context
        get() = getApplication<Application>().applicationContext

    var uiState = mutableStateOf(RecommendationUiState())

    fun sendRecommendation() {
        val s = uiState.value
        viewModelScope.launch {
            try {
                val entrada = RecommendationEntry(
                    Indicacao = Recommendation(
                        CodigoAssociacao = 601,
                        DataCriacao = null,
                        CpfAssociado = s.cpfAssociado,
                        EmailAssociado = s.emailAssociado,
                        NomeAssociado = s.nomeAssociado,
                        TelefoneAssociado = s.telefoneAssociado,
                        PlacaVeiculoAssociado = s.placa,
                        NomeAmigo = s.nomeAmigo,
                        TelefoneAmigo = s.telefoneAmigo,
                        EmailAmigo = s.emailAmigo,
                        Observacao = s.observacao
                    ),
                    Remetente = s.remetente ?: "",
                    Copias = s.copias ?: emptyList()
                )

                val result = repo.sendRecommendation(entrada)

                if (!result.Sucesso.isNullOrBlank() &&
                    result.Sucesso.contains("sucesso", ignoreCase = true)) {

                    uiState.value = RecommendationUiState(
                        success = context.getString(R.string.success_sending_recommendation),
                        error = null
                    )

                } else {
                    uiState.value = s.copy(
                        success = null,
                        error = context.getString(
                            R.string.error_sending_recommendation,
                            result.RetornoErro ?: context.getString(R.string.error_unknown)
                        )
                    )
                }

            } catch (e: Exception) {
                uiState.value = s.copy(
                    success = null,
                    error = context.getString(R.string.error_generic, e.message ?: context.getString(R.string.error_unknown))
                )
            }
        }
    }
}

data class RecommendationUiState(
    val nomeAssociado: String? = null,
    val cpfAssociado: String? = null,
    val emailAssociado: String? = null,
    val telefoneAssociado: String? = null,
    val placa: String? = null,
    val nomeAmigo: String? = null,
    val telefoneAmigo: String? = null,
    val emailAmigo: String? = null,
    val observacao: String? = null,
    val remetente: String? = null,
    val copias: List<String>? = null,
    val success: String? = null,
    val error: String? = null
)

class RecommendationViewModelFactory(
    private val repository: AutoShopRepository,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecommendationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RecommendationViewModel(repository, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}