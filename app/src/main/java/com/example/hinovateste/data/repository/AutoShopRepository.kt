package com.example.hinovateste.data.repository

import com.example.hinovateste.data.remote.ApiService
import com.example.hinovateste.data.remote.RecommendationEntry


class AutoShopRepository(private val api: ApiService) {
    suspend fun fetchAutoShops(
        codigoAssociacao: Int = 601,
        cpfAssociado: String = ""
    ) = api.getAutoShops(codigoAssociacao, cpfAssociado)

    suspend fun sendRecommendation(entry: RecommendationEntry) =
        api.sendIndication(entry)
}