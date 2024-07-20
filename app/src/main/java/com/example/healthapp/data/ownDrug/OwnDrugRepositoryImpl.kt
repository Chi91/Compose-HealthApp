package com.example.healthapp.data.ownDrug

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull

class OwnDrugRepositoryImpl(private val ownDrugDao: OwnDrugDao): OwnDrugRepository {

    override fun getOwnDrug(pzn: String): Flow<OwnDrug> = ownDrugDao.getOwnDrug(pzn= pzn).filterNotNull()

    override fun getOwnDrugWithoutNotification(pzn: String): OwnDrug? = ownDrugDao.getOwnDrugWithoutNotification(pzn= pzn)

    override suspend fun insertDrug(ownDrug: OwnDrug) = ownDrugDao.insert(ownDrug= ownDrug)

    override suspend fun deleteDrug(ownDrug: OwnDrug) = ownDrugDao.delete(ownDrug= ownDrug)

    override suspend fun update(ownDrug: OwnDrug) = ownDrugDao.update(ownDrug= ownDrug)

    override fun getAllOwnDrugs(): Flow<List<OwnDrug>> = ownDrugDao.getAllOwnDrugs()

    override  fun getAllMorningOwnDrugs(): Flow<List<OwnDrug>> = ownDrugDao.getAllMorningOwnDrugs()

    override  fun getAllNoonOwnDrugs(): Flow<List<OwnDrug>> = ownDrugDao.getAllNoonOwnDrugs()

    override  fun getAllEveningOwnDrugs(): Flow<List<OwnDrug>> = ownDrugDao.getAllEveningOwnDrugs()

    override  fun getAllNightOwnDrugs(): Flow<List<OwnDrug>> = ownDrugDao.getAllNightOwnDrugs()
}