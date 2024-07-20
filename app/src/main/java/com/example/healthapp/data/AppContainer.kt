package com.example.healthapp.data

import android.content.Context
import android.util.Log
import com.example.healthapp.data.drug.DrugsRepository
import com.example.healthapp.data.drug.DrugsRepositoryImpl
import com.example.healthapp.data.ownDrug.OwnDrugRepositoryImpl
import com.example.healthapp.data.ownDrug.OwnDrugRepository

interface AppContainer {
    val drugsRepository: DrugsRepository
    val ownDrugRepository: OwnDrugRepository
}

class DataContainer(private val context: Context) : AppContainer{
    private val healthAppDatabase: HealthAppDatabase = HealthAppDatabase.getDatabase(context)

    override val drugsRepository: DrugsRepository by lazy {
        DrugsRepositoryImpl(healthAppDatabase.drugDao())
    }
    override val ownDrugRepository: OwnDrugRepository by lazy {
        OwnDrugRepositoryImpl(healthAppDatabase.ownDrugDao())
    }
    init {
        Log.d("DataContainer", "DataContainer initialized")
    }
}