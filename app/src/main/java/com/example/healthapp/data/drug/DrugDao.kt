package com.example.healthapp.data.drug

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.healthapp.data.drug.Drug
import kotlinx.coroutines.flow.Flow

@Dao
interface DrugDao {
    @Insert
    suspend fun insert(drug: Drug)

    @Update
    suspend fun update(drug: Drug)

    @Delete
    suspend fun delete(drug: Drug)

    @Query("SELECT * FROM drugs WHERE pzn = :pzn")
    fun getDrug(pzn: String): Flow<Drug>

    @Query("SELECT * FROM drugs")
    fun getAllDrugs(): Flow<List<Drug>>
}