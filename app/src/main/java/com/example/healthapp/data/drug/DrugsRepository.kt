package com.example.healthapp.data.drug

import com.example.healthapp.data.drug.Drug
import kotlinx.coroutines.flow.Flow

interface DrugsRepository {

    fun getAllDrugs(): Flow<List<Drug?>>

    fun getDrug(pzn: String): Flow<Drug>

    suspend fun insertDrug(drug: Drug)

    suspend fun deleteDrug(drug: Drug)

    suspend fun update(drug: Drug)
}