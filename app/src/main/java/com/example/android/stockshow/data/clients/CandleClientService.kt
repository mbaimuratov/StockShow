package com.example.android.stockshow.data.clients

import com.example.android.stockshow.data.services.CandleService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object CandleClientService {

    private const val FINNHUB_BASE_URL = "https://finnhub.io/api/v1/"

    //wait
    private const val API_KEY = "c13qlcf48v6ue6hfjdp0"

    var candleApiClient: CandleClientService? = null

    fun getClient(): CandleClientService? {
        if (candleApiClient == null) {
            candleApiClient = CandleClientService
        }
        return candleApiClient
    }

    private val requestInterceptor = Interceptor { chain ->
        val url = chain.request()
            .url()
            .newBuilder()
            .addQueryParameter("token", API_KEY)
            .build()
        val request = chain.request()
            .newBuilder()
            .url(url)
            .build()

        return@Interceptor chain.proceed(request)
    }

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(requestInterceptor)
        .build()

    val retrofitQuoteInstance: CandleService
        get() {
            val retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(FINNHUB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
            return retrofit.create(CandleService::class.java)
        }
}