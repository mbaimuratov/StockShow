package com.example.android.stockshow.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val GITHUB_BASE_URL = "https://raw.githubusercontent.com/"
const val IEX_BASE_URL = "https://cloud.iexapis.com/stable"

const val API_KEY = "pk_170993fc888d4ef787e2272779b79f6e"

object CompaniesClientInstance {

    var companiesApiClient: CompaniesClientInstance? = null

    fun getClient(): CompaniesClientInstance? {
        if (companiesApiClient == null) {
            companiesApiClient = CompaniesClientInstance
        }
        return companiesApiClient
    }

    val retrofitCompaniesInstance: CompaniesService
        get() {
            val retrofit = Retrofit.Builder()
                .baseUrl(GITHUB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
             return retrofit.create(CompaniesService::class.java)
        }
}