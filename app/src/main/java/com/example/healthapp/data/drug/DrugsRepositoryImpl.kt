package com.example.healthapp.data.drug

import kotlinx.coroutines.flow.Flow

class DrugsRepositoryImpl(private val drugDao: DrugDao) : DrugsRepository {

    override fun getAllDrugs(): Flow<List<Drug?>> = drugDao.getAllDrugs()

    override fun getDrug(pzn: String): Flow<Drug> = drugDao.getDrug(pzn = pzn)

    override suspend fun insertDrug(drug: Drug) = drugDao.insert(drug = drug)

    override suspend fun deleteDrug(drug: Drug) = drugDao.delete(drug = drug)

    override suspend fun update(drug: Drug) = drugDao.update(drug = drug)
}