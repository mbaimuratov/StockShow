package com.example.android.stockshow.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.android.stockshow.data.response.CompanyProfile

@Dao
interface CompanyDao {
    @Query("SELECT * FROM company_table")
    fun getCompanies(): LiveData<List<CompanyProfile>>

    @Insert
    fun insert(companyProfile: CompanyProfile)
}
