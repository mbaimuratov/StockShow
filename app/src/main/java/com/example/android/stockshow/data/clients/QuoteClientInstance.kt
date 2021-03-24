package com.example.android.stockshow.data.clients

import com.example.android.stockshow.data.*
import com.example.android.stockshow.data.services.QuoteService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object QuoteClientInstance {

    private const val API_KEY = "pk_ad3e6abcc13f404989b8f48aa19a310e"

    var quoteApiClient: QuoteClientInstance? = null

    fun getClient(): QuoteClientInstance? {
        if (quoteApiClient == null) {
            quoteApiClient = QuoteClientInstance
        }
        return quoteApiClient
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

    val retrofitQuoteInstance: QuoteService
        get() {
            val retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(IEX_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
            return retrofit.create(QuoteService::class.java)
        }
}