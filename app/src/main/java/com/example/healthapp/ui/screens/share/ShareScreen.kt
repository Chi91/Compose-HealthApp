package com.example.healthapp.ui.screens.share

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthapp.R
import com.example.healthapp.ui.theme.HealthAppTheme

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ShareScreen(
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier
) {
    val viewModel: ShareViewModel = viewModel(factory = ShareViewModel.Factory)
    Scaffold(
        modifier = modifier.padding(paddingValues),
    )
    {
        ShareBodyScreen(
            modifier = Modifier.padding(it),
            viewModel = viewModel
        )
    }
}

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun ShareBodyScreen(modifier: Modifier = Modifier, viewModel: ShareViewModel) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        ) {
            Text(
                text = stringResource(id = R.string.QrScreenBoxTitle),
                fontSize = 25.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(id = R.dimen.padding_medium)),
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(id = R.string.QrScreenBoxDescription),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
            )
        }
        Image(
            bitmap = viewModel.generateQR(),
            contentDescription = "QR-Code",
            modifier = modifier
                .height(200.dp)
        )
        Button(
            onClick = {
                viewModel.shareDrugList(context)
            },
            modifier = Modifier,
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
        ) {
            Icon(imageVector = Icons.Filled.Share, contentDescription = null)
            Text(
                text = "Teilen",
                modifier = Modifier.padding(
                    horizontal = dimensionResource(id = R.dimen.padding_medium)
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QrShareScreenPreview() {
    HealthAppTheme() {
        ShareScreen(paddingValues = PaddingValues(5.dp))
    }
}

