package com.example.healthapp.ui.screens.share

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.healthapp.HealthAppApplication
import com.example.healthapp.data.ownDrug.OwnDrug
import com.example.healthapp.data.ownDrug.OwnDrugRepository
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class ShareViewModel(private val ownDrugRepository: OwnDrugRepository) : ViewModel() {

    private val currentDrugList = MutableStateFlow<List<OwnDrug>>(emptyList())

    init {
        viewModelScope.launch {
            ownDrugRepository.getAllOwnDrugs().collect() {
                currentDrugList.value = it
            }
        }
    }

    private fun convertDrugList(): ArrayList<String> {
        val stringArray: ArrayList<String> = arrayListOf()
        currentDrugList.value.forEach {
            stringArray.add(formatDrugToString(it))
        }
        return stringArray
    }

    private fun formatDrugToString(ownDrug: OwnDrug): String =
        "Wirkstoff: ${ownDrug.activeIngredient}, Handelsname: ${ownDrug.name}, Morgens: ${if (ownDrug.morning.isBlank()) "0" else ownDrug.morning}, Mittags: ${if (ownDrug.noon.isBlank()) "0" else ownDrug.noon}, Abends: ${if (ownDrug.evening.isBlank()) "0" else ownDrug.evening}, Nachts: ${if (ownDrug.night.isBlank()) "0" else ownDrug.night}, Indikation: ${ownDrug.indication}, Hinweis: ${ownDrug.notes}"

    fun shareDrugList(context: Context) {
        val file = generatePDF()
        val pdfUri = FileProvider.getUriForFile(context, "com.example.healthapp.fileprovider", file)

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_SUBJECT, "Betreff: Medikationsplan")
            putExtra(Intent.EXTRA_TEXT, "Aktueller Medikationsplan")
            putExtra(Intent.EXTRA_STREAM, pdfUri)
        }
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(
                Intent.createChooser(intent, "")
            )
        }
    }

    private fun generatePDF(): File {
        val document = PdfDocument()
        val pageInfo: PdfDocument.PageInfo = PdfDocument.PageInfo.Builder(1920, 1080, 1).create()
        val page = document.startPage(pageInfo)
        val canvas = page.canvas
        val drugList = convertDrugList()

        val paint = Paint().apply {
            color = Color.Black.toArgb()
            textSize = 18f
        }
        val titlePaint = Paint().apply {
            color = Color.Black.toArgb()
            textSize = 30f
        }
        val startX = 100f
        var startY = 150f
        val rowSpacing = 50f
        canvas.drawText("Medikationsplan", startX, startY, titlePaint)
        for (drug in drugList) {
            startY += rowSpacing
            canvas.drawText(drug, startX, startY, paint)
        }
        document.finishPage(page)
        val downloadDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val fileName = "Medikationsliste.pdf"
        val file = File(downloadDir, fileName)
        if (file.exists()) {
            file.delete()
        }
        try {
            val fos = FileOutputStream(file)
            document.writeTo(fos)
            document.close()
            fos.close()
        } catch (e: FileNotFoundException) {
            throw RuntimeException()
        } catch (e: IOException) {
            throw RuntimeException()
        }
        return file
    }

    fun generateQR(): ImageBitmap {
        val sizePx = 450
        val paddingPx = 0
        val qrCodeWriter = QRCodeWriter()
        val encodeHints = mutableMapOf<EncodeHintType, Any?>()
            .apply {
                this[EncodeHintType.MARGIN] = paddingPx
            }
        val bitmapMatrix = try {
            qrCodeWriter.encode(
                "Medikationsplan HealthApp", BarcodeFormat.QR_CODE,
                sizePx, sizePx, encodeHints
            )
        } catch (ex: WriterException) {
            null
        }
        val matrixWidth = bitmapMatrix?.width ?: sizePx
        val matrixHeight = bitmapMatrix?.height ?: sizePx

        val newBitmap = Bitmap.createBitmap(
            matrixWidth,
            matrixHeight,
            Bitmap.Config.ARGB_8888
        )
        for (x in 0 until matrixWidth) {
            for (y in 0 until matrixHeight) {
                val shouldColorPixel = bitmapMatrix?.get(x, y) ?: false
                val pixelColor =
                    if (shouldColorPixel) android.graphics.Color.BLACK else android.graphics.Color.WHITE
                newBitmap.setPixel(x, y, pixelColor)
            }
        }
        return newBitmap.asImageBitmap()
    }

    companion object {
        var Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[APPLICATION_KEY] as HealthAppApplication)
                val ownDrugRepository = app.container.ownDrugRepository
                ShareViewModel(ownDrugRepository)
            }
        }
    }
}