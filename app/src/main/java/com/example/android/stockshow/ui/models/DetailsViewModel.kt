package com.example.android.stockshow.ui.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.stockshow.data.clients.CandleClientService
import com.example.android.stockshow.data.response.Candle
import com.example.android.stockshow.data.response.StockItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class DetailsViewModel : ViewModel() {

    private val candleClient = CandleClientService.getClient()
    private val disposeBag: CompositeDisposable = CompositeDisposable()

    private val _candle = MutableLiveData<Candle>()
    var candle: LiveData<Candle> = _candle

    fun fetchCandles(stockItem: StockItem, resolution: String, from: Long) {
        val candle = candleClient?.retrofitQuoteInstance?.getCandles(
            stockItem.ticker,
            resolution,
            from,
            System.currentTimeMillis() / 1000
        )
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({
                _candle.value = it
            },
                { throwable ->
                    Log.i(Log.getStackTraceString(throwable), throwable.message.toString())
                }
            )

        disposeBag.add(candle!!)
    }
}