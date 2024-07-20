package com.example.healthapp.ui.screens.home.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthapp.R
import com.example.healthapp.ui.screens.scaffold.TopAppBarArrowName
import kotlinx.coroutines.launch

@Composable
fun HomeEditScreen(
    navigateBack: () -> Unit,
    navigateHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val viewModel: HomeEditViewModel = viewModel(factory = HomeEditViewModel.Factory)
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBarArrowName(
                navigateBack = navigateBack,
                name = viewModel.uiStateEdit.drugDetail.name
            )
        }
    ) { innerPadding ->
        HomeEditBodyScreen(
            viewModel = viewModel,
            navigateHome = navigateHome,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun HomeEditBodyScreen(
    viewModel: HomeEditViewModel,
    navigateHome: () -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = viewModel.getImage()),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .aspectRatio(16f / 9f)
                .padding(bottom = 20.dp)
        )
        UpdateFields(viewModel = viewModel)
        ConfirmButton(navigateHome = navigateHome, viewModel = viewModel)
    }
}

@Composable
fun UpdateFields(
    modifier: Modifier = Modifier,
    viewModel: HomeEditViewModel,
) {
    ListInputTextfield(viewModel = viewModel)
    OutlinedTextField(
        value = viewModel.uiStateEdit.drugDetail.notes,
        onValueChange = { viewModel.updateDrugDetail(viewModel.uiStateEdit.drugDetail.copy(notes = it)) },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        maxLines = 8,
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(Color.White, RoundedCornerShape(5.dp))
            .padding(start = 25.dp, end = 25.dp, top = 5.dp, bottom = 25.dp),
        shape = RoundedCornerShape(10.dp),
        label = { Text(text = "Optional - Eigene Notizen") },
    )
}

@Composable
fun ListInputTextfield(
    modifier: Modifier = Modifier,
    viewModel: HomeEditViewModel
) {
    Column() {
        OutlinedTextField(
            value = viewModel.uiStateEdit.drugDetail.morning,
            onValueChange = {
                viewModel.updateDrugDetail(viewModel.uiStateEdit.drugDetail.copy(morning = it))
            },
            shape = RoundedCornerShape(10.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp, vertical = 5.dp)
                .background(Color.White, RoundedCornerShape(5.dp)),
            label = { Text(text = "Menge Morgens 6:00Uhr") },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done, keyboardType = KeyboardType.Number
            ),
            supportingText = {
                if (viewModel.uiStateEdit.isErrorMorning) {
                    Text(
                        text = "* Die Mengenangabe ist nicht korrekt, sie darf kein - oder mit . oder , anfangen!",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            isError = viewModel.uiStateEdit.isErrorMorning

        )
        OutlinedTextField(
            value = viewModel.uiStateEdit.drugDetail.noon,
            onValueChange = { viewModel.updateDrugDetail(viewModel.uiStateEdit.drugDetail.copy(noon = it)) },
            shape = RoundedCornerShape(10.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp, vertical = 5.dp)
                .background(Color.White, RoundedCornerShape(5.dp)),
            label = { Text(text = "Menge Mittags 12:00 Uhr") },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done, keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions.Default,
            supportingText = {
                if (viewModel.uiStateEdit.isErrorNoon) {
                    Text(
                        text = "* Die Mengenangabe ist nicht korrekt, sie darf kein - oder mit . oder , anfangen!",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            isError = viewModel.uiStateEdit.isErrorNoon
        )
        OutlinedTextField(
            value = viewModel.uiStateEdit.drugDetail.evening,
            onValueChange = {
                viewModel.updateDrugDetail(
                    viewModel.uiStateEdit.drugDetail.copy(
                        evening = it
                    )
                )
            },
            shape = RoundedCornerShape(10.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp, vertical = 5.dp)
                .background(Color.White, RoundedCornerShape(5.dp)),
            label = { Text(text = "Menge Abends 18:00 Uhr") },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done, keyboardType = KeyboardType.Number
            ),
            supportingText = {
                if (viewModel.uiStateEdit.isErrorEvening) {
                    Text(
                        text = "* Die Mengenangabe ist nicht korrekt, sie darf kein - oder mit . oder , anfangen!",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            isError = viewModel.uiStateEdit.isErrorEvening
        )
        OutlinedTextField(
            value = viewModel.uiStateEdit.drugDetail.night,
            onValueChange = { viewModel.updateDrugDetail(viewModel.uiStateEdit.drugDetail.copy(night = it)) },
            shape = RoundedCornerShape(10.dp),
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp, vertical = 5.dp)
                .background(Color.White, RoundedCornerShape(5.dp)),
            label = { Text(text = "Menge Nachts 22:00 Uhr") },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done, keyboardType = KeyboardType.Number
            ),
            supportingText = {
                if (viewModel.uiStateEdit.isErrorNight) {
                    Text(
                        text = "* Die Mengenangabe ist nicht korrekt, sie darf kein - oder mit . oder , anfangen!",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            isError = viewModel.uiStateEdit.isErrorNight
        )
    }
}

@Composable
fun ConfirmButton(
    modifier: Modifier = Modifier,
    navigateHome: () -> Unit,
    viewModel: HomeEditViewModel,
) {
    val scope = rememberCoroutineScope()
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 20.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedButton(
            onClick = { navigateHome() },
        ) {
            Text(
                text = "Abbrechen",
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
            )
        }
        Button(
            onClick = {
                scope.launch {
                    viewModel.updateDrug(
                        drugDetail = viewModel.uiStateEdit.drugDetail,
                        navigateHome = navigateHome
                    )
                }
            },
            enabled = viewModel.uiStateEdit.hasEntry,
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
        ) {
            Text(
                text = "Speichern",
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditDeleteScreenPreview() {
    HomeEditScreen(
        navigateHome = {},
        navigateBack = {})
}

