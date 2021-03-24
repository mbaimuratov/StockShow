package com.example.android.stockshow.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.android.stockshow.data.response.CompanyProfile

@Dao
interface CompanyDao {
    @Query("SELECT * FROM company_table")
    fun getCompanies(): List<CompanyProfile>

    @Insert
    fun insert(companyProfile: CompanyProfile)
}
