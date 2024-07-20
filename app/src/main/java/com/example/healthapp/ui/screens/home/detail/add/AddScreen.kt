package com.example.healthapp.ui.screens.home.detail.add

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
fun AddingScreen(
    navigateBack: () -> Unit,
    navigateHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddScreenViewModel = viewModel(factory = AddScreenViewModel.Factory)
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBarArrowName(
                navigateBack = navigateBack,
                name = viewModel.uiStateAdd.drugDetail.name
            )
        }
    ) { innerPadding ->
        AddingScreenBody(
            viewModel = viewModel,
            navigateHome = navigateHome,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun AddingScreenBody(
    navigateHome: () -> Unit,
    viewModel: AddScreenViewModel,
    modifier: Modifier = Modifier
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
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .aspectRatio(16f / 9f)
                .padding(bottom = 20.dp)
        )
        AddFields(viewModel = viewModel)
        AddButton(navigateHome = navigateHome, viewModel = viewModel)
    }
}

@Composable
fun AddFields(
    modifier: Modifier = Modifier,
    viewModel: AddScreenViewModel,
) {
    ListInputTextfield(viewModel = viewModel)
    OutlinedTextField(
        value = viewModel.uiStateAdd.drugDetail.notes,
        onValueChange = { viewModel.updateDrugDetail(viewModel.uiStateAdd.drugDetail.copy(notes = it)) },
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
    modifier: Modifier = Modifier, viewModel: AddScreenViewModel
) {
    Column() {
        OutlinedTextField(
            value = viewModel.uiStateAdd.drugDetail.morning,
            onValueChange = {
                viewModel.updateDrugDetail(viewModel.uiStateAdd.drugDetail.copy(morning = it))
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
                if (viewModel.uiStateAdd.isErrorMorning) {
                    Text(
                        text = "* Die Mengenangabe ist nicht korrekt, sie darf kein '-' , '.' , ',' oder mit 0 anfangen!",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            isError = viewModel.uiStateAdd.isErrorMorning

        )
        OutlinedTextField(
            value = viewModel.uiStateAdd.drugDetail.noon,
            onValueChange = { viewModel.updateDrugDetail(viewModel.uiStateAdd.drugDetail.copy(noon = it)) },
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
                if (viewModel.uiStateAdd.isErrorNoon) {
                    Text(
                        text = "* Die Mengenangabe ist nicht korrekt, sie darf kein '-' , '.' , ',' oder mit 0 anfangen!",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            isError = viewModel.uiStateAdd.isErrorNoon
        )
        OutlinedTextField(
            value = viewModel.uiStateAdd.drugDetail.evening,
            onValueChange = {
                viewModel.updateDrugDetail(
                    viewModel.uiStateAdd.drugDetail.copy(
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
                if (viewModel.uiStateAdd.isErrorEvening) {
                    Text(
                        text = "* Die Mengenangabe ist nicht korrekt, sie darf kein '-' , '.' , ',' oder mit 0 anfangen!",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            isError = viewModel.uiStateAdd.isErrorEvening
        )
        OutlinedTextField(
            value = viewModel.uiStateAdd.drugDetail.night,
            onValueChange = { viewModel.updateDrugDetail(viewModel.uiStateAdd.drugDetail.copy(night = it)) },
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
                if (viewModel.uiStateAdd.isErrorNight) {
                    Text(
                        text = "Bitte verwenden Sie eine gültige Mengenangabe, ohne Sonderzeichen oder führende Nullen.",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Start,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            isError = viewModel.uiStateAdd.isErrorNight
        )

    }
}


@Composable
fun AddButton(
    modifier: Modifier = Modifier,
    navigateHome: () -> Unit,
    viewModel: AddScreenViewModel,
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
                    viewModel.saveDrug(
                        drugDetail = viewModel.uiStateAdd.drugDetail, navigateHome = navigateHome
                    )
                }
            },
            enabled = viewModel.uiStateAdd.hasEntry,
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
fun AddingScreenPreview() {
    AddingScreen(navigateHome = {}, navigateBack = {})
}
