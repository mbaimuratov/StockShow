package com.example.android.stockshow.ui.stocks

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.stockshow.data.CompaniesClientInstance
import com.example.android.stockshow.data.QuoteClientInstance
import com.example.android.stockshow.data.response.CompanyProfile
import com.example.android.stockshow.data.response.StockItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class StocksViewModel : ViewModel() {

    private val companiesClient = CompaniesClientInstance.getClient()
    private val quoteClient = QuoteClientInstance.getClient()
    private lateinit var companyList: List<CompanyProfile>
    private var stockList = mutableListOf<StockItem>()

    private val disposeBag: CompositeDisposable = CompositeDisposable()

    var stockItems: MutableLiveData<List<StockItem>> = MutableLiveData<List<StockItem>>()

    fun fetchStockItems() {
        val disposable = companiesClient?.retrofitCompaniesInstance?.getCompanies()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe({ companies ->
                companyList = companies
                extractQuotes()
            },
                { throwable -> Log.i("fetchStockItems", throwable.message.toString()) }
            )
        disposeBag.add(disposable!!)
    }

    private fun extractQuotes() {
        for (company in companyList) {
            val disposable = quoteClient?.retrofitQuoteInstance?.getQuote(company.ticker)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({quote ->
                    val stockItem = StockItem(
                        company.name,
                        company.logo,
                        company.ticker,
                        quote.latestPrice,
                        quote.previousClose
                    )
                    stockList.add(stockItem)
                    stockItems.value = stockList
                },
                    {throwable ->
                        Log.i(Log.getStackTraceString(throwable), throwable.message.toString())
                    })
            disposeBag.add(disposable!!)
            Thread.sleep(10)
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposeBag.clear()
    }
}