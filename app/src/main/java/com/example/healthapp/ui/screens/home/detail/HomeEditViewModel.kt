package com.example.healthapp.ui.screens.home.detail

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
import com.example.healthapp.data.ownDrug.OwnDrug
import com.example.healthapp.data.ownDrug.OwnDrugRepository
import com.example.healthapp.ui.screens.home.detail.add.DrugDetail
import kotlinx.coroutines.launch


data class UiStateEdit(
    var drugDetail: DrugDetail = DrugDetail(),
    var hasEntry: Boolean = false,
    var isErrorMorning: Boolean = false,
    var isErrorNoon: Boolean = false,
    var isErrorEvening: Boolean = false,
    var isErrorNight: Boolean = false,
)

class HomeEditViewModel(
    private val ownDrugRepository: OwnDrugRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var uiStateEdit by mutableStateOf(UiStateEdit())
        private set

    init {
        val pzn: String = checkNotNull(savedStateHandle["pzn"])
        getCurrentDrug(pzn = pzn)
        uiStateEdit = uiStateEdit.copy(hasEntry = true)
    }

    private fun validInput(input: String): Boolean {
        if (input.contains("-") || input.startsWith(".") || input.count { it == '.' } > 1 || input.count { it == ',' } > 1 || input.count { it == '.' } > 0 && input.count { it == ',' } > 0 || input.startsWith(
                ","
            )) {
            return false
        }
        return true
    }

    fun updateDrugDetail(drugDetail: DrugDetail) {
        val morningValid = validInput(drugDetail.morning)
        val noonValid = validInput(drugDetail.noon)
        val eveningValid = validInput(drugDetail.evening)
        val nightValid = validInput(drugDetail.night)

        val updateUiState = uiStateEdit.copy(
            drugDetail = drugDetail,
            isErrorMorning = !morningValid,
            isErrorNoon = !noonValid,
            isErrorEvening = !eveningValid,
            isErrorNight = !nightValid
        )
        this.uiStateEdit = updateUiState

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
        val drugDetail = uiStateEdit.drugDetail
        val hasEntry =
            ((drugDetail.night.isNotEmpty() && night) || (drugDetail.morning.isNotEmpty() && morning) || (drugDetail.evening.isNotEmpty() && evening) || (drugDetail.noon.isNotEmpty() && noon) )
        uiStateEdit = uiStateEdit.copy(hasEntry = hasEntry)
    }

    suspend fun updateDrug(drugDetail: DrugDetail, navigateHome: () -> Unit) {
        viewModelScope.launch {
            try {
                ownDrugRepository.update(drugDetail.toOwnDrug())
            } catch (e: Exception) {
                Log.d("Error", "Konnte kein AM in die DB hinzufügen")
            } finally {
                navigateHome()
            }
        }
    }

    fun getImage(): Int {
        when (uiStateEdit.drugDetail.pzn) {
            "02013219" -> return R.drawable.ibu
            "02588457" -> return R.drawable.theo_1
            "09064651" -> return R.drawable.ass_2
            "13896191" -> return R.drawable.atorvastatin_2
            "13817429" -> return R.drawable.losartan_2
            "02817656" -> return R.drawable.mirtalich
        }
        return R.drawable.placeholder_view_vector
    }

    private fun getCurrentDrug(pzn: String) {
        viewModelScope.launch {
            ownDrugRepository.getOwnDrug(pzn).collect() {
                uiStateEdit = uiStateEdit.copy(
                    drugDetail = DrugDetail(
                        pzn = it.pzn,
                        activeIngredient = it.activeIngredient,
                        potency = it.potency,
                        name = it.name,
                        indication = it.indication,
                        description = it.description,
                        divisible = it.divisible,
                        dosageInstruction = it.dosageInstruction,
                        notes = it.notes,
                        morning = it.morning,
                        noon = it.noon,
                        evening = it.evening,
                        night = it.night,
                        amount = it.amount,
                        checkedEvening = it.checkedEvening,
                        checkedMorning = it.checkedMorning,
                        checkedNight = it.checkedNight,
                        checkedNoon = it.checkedNoon
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
            amount= amount,
        )
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as HealthAppApplication)
                val ownDrugRepository = app.container.ownDrugRepository
                HomeEditViewModel(
                    ownDrugRepository = ownDrugRepository,
                    savedStateHandle = this.createSavedStateHandle()
                )
            }
        }
    }
}