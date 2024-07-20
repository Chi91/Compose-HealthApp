package com.example.healthapp.data.ownDrug

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


@Dao
interface OwnDrugDao {
    @Insert
    suspend fun insert(ownDrug: OwnDrug)

    @Update
    suspend fun update(ownDrug: OwnDrug)

    @Delete
    suspend fun delete(ownDrug: OwnDrug)

    @Query("SELECT * FROM ownDrugs WHERE pzn = :pzn")
    fun getOwnDrug(pzn: String): Flow<OwnDrug?>

    @Query("SELECT * FROM ownDrugs WHERE pzn = :pzn")
    fun getOwnDrugWithoutNotification(pzn: String): OwnDrug?

    @Query("SELECT * FROM ownDrugs")
    fun getAllOwnDrugs(): Flow<List<OwnDrug>>

    @Query("SELECT * FROM ownDrugs WHERE morning IS NOT NULL AND morning != '' ")
    fun getAllMorningOwnDrugs(): Flow<List<OwnDrug>>

    @Query("SELECT * FROM ownDrugs WHERE noon IS NOT NULL AND noon != '' ")
    fun getAllNoonOwnDrugs(): Flow<List<OwnDrug>>

    @Query("SELECT * FROM ownDrugs WHERE evening IS NOT NULL AND evening!= '' ")
    fun getAllEveningOwnDrugs(): Flow<List<OwnDrug>>

    @Query("SELECT * FROM ownDrugs WHERE night IS NOT NULL AND night != '' ")
    fun getAllNightOwnDrugs(): Flow<List<OwnDrug>>
}