package com.example.android.stockshow.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.android.stockshow.data.response.CompanyProfile

@Database(entities = [CompanyProfile::class], version = 1)
abstract class CompanyDatabase : RoomDatabase() {

    abstract fun companyDao(): CompanyDao

    companion object {
        @Volatile
        private var INSTANCE: CompanyDatabase? = null

        fun getDatabase(context: Context): CompanyDatabase {

            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CompanyDatabase::class.java,
                    "company_database"
                ).fallbackToDestructiveMigration().build()

                INSTANCE = instance
                instance
            }
        }
    }
}