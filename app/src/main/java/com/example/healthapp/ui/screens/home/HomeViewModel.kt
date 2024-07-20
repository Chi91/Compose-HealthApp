package com.example.healthapp.ui.screens.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.healthapp.HealthAppApplication
import com.example.healthapp.data.ownDrug.OwnDrug
import com.example.healthapp.data.ownDrug.OwnDrugRepository
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


data class UiStateHome(
    var showDialog: Boolean = false,
    var screenNotEmpty: Boolean = false,
    var drugEmpty: Boolean = false
)

class HomeViewModel(private val ownDrugRepository: OwnDrugRepository) : ViewModel() {

    val morningDrugs = MutableStateFlow<List<OwnDrug>>(emptyList())

    val noonDrugs = MutableStateFlow<List<OwnDrug>>(emptyList())

    val eveningDrugs = MutableStateFlow<List<OwnDrug>>(emptyList())

    val nightDrugs = MutableStateFlow<List<OwnDrug>>(emptyList())

    val allDrugs = MutableStateFlow<List<OwnDrug>>(emptyList())

    var uiStateHome by mutableStateOf(UiStateHome())
        private set

    init {
        viewModelScope.launch {
            try {
                coroutineScope {
                    launch { getAllDrugs() }
                    launch { getMorningDrugs() }
                    launch { getNoonDrugs() }
                    launch { getEveningDrugs() }
                    launch { getNightDrugs() }
                }
            } catch (e: Exception) {
                Log.e("ViewModel", "Fehler bei der Init von HomeViewModel", e)
            }
        }
    }

    private suspend fun getAllDrugs() {
        try {
            ownDrugRepository.getAllOwnDrugs().collect() {
                allDrugs.value = it
            }
        } catch (e: Exception) {
            Log.e("Error", "Fehler in getAllDrugs", e)
        }
    }

    private suspend fun getMorningDrugs() {

        try {
            ownDrugRepository.getAllMorningOwnDrugs().collect {
                morningDrugs.value = it
            }
        } catch (e: Exception) {
            Log.e("Error", "Fehler in getMorningDrugs", e)
        }
    }

    private suspend fun getNoonDrugs() {
        try {
            ownDrugRepository.getAllNoonOwnDrugs().collect {
                noonDrugs.value = it
            }
        } catch (e: Exception) {
            Log.e("Error", "Fehler in getNoonDrugs", e)
        }
    }

    private suspend fun getEveningDrugs() {
        try {
            ownDrugRepository.getAllEveningOwnDrugs().collect {
                eveningDrugs.value = it
            }
        } catch (e: Exception) {
            Log.e("Error", "Fehler in getEveningDrugs", e)
        }
    }

    private suspend fun getNightDrugs() {
        try {
            ownDrugRepository.getAllNightOwnDrugs().collect {
                nightDrugs.value = it
            }
        } catch (e: Exception) {
            Log.e("Error", "Fehler in getNightDrugs", e)
        }
    }

    fun showDialog() {
        uiStateHome = uiStateHome.copy(showDialog = true)
    }

    fun dismissDialog() {
        uiStateHome = uiStateHome.copy(showDialog = false)
    }

    fun setCheckBox(time: String, ownDrug: OwnDrug, valueChecked: Boolean) {
        when (time) {
            "Morgens: 6:00 Uhr" -> {
                ownDrug.checkedMorning = valueChecked
                handleCheckBox(
                    ownDrug = ownDrug,
                    valueChecked = valueChecked,
                    amountTime = ownDrug.morning.toInt()
                )
            }

            "Mittags: 12:00 Uhr" -> {
                ownDrug.checkedNoon = valueChecked
                handleCheckBox(
                    ownDrug = ownDrug,
                    valueChecked = valueChecked,
                    amountTime = ownDrug.noon.toInt()
                )
            }

            "Abends: 18:00 Uhr" -> {
                ownDrug.checkedEvening = valueChecked
                handleCheckBox(
                    ownDrug = ownDrug,
                    valueChecked = valueChecked,
                    amountTime = ownDrug.evening.toInt()
                )
            }

            "Nachts: 22:00 Uhr" -> {
                ownDrug.checkedNight = valueChecked
                handleCheckBox(
                    ownDrug = ownDrug,
                    valueChecked = valueChecked,
                    amountTime = ownDrug.night.toInt()
                )
            }
        }
    }

    fun getCheckBoxValue(time: String, ownDrug: OwnDrug): Boolean {
        return when (time) {
            "Morgens: 6:00 Uhr" -> ownDrug.checkedMorning
            "Mittags: 12:00 Uhr" -> ownDrug.checkedNoon
            "Abends: 18:00 Uhr" -> ownDrug.checkedEvening
            "Nachts: 22:00 Uhr" -> ownDrug.checkedNight
            else -> false
        }
    }

    fun handleCheckBox(ownDrug: OwnDrug, valueChecked: Boolean, amountTime: Int) {
        if (ownDrug.amount > 8 && valueChecked) {
            ownDrug.amount = ownDrug.amount - amountTime
        } else if (!valueChecked) {
            ownDrug.amount = ownDrug.amount + amountTime
        } else {
            uiStateHome = uiStateHome.copy(drugEmpty = true)
            if (ownDrug.amount > 0 && (ownDrug.amount - amountTime) > 0) {
                ownDrug.amount = ownDrug.amount - amountTime
            }
        }
        try {
            viewModelScope.launch {
                ownDrugRepository.update(ownDrug = ownDrug)
            }
        } catch (e: Exception) {
            Log.d("Teste", "HomeviewModel setCheckbox im Exception $e")
        }
    }

    fun updateHomeState(updatedUiState: UiStateHome) {
        uiStateHome = updatedUiState
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as HealthAppApplication)
                val ownDrugRepository = app.container.ownDrugRepository
                HomeViewModel(ownDrugRepository = ownDrugRepository)
            }
        }
    }
}