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

data class UiStateHomeDetail(
    var drugDetail: DrugDetail = DrugDetail(),
    var deleteDialog: Boolean = false
)

class HomeDetailViewModel(
    private val ownDrugRepository: OwnDrugRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    var uiStateHomeDetail by mutableStateOf(UiStateHomeDetail())
        private set

    init {
        val pzn: String = checkNotNull(savedStateHandle["pzn"])
        getCurrentDrug(pzn = pzn)
    }

    fun getImage(): Int {
        when (uiStateHomeDetail.drugDetail.pzn) {
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
                uiStateHomeDetail = uiStateHomeDetail.copy(
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
                        amount = it.amount
                    )
                )
            }
        }
    }

    fun setDeleteDialogTrue() {
        uiStateHomeDetail = uiStateHomeDetail.copy(deleteDialog = true)
    }

    fun setDeleteDialogFalse(){
        uiStateHomeDetail = uiStateHomeDetail.copy(deleteDialog = false)
    }

    suspend fun deleteDrug(navigateHome: () -> Unit) {
        viewModelScope.launch {
            try {
                ownDrugRepository.deleteDrug(ownDrug = uiStateHomeDetail.drugDetail.toOwnDrug())
            } catch (e: Exception) {
                Log.d("Teste", "Bin trotzdem hier im catch")
            }
            finally {
                navigateHome()
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
            checkedMorning = false,
            checkedNoon = false,
            checkedEvening = false,
            checkedNight = false,
            amount = amount
        )
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as HealthAppApplication)
                val ownDrugRepository = app.container.ownDrugRepository
                HomeDetailViewModel(ownDrugRepository, this.createSavedStateHandle())
            }
        }

    }
}