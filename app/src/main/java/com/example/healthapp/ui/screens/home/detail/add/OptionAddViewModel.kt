package com.example.healthapp.ui.screens.home.detail.add

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.healthapp.HealthAppApplication
import com.example.healthapp.data.drug.DrugsRepository
import com.example.healthapp.data.ownDrug.OwnDrugRepository
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class UiStateOptionAdd(
    var inputIsNotValid: Boolean = false,
    var inputField: String = "",
    var barCode: String = "",
    var trigger: Boolean = false,
    var showDialog: Boolean = false,
    var pzn: String = "",
    var drugExist: Boolean = false,

    )

class OptionAddViewModel(
    private val drugsRepository: DrugsRepository,
    private val ownDrugRepository: OwnDrugRepository
) : ViewModel() {

    var uiState by mutableStateOf(UiStateOptionAdd())
        private set

    private fun validateInput(input: String): Boolean {
        return input.length == 8 && input.isDigitsOnly()
    }

    fun updateOptionState(updatedUiState: UiStateOptionAdd) {
        uiState = updatedUiState
    }

    suspend fun checkInputFieldPzn(inputPZN: String, navigateTo: (String) -> Unit) {
        if (validateInput(inputPZN)) {
            coroutineScope {
                withContext(Dispatchers.Default) {
                    checkDrugExist(inputPZN )
                }
                Log.d("Teste", "überprüfung fand statt checkDrugExist")
            }
            if (!uiState.drugExist) {
                getDrug(input = inputPZN, navigateTo = navigateTo)
            }
        } else {
            updateOptionState(updatedUiState = uiState.copy(inputIsNotValid = true))
        }
    }

    suspend fun checkCameraScan(barcode: Barcode, navigateTo: (String) -> Unit) {
        val input = barcode.rawValue!!
        val validatedInput = if (validateInput(input)) input else input.substring(1)
        coroutineScope {
            withContext(Dispatchers.Default) {
                checkDrugExist(validatedInput )
            }
        }
        if (!uiState.drugExist) {
            getDrug(input = validatedInput, navigateTo = navigateTo)
        }
    }

    fun checkDrugExist(pzn: String) {
            try {
                val result = ownDrugRepository.getOwnDrugWithoutNotification(pzn = pzn)
                if(result != null){
                    uiState = uiState.copy(drugExist = true)
                }
            } catch (e: Exception) {
                Log.d("Teste", "Fehler: ${e.message}")
            }
    }

    fun getDrug(input: String, navigateTo: (String) -> Unit) {
        viewModelScope.launch {
            try {
                drugsRepository.getDrug(input).collect() {
                    uiState.pzn = it.pzn
                    navigateTo(it.pzn)
                }
            } catch (e: Exception) {
                uiState = uiState.copy(showDialog = true)
                Log.d("testen", "$e")
            }
        }
    }

    fun scanBarcode(context: Context, navigateTo: (String) -> Unit) {
        val options = GmsBarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_CODE_39)
            .enableAutoZoom()
            .build()
        val scanner = GmsBarcodeScanning.getClient(context, options)
        try {
            scanner.startScan().addOnSuccessListener { barcode ->
                viewModelScope.launch {
                    checkCameraScan(barcode = barcode, navigateTo = navigateTo)
                }
            }.addOnFailureListener { e ->
                Log.d("error", "fehler ist unterlaufen und zwar: ${e.message}")
            }
        } catch (e: Exception) {
            Log.d("error", "exception in barcode: " + e.message)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as HealthAppApplication)
                val drugsRepository = app.container.drugsRepository
                val ownDrugRepository = app.container.ownDrugRepository
                OptionAddViewModel(
                    drugsRepository = drugsRepository,
                    ownDrugRepository = ownDrugRepository
                )
            }
        }
    }
}

