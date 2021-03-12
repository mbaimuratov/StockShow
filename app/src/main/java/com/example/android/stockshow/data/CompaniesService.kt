package com.example.android.stockshow.data

import com.example.android.stockshow.data.response.CompanyProfile
import io.reactivex.Observable
import retrofit2.http.GET

interface CompaniesService {
    @GET("justadlet/testing-API/master/api/stockProfiles.json")
    fun getCompany(): Observable<CompanyProfile>
}