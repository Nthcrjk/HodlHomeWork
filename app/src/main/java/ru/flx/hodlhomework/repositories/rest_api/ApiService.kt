package ru.flx.hodlhomework.repositories.rest_api

import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import ru.flx.hodlhomework.BuildConfig
import java.util.concurrent.TimeUnit

class ApiService {

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    val jsonApi = getRetrofitInstanceByBaseUrl(GsonConverterFactory.create()).create(JsonApi::class.java)
    val textApi = getRetrofitInstanceByBaseUrl(TextConverter.create()).create(TextApi::class.java)

    private fun getRetrofitInstanceByBaseUrl(converter: Converter.Factory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.API_URL)
            .client(httpClient)
            .addConverterFactory(converter)
            .build()
    }

    interface JsonApi {
        @GET("address/{address}/utxo")
        suspend fun getUtxoList(@Path("address") address: String) : Response<List<UtxoResponse>>

        @GET("tx/{txId}")
        @Headers("Content-Type: text/plain")
        suspend fun getTxInfo(@Path("txId") address: String): Response<TransactionResponse>

        @GET("address/{address}/txs")
        suspend fun getStarterTransactionList(@Path("address") address: String): Response<MutableList<TransactionResponse>>

        @GET("address/{address}/txs/chain/{txid}")
        suspend fun getTransactionsFromTxId(@Path("address") address: String, @Path("txid") txid: String): Response<MutableList<TransactionResponse>>
    }

    interface TextApi {
        @POST("tx")
        @Headers("Content-Type: text/plain")
        suspend fun sendTransaction(@Body hex: RequestBody): Response<String>
    }
}