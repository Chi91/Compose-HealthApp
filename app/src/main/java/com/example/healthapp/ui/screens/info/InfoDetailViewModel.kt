package com.example.healthapp.ui.screens.info

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
import com.example.healthapp.data.drug.Drug
import com.example.healthapp.data.drug.DrugsRepository
import kotlinx.coroutines.launch

data class UiStateInfoDetail(
    val drug: Drug = Drug(
        pzn = "",
        activeIngredient = "",
        potency = "", name = "",
        indication = "",
        description = "",
        dosageInstruction = "",
        isDivisible = "",
        amount = 0
    )
)

class InfoDetailViewModel(
    private val drugsRepository: DrugsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var uiStateInfoDetail by mutableStateOf(UiStateInfoDetail())
        private set

    init {
        val pzn: String = checkNotNull(savedStateHandle["pzn"])
        getCurrentDrug(pzn = pzn)
    }

    fun getCurrentDrug(pzn: String) {
        viewModelScope.launch {
            drugsRepository.getDrug(pzn = pzn).collect() {
                uiStateInfoDetail = uiStateInfoDetail.copy(drug = it)
            }
        }
    }

    fun getImage(): List<Int> {
        Log.d("Teste", "Bin im getImage")
        when (uiStateInfoDetail.drug.pzn) {
            "02013219" -> return listOf(R.drawable.ibu_400_akut_verpackung, R.drawable.ibu)
            "02588457" -> return listOf(R.drawable.theophyllin, R.drawable.theo_1)
            "09064651" -> return listOf(R.drawable.dexcel_ass_dexcel, R.drawable.ass_2)
            "13896191" -> return listOf(R.drawable.atorvastatin, R.drawable.atorvastatin_2)
            "13817429" -> return listOf(R.drawable.losartan, R.drawable.losartan_2)
            "02817656" -> return listOf(R.drawable.mirta, R.drawable.mirtalich)
        }
        return listOf(R.drawable.placeholder_view_vector, R.drawable.symptome)
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as HealthAppApplication)
                val drugsRepository = app.container.drugsRepository
                InfoDetailViewModel(
                    drugsRepository = drugsRepository,
                    savedStateHandle = createSavedStateHandle()
                )
            }
        }
    }
}