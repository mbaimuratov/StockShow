package com.example.android.stockshow.ui.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.stockshow.data.response.StockItem
import java.util.*

class SharedViewModel : ViewModel() {

    private val _stockMap = MutableLiveData<TreeMap<String, StockItem>>()
    var stockMap: LiveData<TreeMap<String, StockItem>> = _stockMap

    private val _searchMap = MutableLiveData<TreeMap<String, StockItem>>()
    val searchMap = _searchMap as LiveData<TreeMap<String, StockItem>>

    fun setStockMap(map: TreeMap<String, StockItem>) {
        _stockMap.value = map
    }

    fun setSearchMap(map: TreeMap<String, StockItem>) {
        _searchMap.value = map
    }

    fun addOrRemoveFavourite(stockItem: StockItem) {
        val map = TreeMap(stockMap.value)
        map[stockItem.ticker]?.isFavourite = !map[stockItem.ticker]?.isFavourite!!
        setStockMap(map)
    }

}