package com.example.android.stockshow.data

import com.example.android.stockshow.data.response.Quote
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface QuoteService {
    @GET("stock/{symbol}/quote")
    fun getQuote(@Path("symbol") ticker: String): Observable<Quote>
}