package com.example.android.stockshow

import android.app.Application

import com.example.android.stockshow.data.db.CompanyDatabase
import com.example.android.stockshow.data.repos.CompanyRepository

class App : Application() {
    private val database by lazy { CompanyDatabase.getDatabase(this) }
    val repository by lazy { CompanyRepository(database.companyDao()) }
}