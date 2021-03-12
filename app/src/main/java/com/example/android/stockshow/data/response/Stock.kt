package com.example.android.stockshow.data.response

data class Stock(
    val name: String,
    val logo: String,
    val ticker: String,
    val latestPrice: Double,
    val previousClose: Double
)