package com.example.healthapp.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.healthapp.R
import com.example.healthapp.data.ownDrug.OwnDrug
import com.example.healthapp.ui.screens.scaffold.FloatingButtonAdd
import com.example.healthapp.ui.screens.scaffold.TopAppBarHome

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navigateToItem: (String) -> Unit,
    onNextDetailScreen: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val datePickerState = rememberDatePickerState()
    val viewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBarHome(
                scrollBehavior = scrollBehavior,
                datePickerState = datePickerState,
                viewModel = viewModel
            )
        },
        floatingActionButton = {
            FloatingButtonAdd(onNextDetailScreen = onNextDetailScreen)
        },
    ) { innerPadding ->
        ScrollList(
            modifier = Modifier.padding(innerPadding),
            navigateToItem = navigateToItem,
            viewModel = viewModel
        )
    }
}

@Composable
fun ScrollList(
    viewModel: HomeViewModel, modifier: Modifier = Modifier, navigateToItem: (String) -> Unit
) {
    val drugs by viewModel.allDrugs.collectAsState()
    if (drugs.isNotEmpty()) {
        CategorizeList(
            viewModel = viewModel, navigateTo = navigateToItem, modifier = modifier
        )
    } else {
        EmptyList(modifier = modifier)
    }
}

@Composable
fun EmptyList(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Card(
            modifier = Modifier
                .padding(horizontal = 50.dp)
                .padding(top = 50.dp),
        ) {
            Text(
                text = stringResource(id = R.string.EmptyScreen),
                fontSize = 25.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = dimensionResource(id = R.dimen.padding_medium)),
                textAlign = TextAlign.Center
            )
            Text(
                text = "Der Medikationsplan ist aktuell leer, fügen Sie über den + Button ein Arzneimittel hinzu!",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(
                    top = 5.dp, start = 20.dp, end = 20.dp, bottom = 20.dp
                )
            )
        }
        Image(
            painter = painterResource(id = R.drawable.undraw_searching_re_3ra9),
            contentDescription = "picture of empty screen",
            modifier
                .size(280.dp)
                .padding(top = 10.dp)
        )
    }
}

@Composable
fun CategorizeList(
    viewModel: HomeViewModel, navigateTo: (String) -> Unit, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(bottom = 40.dp)
    ) {
        val morning = viewModel.morningDrugs.collectAsState()
        val noon = viewModel.noonDrugs.collectAsState()
        val evening = viewModel.eveningDrugs.collectAsState()
        val night = viewModel.nightDrugs.collectAsState()

        val listPair = listOf(
            Pair("Morgens: 6:00 Uhr", morning.value),
            Pair("Mittags: 12:00 Uhr", noon.value),
            Pair("Abends: 18:00 Uhr", evening.value),
            Pair("Nachts: 22:00 Uhr", night.value),
        )
        for ((time, drugList) in listPair) {
            if (drugList.isNotEmpty()) {
                ListTask(
                    time = time,
                    navigateToItem = navigateTo,
                    drugList = drugList,
                    viewModel = viewModel
                )
            }
        }
        if (viewModel.uiStateHome.drugEmpty) {
            AlertEmptyDrugDialog(viewModel = viewModel)
        }
    }
}

@Composable
fun AlertEmptyDrugDialog(viewModel: HomeViewModel, modifier: Modifier = Modifier) {
    AlertDialog(
        onDismissRequest = { viewModel.updateHomeState(viewModel.uiStateHome.copy(drugEmpty = false)) },
        title = { Text("Achtung!") },
        icon = { Icons.Default.Info },
        text = { Text("Sie haben weniger als 10 Tabletten, wenn es ein verschreibungspflichtes Präperat handelt, kümmern Sie sich um ein neues Rezept !") },
        confirmButton = {
            TextButton(onClick = {
                viewModel.updateHomeState(
                    viewModel.uiStateHome.copy(
                        drugEmpty = false
                    )
                )
            }) {
                Text("Verstanden")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                viewModel.updateHomeState(
                    viewModel.uiStateHome.copy(
                        drugEmpty = false
                    )
                )
            }) {
                Text("Abbrechen")
            }
        },
    )
}

@Composable
fun ListTask(
    time: String,
    navigateToItem: (String) -> Unit,
    drugList: List<OwnDrug>,
    viewModel: HomeViewModel
) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .padding(vertical = 15.dp)
    ) {
        Text(
            text = time, fontSize = 20.sp, modifier = Modifier.padding(10.dp)
        )
        Divider()
        ListItemTask(
            navigateToItem = navigateToItem, drugList = drugList, time = time, viewModel = viewModel
        )
    }
}

@Composable
fun ListItemTask(
    time: String,
    modifier: Modifier = Modifier,
    navigateToItem: (String) -> Unit,
    drugList: List<OwnDrug>,
    viewModel: HomeViewModel
) {
    Column(
        modifier = modifier
    ) {
        drugList.forEachIndexed { index, ownDrug ->
            ListItem(headlineContent = { Text(text = ownDrug.name) },
                leadingContent = {
                    Icon(
                        painter = painterResource(id = R.drawable.pill),
                        contentDescription = "icon pill"
                    )
                },
                trailingContent = {
                    Checkbox(
                        checked = viewModel.getCheckBoxValue(time = time, ownDrug = ownDrug),
                        onCheckedChange = {
                            viewModel.setCheckBox(
                                time = time, ownDrug = ownDrug, valueChecked = it
                            )
                        })
                },
                colors = ListItemDefaults.colors(containerColor = Color.White),
                modifier = Modifier.clickable {
                    navigateToItem(ownDrug.pzn)
                })
            if (drugList.size != index) Divider()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navigateToItem = {}, onNextDetailScreen = {})
}

@Preview(showBackground = true)
@Composable
fun ScrollListPreview() {
}
