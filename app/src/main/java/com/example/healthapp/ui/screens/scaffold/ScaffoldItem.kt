package com.example.healthapp.ui.screens.scaffold

import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import com.example.healthapp.ui.screens.home.HomeViewModel

@Composable
fun FloatingButtonEdit(navigateTo: (String) -> Unit, pzn: String) {
    FloatingActionButton(
        onClick = {
            navigateTo(pzn)
        }
    )
    {
        Icon(
            imageVector = Icons.Filled.Edit,
            contentDescription = null
        )
    }
}

@Composable
fun FloatingButtonAdd(onNextDetailScreen: () -> Unit){
    FloatingActionButton(
        onClick = {
            onNextDetailScreen()
        })
    {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = null
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarArrowName(navigateBack: () -> Unit, name: String) {
    TopAppBar(
        title = { Text(text = name) },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "go back",
                )
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarItem(
    navigateBack: () -> Unit,
    name: String,
    deleteItem: () -> Unit,
    scrollBehavior: TopAppBarScrollBehavior
) {
    TopAppBar(
        title = { Text(text = name, fontSize = 18.sp) },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "back",
                )
            }
        },
        actions = {
            IconButton(onClick = { deleteItem() }) {
                Icon(
                    imageVector =
                    Icons.Default.Delete,
                    contentDescription = null
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarHome(
    scrollBehavior: TopAppBarScrollBehavior,
    datePickerState: DatePickerState,
    viewModel: HomeViewModel
){
    TopAppBar(
        title = { Text(text = "Medikationsplan") },
        actions = {
            IconButton(onClick = { viewModel.showDialog() }) {
                Icon(imageVector = Icons.Default.CalendarMonth, contentDescription = null)
                if (viewModel.uiStateHome.showDialog) {
                    DatePickerDialog(
                        onDismissRequest = { viewModel.dismissDialog() },
                        confirmButton = {
                            TextButton(onClick = { viewModel.dismissDialog() }) {
                                Text("Ok")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { viewModel.dismissDialog() }) {
                                Text("Abbrechen")
                            }
                        }
                    ) {
                        DatePicker(state = datePickerState)
                    }
                }
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Settings, contentDescription = null)
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoNavBar(
    navigateTo: () -> Unit
) {
    TopAppBar(
        title = {
            Text(text = "Information")
        },
        navigationIcon = {
            androidx.compose.material3.IconButton(
                onClick = { navigateTo() }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarArrow(navigateBack: () -> Unit) {
    TopAppBar(
        title = { Text(text = "") },
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "go back",
                )
            }
        }
    )
}
