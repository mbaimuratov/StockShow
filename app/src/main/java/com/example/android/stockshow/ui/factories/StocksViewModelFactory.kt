package com.example.android.stockshow.ui.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.stockshow.data.CompanyRepository
import com.example.android.stockshow.ui.models.StocksViewModel

class StocksViewModelFactory(
    private val repository: CompanyRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StocksViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StocksViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}