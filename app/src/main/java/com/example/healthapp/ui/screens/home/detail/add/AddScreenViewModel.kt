package com.example.healthapp.ui.screens.home.detail.add

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.healthapp.HealthAppApplication
import com.example.healthapp.R
import com.example.healthapp.data.drug.DrugsRepository
import com.example.healthapp.data.ownDrug.OwnDrug
import com.example.healthapp.data.ownDrug.OwnDrugRepository
import kotlinx.coroutines.launch


data class UiStateAdding(
    var drugDetail: DrugDetail = DrugDetail(),
    var hasEntry: Boolean = false,
    var isErrorMorning: Boolean = false,
    var isErrorNoon: Boolean = false,
    var isErrorEvening: Boolean = false,
    var isErrorNight: Boolean = false,
    var drugExist: Boolean = false
)

data class DrugDetail(
    var pzn: String = "",
    var activeIngredient: String = "",
    var potency: String = "",
    var name: String = "",
    var indication: String = "",
    var description: String = "",
    var divisible: String = "",
    var dosageInstruction: String = "",
    var notes: String = "",
    var morning: String = "",
    var noon: String = "",
    var evening: String = "",
    var night: String = "",
    var checkedMorning: Boolean = false,
    var checkedNoon: Boolean = false,
    var checkedEvening: Boolean = false,
    var checkedNight: Boolean = false,
    var amount: Int = 0
)

class AddScreenViewModel(
    private val drugsRepository: DrugsRepository,
    private val ownDrugRepository: OwnDrugRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var uiStateAdd by mutableStateOf(UiStateAdding())
        private set

    init {
        val pzn: String = checkNotNull(savedStateHandle["pzn"])
        getCurrentDrug(pzn = pzn)
    }

    private fun validInput(input: String): Boolean {
        if (input.contains("-") || input.startsWith(".") || input.count { it == '.' } > 1 || input.count { it == ',' } > 1
            || input.count { it == '.' } > 0 && input.count { it == ',' } > 0 || input.startsWith(",") || input == "0") {
            return false
        }
        return true
    }

    fun updateDrugDetail(drugDetail: DrugDetail) {
        val morningValid = validInput(drugDetail.morning)
        val noonValid = validInput(drugDetail.noon)
        val eveningValid = validInput(drugDetail.evening)
        val nightValid = validInput(drugDetail.night)

        val updateUiState = uiStateAdd.copy(
            drugDetail = drugDetail,
            isErrorMorning = !morningValid,
            isErrorNoon = !noonValid,
            isErrorEvening = !eveningValid,
            isErrorNight = !nightValid
        )
        this.uiStateAdd = updateUiState

        minimumOneTextField(
            morning = morningValid,
            noon = noonValid,
            evening = eveningValid,
            night = nightValid
        )
    }

    private fun minimumOneTextField(
        morning: Boolean,
        noon: Boolean,
        evening: Boolean,
        night: Boolean
    ) {
        val drugDetail = uiStateAdd.drugDetail
        val hasEntry =
            ((drugDetail.night.isNotEmpty() && night) || (drugDetail.morning.isNotEmpty() && morning) || (drugDetail.evening.isNotEmpty() && evening) || (drugDetail.noon.isNotEmpty() && noon))
        uiStateAdd = uiStateAdd.copy(hasEntry = hasEntry)
    }

    suspend fun saveDrug(drugDetail: DrugDetail, navigateHome: () -> Unit) {
            viewModelScope.launch {
                try {
                    ownDrugRepository.insertDrug(drugDetail.toOwnDrug())
                } catch (e: Exception) {
                    Log.d("Error", "Konnte kein AM in die DB gespeichert werden")
                } finally {
                    navigateHome()
                }
            }
    }

    fun updateUiState(uiStateAdding: UiStateAdding){
        uiStateAdd = uiStateAdding
    }

    fun getImage(): Int {
        when (uiStateAdd.drugDetail.pzn) {
            "02013219" -> return R.drawable.ibu_400_akut_verpackung
            "02588457" -> return R.drawable.theophyllin
            "09064651" -> return R.drawable.dexcel_ass_dexcel
            "13896191" -> return R.drawable.atorvastatin
            "13817429" -> return R.drawable.losartan
            "02817656" -> return R.drawable.mirta
        }
        return R.drawable.placeholder_view_vector
    }

    fun getCurrentDrug(pzn: String) {
        viewModelScope.launch {
            drugsRepository.getDrug(pzn).collect() {
                uiStateAdd = uiStateAdd.copy(
                    drugDetail = DrugDetail(
                        pzn = it.pzn,
                        activeIngredient = it.activeIngredient,
                        potency = it.potency,
                        name = it.name,
                        description = it.description,
                        divisible = it.isDivisible,
                        indication = it.indication,
                        dosageInstruction = it.dosageInstruction,
                        amount = it.amount
                    )
                )
            }
        }
    }

    fun DrugDetail.toOwnDrug(): OwnDrug {
        return OwnDrug(
            pzn = pzn,
            activeIngredient = activeIngredient,
            potency = potency,
            name = name,
            indication = indication,
            description = description,
            divisible = divisible,
            dosageInstruction = dosageInstruction,
            notes = notes,
            morning = morning,
            noon = noon,
            evening = evening,
            night = night,
            checkedMorning = checkedMorning,
            checkedNoon = checkedNoon,
            checkedEvening = checkedEvening,
            checkedNight = checkedNight,
            amount = amount
        )
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as HealthAppApplication)
                val drugsRepository = app.container.drugsRepository
                val ownDrugRepository = app.container.ownDrugRepository
                AddScreenViewModel(
                    drugsRepository = drugsRepository,
                    ownDrugRepository = ownDrugRepository,
                    savedStateHandle = this.createSavedStateHandle()
                )
            }
        }
    }
}