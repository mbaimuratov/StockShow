package com.example.android.stockshow.data.response

import com.example.android.stockshow.data.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object QuoteClientInstance {

    var quoteApiClient: CompaniesClientInstance? = null

    fun getClient(): CompaniesClientInstance? {
        if (quoteApiClient == null) {
            quoteApiClient = CompaniesClientInstance
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
                .build()
            return retrofit.create(QuoteService::class.java)
        }
}