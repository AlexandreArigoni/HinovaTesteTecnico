package com.example.hinovateste.data.remote

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Body
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.logging.HttpLoggingInterceptor

data class AutoShopResponse(
    val ListaOficinas: List<AutoShopDto>,
    val RetornoErro: Any?,
    val Token: String?
)

@Parcelize
data class AutoShopDto(
    val Id: Int,
    val Nome: String,
    val Descricao: String?,
    val DescricaoCurta: String?,
    val Endereco: String?,
    val Latitude: String?,
    val Longitude: String?,
    val Foto: String?, // base64 or token
    val AvaliacaoUsuario: Int?,
    val CodigoAssociacao: Int?,
    val Email: String?,
    val Telefone1: String?,
    val Telefone2: String?,
    val Ativo: Boolean?
) : Parcelable

data class RecommendationEntry(
    val Indicacao: Recommendation,
    val Remetente: String,
    val Copias: List<String> = emptyList()
)

data class Recommendation(
    val CodigoAssociacao: Int,
    val DataCriacao: String?,
    val CpfAssociado: String?,
    val EmailAssociado: String?,
    val NomeAssociado: String?,
    val TelefoneAssociado: String?,
    val PlacaVeiculoAssociado: String?,
    val NomeAmigo: String?,
    val TelefoneAmigo: String?,
    val EmailAmigo: String?,
    val Observacao: String?
)

data class RecommendationReturn(
    val RetornoErro: Any?,
    val Sucesso: String?
)

interface ApiService {
    @GET("api/Oficina")
    suspend fun getAutoShops(
        @Query("codigoAssociacao") codigoAssociacao: Int,
        @Query("cpfAssociado") cpfAssociado: String = ""
    ): AutoShopResponse

    @POST("Api/Indicacao")
    suspend fun sendIndication(@Body entry: RecommendationEntry): RecommendationReturn

    companion object {
        fun create(): ApiService {
            val logging = HttpLoggingInterceptor { message ->
                android.util.Log.d("API_LOG", message)
            }.apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val okHttpClient = okhttp3.OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://dev.hinovamobile.com.br/ProvaConhecimentoWebApi/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ApiService::class.java)
        }
    }
}