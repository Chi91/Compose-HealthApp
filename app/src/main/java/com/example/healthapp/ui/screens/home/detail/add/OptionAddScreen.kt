package com.example.healthapp.ui.screens.home.detail.add

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthapp.R
import com.example.healthapp.ui.screens.scaffold.TopAppBarArrow
import kotlinx.coroutines.launch


@Composable
fun OptionScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    onNextScreen: (String) -> Unit,
    viewModel: OptionAddViewModel = viewModel(factory = OptionAddViewModel.Factory)
) {
    Scaffold(modifier = modifier, topBar = {
        TopAppBarArrow(navigateBack = { navigateBack() })
    }) { innerPadding ->
        OptionScreenBody(
            onNextScreen = onNextScreen,
            modifier = Modifier.padding(innerPadding),
            viewModel = viewModel
        )
    }
}


@Composable
fun OptionScreenBody(
    onNextScreen: (String) -> Unit, modifier: Modifier = Modifier, viewModel: OptionAddViewModel
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        InstructionCard()
        OptionButtons(
            onNextScreen = onNextScreen, viewModel = viewModel
        )
    }
}

@Composable
fun InstructionCard() {
    Card(
        modifier = Modifier.padding(
            start = 30.dp, end = 30.dp, bottom = 100.dp, top = 15.dp
        )
    ) {
        Text(
            text = stringResource(id = R.string.OptionAddSreenTitle),
            fontSize = 25.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = dimensionResource(id = R.dimen.padding_medium)),
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(id = R.string.OptionAddScreenDescription),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
        )
    }
}

@Composable
fun OptionButtons(
    onNextScreen: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OptionAddViewModel,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 70.dp)
    ) {
        PznTextfield(
            onNextScreen = onNextScreen, viewModel = viewModel
        )
        Text(
            text = stringResource(id = R.string.OptionAddOrText), modifier = Modifier.padding(40.dp)
        )
        BarCodeButton(
            onNextScreen = onNextScreen, viewModel = viewModel
        )
        if (viewModel.uiState.showDialog) {
            HintDialog(viewModel = viewModel)
        }
        if (viewModel.uiState.drugExist) {
            DrugExistDialog(viewModel = viewModel)
        }
    }
}

@Composable
fun DrugExistDialog(viewModel: OptionAddViewModel) {
    AlertDialog(
        onDismissRequest = { viewModel.updateOptionState(viewModel.uiState.copy(showDialog = false)) },
        title = {
            Text(
                text = "Arzneimittel existiert bereits!",
                textAlign = TextAlign.Center
            )
        },
        icon = { Icons.Default.Info },
        text = {
            Text(
                text = "Das Arzneimittel ist bereits hinzugefügt. Scannen Sie ein Anderes ein!",
            )
        },
        confirmButton = {
            TextButton(onClick = {
                viewModel.updateOptionState(
                    viewModel.uiState.copy(
                        drugExist = false
                    )
                )
            }) {
                Text("Verstanden")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                viewModel.updateOptionState(
                    viewModel.uiState.copy(
                        drugExist = false
                    )
                )
            }) {
                Text("Abbrechen")
            }
        },
    )
}

@Composable
fun HintDialog(viewModel: OptionAddViewModel, modifier: Modifier = Modifier) {
    AlertDialog(
        onDismissRequest = { viewModel.updateOptionState(viewModel.uiState.copy(showDialog = false)) },
        title = {
            Text(
                text = "PZN existiert nicht!",
                textAlign = TextAlign.Center,
            )
        },
        icon = { Icons.Default.Info },
        text = {
            Text(
                text = "Entweder existiert die PZN nicht oder die Eingabe war nicht korrekt!",
                modifier = Modifier.padding(start = 5.dp)
            )
        },
        confirmButton = {
            TextButton(onClick = {
                viewModel.updateOptionState(
                    viewModel.uiState.copy(
                        showDialog = false
                    )
                )
            }) {
                Text("Verstanden")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                viewModel.updateOptionState(
                    viewModel.uiState.copy(
                        showDialog = false
                    )
                )
            }) {
                Text("Abbrechen")
            }
        },
    )
}

@Composable
fun PznTextfield(
    onNextScreen: (String) -> Unit, modifier: Modifier = Modifier, viewModel: OptionAddViewModel
) {
    val scope = rememberCoroutineScope()
    OutlinedTextField(value = viewModel.uiState.inputField,
        onValueChange = {
            viewModel.updateOptionState(
                viewModel.uiState.copy(
                    inputField = it, inputIsNotValid = false
                )
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search, contentDescription = "search"
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(10.dp),
        label = { Text(text = stringResource(id = R.string.OptionAddTextfield)) },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done, keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(onDone = {
            scope.launch {
                viewModel.checkInputFieldPzn(
                    inputPZN = viewModel.uiState.inputField, navigateTo = onNextScreen
                )
            }
        }),
        isError = viewModel.uiState.inputIsNotValid,
        modifier = modifier.fillMaxWidth(),
        supportingText = {
            if (viewModel.uiState.inputIsNotValid) {
                Text(
                    text = "* PZN enhält 8 Zahlen!",
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.error
                )
            }
        })
}

@Composable
fun BarCodeButton(
    onNextScreen: (String) -> Unit, modifier: Modifier = Modifier, viewModel: OptionAddViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    Button(
        onClick = {
            scope.launch {
                viewModel.scanBarcode(context, onNextScreen)
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.PhotoCamera,
            contentDescription = null,
            modifier = Modifier.padding(end = 10.dp)
        )
        Text(
            text = stringResource(id = R.string.OptionAddScreenButton), fontSize = 18.sp
        )
    }
}


@Preview(showBackground = true)
@Composable
fun OptionScreenPreview() {
    OptionScreen(navigateBack = {}, onNextScreen = {})
}