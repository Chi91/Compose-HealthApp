package com.example.healthapp.ui.screens.info

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
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.launch
data class UiStateInfo(
    var inputIsNotValid: Boolean = false,
    var inputField: String = "",
    var barCode: String = "",
    var trigger: Boolean = false,
    var showDialog: Boolean = false,
    var pzn: String = ""
)
class InfoViewModel(private val drugsRepository: DrugsRepository) : ViewModel() {

    var uiStateInfo by mutableStateOf(UiStateInfo())
        private set

    private fun validateInput(input: String): Boolean {
        return input.length == 8 && input.isDigitsOnly()
    }

    fun updateInfoState(updatedUiState: UiStateInfo) {
        uiStateInfo = updatedUiState
    }

    fun checkInputFieldPzn(inputPZN: String, navigateTo: (String) -> Unit) {
        if (validateInput(inputPZN)) {
            getDrug(
                input = inputPZN,
                navigateTo = navigateTo
            )
        }else{
            updateInfoState(updatedUiState = uiStateInfo.copy(inputIsNotValid = true))
        }
    }

    fun checkCameraScan(barcode: Barcode, navigateTo: (String) -> Unit) {
        if (validateInput(barcode.rawValue!!)) {
            getDrug(input = barcode.rawValue!!, navigateTo = navigateTo)
        } else {
            getDrug(barcode.rawValue?.substring(1)!!, navigateTo = navigateTo)
        }
    }

    private fun getDrug(input: String, navigateTo: (String) -> Unit) {
        viewModelScope.launch() {
            try {
                drugsRepository.getDrug(input).collect() {
                    uiStateInfo.pzn = it.pzn
                    navigateTo(it.pzn)
                }
            } catch (e: Exception) {
                uiStateInfo = uiStateInfo.copy(showDialog = true)
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
                // Fehler bei der Barcode-Erkennung
                Log.d("Teste", "Bin im addOnFaulreListener: ${e.message}")
            }

        } catch (e: Exception) {
            Log.d("Teste", "fehler ist unterlaufen und zwar: " + e.message)
        }
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as HealthAppApplication)
                val drugsRepository = app.container.drugsRepository
                InfoViewModel(drugsRepository = drugsRepository)
            }
        }
    }
}