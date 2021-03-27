package com.example.android.stockshow.data.services

import com.example.android.stockshow.data.response.CompanyProfile
import io.reactivex.Observable
import retrofit2.http.GET

interface CompaniesService {
    @GET("mbaimuratov/stockProfiles/main/stockProfiles.json")
    fun getCompanies(): Observable<List<CompanyProfile>>
}