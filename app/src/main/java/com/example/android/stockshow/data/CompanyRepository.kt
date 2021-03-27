package com.example.android.stockshow.data

import com.example.android.stockshow.data.db.CompanyDao
import com.example.android.stockshow.data.response.CompanyProfile
import java.io.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CompanyRepository(private val companyDao: CompanyDao) {

    var companyList: List<CompanyProfile>? = null

    init {
        getCompanies()
    }

    fun insert(company: CompanyProfile) {
        runOnBackground {
            it.submit { companyDao.insert(company) }
        }
    }

    private fun getCompanies() {
        runOnBackground {
            it.submit { companyList = companyDao.getCompanies() }
        }
    }

}

private fun runOnBackground(submit: (ExecutorService) -> Unit) {
    val executor = Executors.newCachedThreadPool()
    try {
        submit.invoke(executor)
    } catch (e: IOException) {
        e.printStackTrace()
    } finally {
        executor.shutdown()
    }
}