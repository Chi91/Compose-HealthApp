package com.example.healthapp.data.ownDrug

import kotlinx.coroutines.flow.Flow

interface OwnDrugRepository {

    fun getOwnDrug(pzn: String): Flow<OwnDrug>

    fun getOwnDrugWithoutNotification(pzn: String): OwnDrug?

    suspend fun insertDrug(ownDrug: OwnDrug)

    suspend fun deleteDrug(ownDrug: OwnDrug)

    suspend fun update(ownDrug: OwnDrug)

    fun getAllOwnDrugs(): Flow<List<OwnDrug>>

    fun getAllMorningOwnDrugs(): Flow<List<OwnDrug>>

    fun getAllNoonOwnDrugs(): Flow<List<OwnDrug>>

    fun getAllEveningOwnDrugs(): Flow<List<OwnDrug>>

    fun getAllNightOwnDrugs(): Flow<List<OwnDrug>>
}