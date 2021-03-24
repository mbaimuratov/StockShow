package com.example.android.stockshow.data.clients

import com.example.android.stockshow.data.services.CompaniesService
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

const val GITHUB_BASE_URL = "https://raw.githubusercontent.com/"
const val IEX_BASE_URL = "https://cloud.iexapis.com/stable/"

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
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
             return retrofit.create(CompaniesService::class.java)
        }
}