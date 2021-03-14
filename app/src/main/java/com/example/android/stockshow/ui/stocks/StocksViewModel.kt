package com.example.android.stockshow.ui.stocks

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.stockshow.data.CompaniesClientInstance
import com.example.android.stockshow.data.QuoteClientInstance
import com.example.android.stockshow.data.response.StockItem
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class StocksViewModel : ViewModel() {

    private val companiesClient = CompaniesClientInstance.getClient()
    private val quoteClient = QuoteClientInstance.getClient()

    private val disposeBag: CompositeDisposable = CompositeDisposable()

    private var _stockItems: MutableLiveData<List<StockItem>> = MutableLiveData<List<StockItem>>()
    val stockItems: LiveData<List<StockItem>>
        get() = _stockItems

    fun fetchStockItems() {
        val disposable = companiesClient?.retrofitCompaniesInstance?.getCompanies()
            ?.takeUntil(Observable.timer(10, TimeUnit.SECONDS))
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(
                { companies ->
                    val stockItemList = arrayListOf<StockItem>()
                    for (company in companies) {
                        Observable.interval(0, 10, TimeUnit.MILLISECONDS)
                            .flatMap {
                                quoteClient?.retrofitQuoteInstance?.getQuote(company.ticker)
                            }
                            ?.subscribeOn(Schedulers.io())
                            ?.observeOn(AndroidSchedulers.mainThread())
                            ?.subscribe({ result ->
                                val stockItem = StockItem(
                                    company.name,
                                    company.logo,
                                    company.ticker,
                                    result.latestPrice,
                                    result.previousClose
                                )
                                Log.i("fetchStockItems", result.toString())
                                stockItemList.add(stockItem)
                            },
                                { throwable -> Log.i("fetchStockItems", throwable.message.toString()) }
                            )
                    }

                    _stockItems.value = stockItemList
                },
                { throwable -> Log.i("fetchStockItems", throwable.message.toString()) }
            )

        disposeBag.add(disposable!!)
    }

    override fun onCleared() {
        super.onCleared()
        disposeBag.clear()
    }
}