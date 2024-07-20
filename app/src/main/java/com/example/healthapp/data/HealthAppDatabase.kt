package com.example.healthapp.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.healthapp.data.drug.Drug
import com.example.healthapp.data.drug.DrugDao
import com.example.healthapp.data.ownDrug.OwnDrug
import com.example.healthapp.data.ownDrug.OwnDrugDao
import java.io.File
import java.io.FileOutputStream

@Database(entities = [Drug::class, OwnDrug::class], version = 13, exportSchema = false)
abstract class HealthAppDatabase : RoomDatabase() {

    abstract fun drugDao(): DrugDao
    abstract fun ownDrugDao(): OwnDrugDao

    companion object {
        @Volatile
        private var Instance: HealthAppDatabase? = null

        fun getDatabase(context: Context): HealthAppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, HealthAppDatabase::class.java, "health_app_database")
                    .createFromAsset("database/drug.db")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}